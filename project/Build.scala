import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtScalariform._
import scalariform.formatter.preferences.AlignSingleLineCaseStatements

object Build extends Build {

  lazy val root = Project(
    "scala-in-action",
    file("."),
    settings =
      Defaults.defaultSettings ++ 
      scalariformSettings ++
      Seq(
        organization := "name.heikoseeberger",
        scalaVersion := "2.10.1",
        scalacOptions ++= Seq(
          "-unchecked",
          "-deprecation",
          "-Xlint",
          "-language:_", 
          "-target:jvm-1.7",
          "-encoding", "UTF-8"
        ),
        libraryDependencies ++= Seq(
          Dependencies.Compile.httpClient,
          Dependencies.Test.scalaTest,
          Dependencies.Test.scalaCheck
        ),
        fork in run := true,
        ScalariformKeys.preferences <<= ScalariformKeys.preferences(_.setPreference(AlignSingleLineCaseStatements, true))
      )
  )

  object Dependencies {

    object Compile {
      val httpClient = "org.apache.httpcomponents" % "httpclient" % "4.2.3"
    }

    object Test {
      val scalaTest = "org.scalatest" %% "scalatest" % "2.0.M6-SNAP12" % "test"
      val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
    }
  }
}
