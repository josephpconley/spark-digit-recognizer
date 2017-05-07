name := "spark-digit-recognizer"

version := "1.0.0"

scalaVersion := "2.11.8"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

val sparkVersion = "2.1.1"

val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion
)

libraryDependencies ++= Seq(
  "joda-time" % "joda-time" % "2.9.9",
  "com.typesafe" % "config" % "1.3.1",
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.7",
  "com.sksamuel.scrimage" %% "scrimage-io-extra" % "2.1.7",
  "com.sksamuel.scrimage" %% "scrimage-filters" % "2.1.7"
)

libraryDependencies ++= sparkDependencies.map(_ % "provided")

outputStrategy := Some(StdoutOutput)

run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))

lazy val mainRunner = project.in(file("mainRunner")).dependsOn(RootProject(file("."))).settings(
  scalaVersion := "2.11.8",
  libraryDependencies ++= sparkDependencies.map(_ % "compile"),
  assembly := new File("")
)