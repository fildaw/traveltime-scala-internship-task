ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
    name := "traveltime-scala-internship-task"
  )
enablePlugins(JavaAppPackaging)
enablePlugins(AshScriptPlugin)
maintainer := "fildaw"

// https://mvnrepository.com/artifact/com.google.geometry/s2-geometry
libraryDependencies += "com.google.geometry" % "s2-geometry" % "2.0.0"

libraryDependencies += "io.circe" %% "circe-parser" % "0.14.14"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"