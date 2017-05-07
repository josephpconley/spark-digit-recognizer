package util

import java.io.File
import java.net.URL
import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.joda.time.DateTime

import scala.collection.JavaConversions._
import scala.io.Source

trait BaseDriver extends Logging with Writers {

  def main(args: Array[String]): Unit = {
    val (sparkConf, config) = loadConfig(args.headOption)

    val spark = SparkSession.builder()
      .appName(this.getClass.getName)
      .config(sparkConf)
      .getOrCreate()

    val startTime = DateTime.now()
    println(s"Started at ${startTime.toString()}")

    try{
      run(spark, config)
    } catch {
      case e: Exception =>
        logger.error("ERROR", e)

    } finally {
      val endTime = DateTime.now()
      println(s"Finished at ${endTime.toString()}, took ${(endTime.getMillis.toDouble - startTime.getMillis.toDouble) / 1000} s")

      spark.stop()
    }
  }

  private def loadConfig(pathArg: Option[String]): (SparkConf, Map[String, String]) = {
    val configPath: URL = pathArg.map(new File(_).toURI.toURL).getOrElse(getClass.getResource("/spark.properties"))
    println(s"Loading properties from $configPath")

    val config = Option(configPath).map { url =>
      val source = Source.fromURL(url)

      val properties = new Properties()
      properties.load(source.bufferedReader())

      properties.propertyNames().map {
        case key: String => key -> properties.getProperty(key)
      }.toMap

    }.getOrElse {
      Map.empty[String, String]
    }

    val sparkConf = new SparkConf().setAll(config)
    println("Config")
    (sparkConf.getAll.toSet ++ config.toSet).toList.sortBy(_._1).foreach(println)

    sparkConf -> config
  }

  def run(spark: SparkSession, config: Map[String, String]): Unit
}