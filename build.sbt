organization := "com.github.losizm"
name         := "t2"
version      := "2.0.0"
description  := "Utility for text tables"
homepage     := Some(url("https://github.com/losizm/t2"))
licenses     := List("Apache License, Version 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

versionScheme := Some("early-semver")

scalaVersion := "3.1.2"

scalacOptions := Seq("-deprecation", "-feature", "-new-syntax", "-Werror", "-Yno-experimental")

Compile / doc / scalacOptions := Seq(
  "-project", name.value,
  "-project-version", version.value,
  "-doc-root-content:src/main/scala/root.scala"
)

libraryDependencies += "org.scalatest" %% "scalatest-flatspec" % "3.2.12" % "test"

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
