logLevel := Level.Warn

libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.10.2"

addSbtPlugin("com.earldouglas" % "xsbt-web-plugin" % "4.2.4")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.1.0")

addSbtPlugin("com.github.sbt" % "sbt-avro" % "3.2.0")

addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "7.4.0")

addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.32")

addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.5.1")
