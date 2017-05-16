package drivers

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.SparkSession
import util.BaseDriver

/**
  * Digit recognizer from the famous MNIST dataset
  */
object DecisionTreeDigitRecognizer extends BaseDriver {

  override def run(spark: SparkSession, config: Map[String, String]): Unit = {
    val sc = spark.sparkContext
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._

    val treePath = config("tree.libsvm")
    val data = sqlContext.read.format("libsvm").load(treePath)
    val Array(train, test) = data.randomSplit(Array(0.7, 0.3))

    train.printSchema()
    train.show(5)

    //build an index mapping labels [0-9] to an index [0-9], pretty sure this was unnecessary, maybe it's good standard practice but for this use case, didn't need it
//    val stringIndexer = new StringIndexer()
//      .setInputCol("label")
//      .setOutputCol("indexedLabel")
//      .fit(train)

    val tree = new DecisionTreeClassifier()
      .setMaxDepth(10)
      .setLabelCol("label")
      .setFeaturesCol("features")

    // Convert indexed labels back to original labels.
//    val indexToString = new IndexToString()
//      .setInputCol("prediction")
//      .setOutputCol("predictedLabel")
//      .setLabels(stringIndexer.labels)

    val pipeline = new Pipeline().setStages(Array(tree))

    val model = pipeline.fit(train)

    val result = model.transform(test)
    val rows = result.collect().map { row =>
      val p = row.getAs[org.apache.spark.ml.linalg.Vector]("probability").toArray

      (row.getAs[Double](tree.getLabelCol), row.getAs[Double](tree.getPredictionCol), p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9))
    }

    writeToCSV(sc.parallelize(rows).toDF(), "target/csv")

    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol(tree.getLabelCol)
      .setPredictionCol(tree.getPredictionCol)
      .setMetricName("accuracy")

    val accuracy = evaluator.evaluate(result)
    println(s"Accuracy: $accuracy")

    //output model for use in the photo OCR app
    model.write.overwrite().save("models/tree-digit-recognizer")
  }
}