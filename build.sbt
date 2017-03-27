name := """scala-blog"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

routesGenerator := InjectedRoutesGenerator

lazy val reactiveMongoVer: String = {
  "0.12.0"
}

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % reactiveMongoVer
)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % "test"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

scalacOptions in ThisBuild ++= Seq("-feature", "-language:postfixOps")