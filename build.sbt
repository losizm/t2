organization := "com.github.losizm"
name         := "t2"
version      := "0.1.0-SNAPSHOT"
description  := "Utility for printing text tables"
homepage     := Some(url("https://github.com/losizm/t2"))
licenses     := List("Apache License, Version 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

scalaVersion := "2.13.5"
scalacOptions ++= Seq("-deprecation", "-feature", "-Xcheckinit")

Compile / doc / scalacOptions ++= Seq(
  "-doc-title",   name.value,
  "-doc-version", version.value
)

crossScalaVersions := Seq("2.12.13")

unmanagedSourceDirectories in Compile += {
  (sourceDirectory in Compile).value / s"scala-${scalaBinaryVersion.value}"
}

libraryDependencies += "org.scalatest" %% "scalatest-flatspec" % "3.2.5" % "test"

scmInfo := Some(
  ScmInfo(
    url("https://github.com/losizm/t2"),
    "scm:git@github.com:losizm/t2.git"
  )
)

developers := List(
  Developer(
    id    = "losizm",
    name  = "Carlos Conyers",
    email = "carlos.conyers@hotmail.com",
    url   = url("https://github.com/losizm")
  )
)

publishMavenStyle := true

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org"
  isSnapshot.value match {
    case true  => Some("snaphsots" at s"$nexus/content/repositories/snapshots")
    case false => Some("releases"  at s"$nexus/service/local/staging/deploy/maven2")
  }
}