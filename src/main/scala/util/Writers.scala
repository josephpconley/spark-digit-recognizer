package util

import org.apache.spark.sql.DataFrame

trait Writers {

  def writeToCSV(df: DataFrame, folderName: String) =
    df.repartition(1).write
      .mode("overwrite")
      .format("com.databricks.spark.csv")
      .option("header", "true")
      .save(folderName)
}
