package drivers

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorAssembler}
import org.apache.spark.sql.SparkSession
import util.BaseDriver

/**
  * Digit recognizer from the famous MNIST dataset
  *
  * TODO basic implementation working
  * TODO visualize what's actually going on here
  * TODO build separate photo recognition software
  * TODO compare CSV to Parquet
  */
object NeuralNetDigitRecognizer extends BaseDriver {

  override def run(spark: SparkSession, config: Map[String, String]): Unit = {
    val trainPath = config("digit.csv.train")
    val testPath = config("digit.csv.test")

    val train = spark.read.option("header", "true").option("inferSchema", "true").csv(trainPath)
    val test = spark.read.option("header", "true").option("inferSchema", "true").csv(testPath)

    val label = "label"
    val labels: Seq[String] = (0 to 9).map(_.toString)
    val features: Array[String] = (0 to 783).map(i => s"pixel$i").toArray

    //input, two hidden layers of 784 and 800, then output
    val layers = Array[Int](features.length, 784, 800, labels.length)

    train.cache()
    test.cache()

    println(s"Train ${train.count()}")
    println(s"Test ${test.count()}")

    val assembler = new VectorAssembler().setInputCols(features)
    val stringIndexer = new StringIndexer().setInputCol(label).fit(train)

    val mlp = new MultilayerPerceptronClassifier()
      .setLabelCol(stringIndexer.getOutputCol)
      .setFeaturesCol(assembler.getOutputCol)
      .setLayers(layers)
      .setSeed(42L)
      .setBlockSize(128) //default 128
      .setMaxIter(config("digit.iterations").toInt) //default 100
      .setTol(1e-7) //default 1e-4

    val indexToString = new IndexToString()
      .setInputCol(mlp.getPredictionCol)
      .setLabels(stringIndexer.labels)

    val pipeline = new Pipeline().setStages(Array(assembler, stringIndexer, mlp, indexToString))

    val model = pipeline.fit(train)
    val result = model.transform(test)

    val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol(stringIndexer.getOutputCol)
      .setPredictionCol(mlp.getPredictionCol)
      .setMetricName("accuracy")

    val precision = evaluator.evaluate(result)
    println(s"Precision: $precision")

    //output model for use in the photo OCR app
    model.write.overwrite().save("digit-recognizer")
  }
}