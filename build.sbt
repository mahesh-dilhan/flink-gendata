name := "sensergen"

version := "0.1"

scalaVersion := "2.12.11"

libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-scala" % "1.8.0",
  "org.apache.flink" %% "flink-streaming-scala" % "1.8.0"
)