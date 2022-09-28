name := """listpage"""

version := "1.0-SNAPSHOT"

lazy val playBootstrap3 = (project in file("modules/play-bootstrap3"))
    .enablePlugins(PlayScala)

lazy val crud = (project in file("modules/crud"))
    .enablePlugins(PlayScala, PlayJava)

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
    .dependsOn(crud, playBootstrap3)
//    .aggregate(crud, playBootstrap3)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc
)

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
//routesGenerator := InjectedRoutesGenerator
