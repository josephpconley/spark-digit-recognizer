import java.io.File

import com.sksamuel.scrimage.Image
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.linalg.{DenseVector, Vector}
import org.apache.spark.sql.SparkSession
import util.BaseDriver

case class InputImage(features: Vector)

object ImagePredictor extends BaseDriver {

  override def run(spark: SparkSession, config: Map[String, String]): Unit = {
    val sc = spark.sparkContext
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._

    val fiveResized = Image.fromFile(new File("five-resized.jpg"))
    val vector = new DenseVector(fiveResized.pixels.map(p => (p.red + p.green + p.blue) / 3.0))

    val model = PipelineModel.load("models/tree-digit-recognizer")
    val input = spark.createDataFrame(sc.parallelize(Seq(InputImage(vector))))

    val result = model.transform(input)
    result.show()

    val rows = result.collect().map { row =>
      val p = row.getAs[org.apache.spark.ml.linalg.Vector]("probability").toArray

      (row.getAs[String]("predictedLabel"), p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9))
    }

    writeToCSV(sc.parallelize(rows).toDF(), "target/five")
  }
}