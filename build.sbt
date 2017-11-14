name := "sangria"
version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.3"
scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.sangria-graphql" %% "sangria" % "1.3.0",
  "org.sangria-graphql" %% "sangria-spray-json" % "1.0.0",
  "org.sangria-graphql" %% "sangria-circe" % "1.1.0",
  "com.typesafe.akka" %% "akka-http" % "10.0.9",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.9",

  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)

val circeVersion = "0.8.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

Revolver.settings
enablePlugins(JavaAppPackaging)
