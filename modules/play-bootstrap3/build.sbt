name := """play-bootstrap3"""

scalaVersion := "2.11.7"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  specs2 % Test
  , "org.webjars" % "jquery" % "2.1.4"
  , "org.webjars" % "bootstrap" % "3.3.4"
)

scalariformSettings
