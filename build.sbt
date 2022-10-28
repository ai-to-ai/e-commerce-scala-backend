name := "nichoshop_backend"

version := "1.0"

scalaVersion := "2.12.8"

val scalatraVersion = "2.6.5"

val scalazVersion = "7.1.0"

val akkaVersion = "2.4.12"

val slickVersion = "2.1.0"

resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"

resolvers += "Spy" at "https://files.couchbase.com/maven2/"

resolvers += "sbt-plugin-releases" at "https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases"

avroStringType := "String"
avroFieldVisibility := "private"
avroCreateSetters := false
avroGenerate := Seq(file(baseDirectory.value.absolutePath + "/src/main/generated"))

enablePlugins(JettyPlugin)
containerPort := 9090

javaOptions in Jetty ++= Seq(
  "-Xdebug",
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000"
)

enablePlugins(SbtTwirl)

libraryDependencies ++= Seq(
  "com.zaxxer" % "HikariCP" % "2.2.5",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "com.typesafe.slick" %% "slick" % slickVersion,
  "commons-lang" % "commons-lang" % "2.6",
  "org.apache.commons" % "commons-email" % "1.4",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "org.apache.httpcomponents" % "httpclient" % "4.3.4",
  "org.scala-lang" % "scala-reflect" % "2.11.7",
  "org.scalatra" %% "scalatra" % scalatraVersion,
  "org.scalatra" %% "scalatra-auth" % scalatraVersion,
  "org.scalatra" %% "scalatra-scalate" % scalatraVersion,
  "org.scalatra" %% "scalatra-specs2" % scalatraVersion % "test",
  "org.scalatra" %% "scalatra-json" % scalatraVersion,
  "net.databinder.dispatch" %% "dispatch-core" % "0.12.0",
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "org.eclipse.jetty" % "jetty-webapp" % "9.2.9.v20150224" % "container;compile",
  "com.twilio.sdk" % "twilio-java-sdk" % "4.4.1",
  "org.json4s" %% "json4s-jackson" % "3.5.0",
  "org.json4s" %% "json4s-native" % "3.5.3",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "mysql" % "mysql-connector-java" % "5.1.33",
  "javax.servlet" % "javax.servlet-api" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.hsqldb" % "hsqldb" % "2.3.2" % "test",
  "org.scalatra" %% "scalatra-swagger" % scalatraVersion,
  "io.monix" %% "shade" % "1.10.0",
  "org.apache.avro" % "avro" % "1.10.2",
  "com.duosecurity" % "duo-universal-sdk" % "1.1.3",
  "io.jsonwebtoken" % "jjwt" % "0.9.1",
  "javax.xml.bind" % "jaxb-api" % "2.3.0",
  "com.snowplowanalytics" %% "scala-maxmind-iplookups" % "0.8.0",
  "org.scalatra" %% "scalatra-fileupload" % "2.5.4",
  "io.spray" %% "spray-json" % "1.3.5",
  "com.sksamuel.avro4s" %% "avro4s-core" % "1.8.3"
  )

inThisBuild(
  List(
    scalaVersion := "2.12.8",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

lazy val nichoShopProject = project.settings(
  scalacOptions += " -Ywarn-unused-import -Ywarn-unused -Xlint:unused" // required by `RemoveUnused` rule
)

import sbtassembly.MergeStrategy

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}

mainClass in (Compile, run) := Some("com.nichoshop.JettyStart")
mainClass in (Compile, packageBin) := Some("com.nichoshop.JettyStart")
