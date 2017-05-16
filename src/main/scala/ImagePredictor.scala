import java.io.File

import com.sksamuel.scrimage.Image
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.linalg.{DenseVector, Vector}
import org.apache.spark.sql.SparkSession
import util.BaseDriver

case class InputImage(features: Vector, label: Option[Int] = None)

object ImagePredictor extends BaseDriver {

  override def run(spark: SparkSession, config: Map[String, String]): Unit = {
    val sc = spark.sparkContext
    val sqlContext = spark.sqlContext
    import sqlContext.implicits._

//    val fiveResized = Image.fromFile(new File("five-resized.jpg"))

//    val vectors = (0 to 4).map { n =>
//      val img = Image.fromFile(new File(s"images/mnist/$n.png"))
//      InputImage(new DenseVector(img.pixels.map(p => (p.red + p.green + p.blue) / 3.0)), n)
//    }

    val front = new File("images/joe-front.jpg")
    val img = Image.fromFile(front)

    val stepSize = img.width / 9
    val vectors = (0 until 9).map { idx =>
      val score = Image(stepSize, img.height, img.pixels(idx * stepSize, 0, stepSize, img.height))
        .scaleToWidth(28)
        .pixels

      InputImage(new DenseVector(score.map(p => (255 - (p.red + p.green + p.blue) / 3).toDouble)))
    }

    val model = PipelineModel.load("models/tree-digit-recognizer")
    val input = spark.createDataFrame(sc.parallelize(vectors))

    input.printSchema()

    val result = model.transform(input)
    result.show()

    val rows = result.collect().map { row =>
      val p = row.getAs[org.apache.spark.ml.linalg.Vector]("probability").toArray

      (row.getAs[Double]("prediction"), p(0), p(1), p(2), p(3), p(4), p(5), p(6), p(7), p(8), p(9))
    }

    writeToCSV(sc.parallelize(rows).toDF(), "target/mnist")
  }
}