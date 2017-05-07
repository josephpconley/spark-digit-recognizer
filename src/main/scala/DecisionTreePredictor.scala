import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession
import util.BaseDriver

object DecisionTreePredictor extends BaseDriver {
  override def run(spark: SparkSession, config: Map[String, String]): Unit = {
    val sqlContext = spark.sqlContext

    val model = PipelineModel.load("models/tree-digit-recognizer")
    val testPath = config("tree.libsvm")
    val test = sqlContext.read.format("libsvm").load(testPath)
    test.cache()

    test.printSchema()

    val result = model.transform(test)
    result.show()
  }
}
