#!/bin/bash

JAR=$(find target/scala-2.10/ -maxdepth 1 -name *.jar 2>&1)
SPARK_HOME="/opt/spark-2.1.1-bin-hadoop2.7"
PROPS="spark.properties"

export HADOOP_CONF_DIR=/etc/hadoop/conf

nohup $SPARK_HOME/bin/spark-submit \
--class DigitRecognizer \
--deploy-mode client \
--properties-file $PROPS \
$JAR \
$PROPS > nohup.out 2>&1 </dev/null &