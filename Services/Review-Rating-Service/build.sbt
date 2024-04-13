import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

val circeVersion = "0.14.1"

lazy val root = (project in file("."))
  .settings(
    name := "SparkScala",

    resolvers ++= Seq(
      Classpaths.typesafeReleases,
      "confluent" at "https://packages.confluent.io/maven/"
    ),

    libraryDependencies ++= Seq(

      "org.apache.spark" %% "spark-core" % "3.5.0",
      "org.apache.spark" %% "spark-streaming" % "3.5.0",
      "org.apache.spark" %% "spark-sql" % "3.5.0",
      "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.5.0",
      "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.5.0",
      "com.lihaoyi" %% "upickle" % "3.2.0",
      "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0",
      "com.lihaoyi" %% "os-lib" % "0.9.0",





    ),

      libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
      ).map(_ % circeVersion)
  )


