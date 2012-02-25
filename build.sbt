name := "scala-hexmap"

version := "1.0"

scalaVersion := "2.9.1"

// Import web settings from plugin
seq(webSettings :_*)

scalacOptions ++= Seq("-deprecation")

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.8" % "test",
  "org.scalatra" %% "scalatra" % "2.0.2",
  "org.scalatra" %% "scalatra-scalate" % "2.0.2",
  "org.scalatra" %% "scalatra-specs2" % "2.0.2" % "test",
  "ch.qos.logback" % "logback-classic" % "1.0.0" % "runtime",
  "net.databinder" %% "dispatch-http" % "0.8.6",
  "org.eclipse.jetty" % "jetty-webapp" % "8.0.4.v20111024" % "container",
  "javax.servlet" % "servlet-api" % "2.5" % "provided",
  "net.liftweb" %% "lift-json" % "2.4"
)
