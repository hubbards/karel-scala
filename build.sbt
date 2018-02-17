// sbt build file

// factor out common settings into a sequence
lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.7"
)

// define ModuleID for library dependencies
libraryDependencies += "junit" % "junit" % "4.12" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "Karel",
    scalaSource in Compile := baseDirectory.value / "src"
  )
