lazy val commonSettings = Seq(
  name := "SimpleServer",
  version := "0.1",
  scalaVersion := "2.12.6",
  resolvers += Resolver.sonatypeRepo("snapshots")
)

lazy val libraries = Seq (
  guice
)

lazy val dockerSettings = Seq(
  dockerBaseImage := "openjdk:jre-alpine",
  dockerExposedPorts := Seq(9000)
)

lazy val root = (project in file("."))
  .settings(commonSettings:_*)
  .settings(dockerSettings:_*)
  .settings(libraryDependencies ++= libraries)
  .enablePlugins(PlayScala, JavaAppPackaging, AshScriptPlugin)
