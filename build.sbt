import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scalariform.formatter.preferences._

name := "busy"
organization := "com.viajobien"
version := "0.1.0"
scalaVersion := "2.11.8"

lazy val root = project in file(".")

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value
compileScalastyle <<= compileScalastyle triggeredBy (compile in Compile)

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(DoubleIndentMethodDeclaration, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(SpacesAroundMultiImports, true)

coverageMinimum := 75
coverageFailOnMinimum := true
coverageHighlighting := false
coverageExcludedPackages := "<empty>;Reverse.*;views.*;mongo.*;global.*;router.*"

incOptions := incOptions.value.withNameHashing(true)

libraryDependencies ++= {
  val playVersion = "2.5.9"
  val akkaVersion = "2.4.11"
  Seq(
    "com.typesafe.play" %% "play-ws" % playVersion,
    //  "com.typesafe.play" %% "play-json" % playVersion,
    "com.typesafe.play" %% "play" % playVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.google.inject" % "guice" % "4.0",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    "org.scalatestplus" %% "play" % "1.4.0" % Test,
    "org.mockito" % "mockito-core" % "1.10.19" % Test
  )
}

scalacOptions ++= Seq(
  "-feature",
  "-language:postfixOps",
  "-language:reflectiveCalls"
)

credentials += Credentials(Path.userHome / ".ivy2" / ".vb_sonatype")
sonatypeProfileName := organization.value
publishMavenStyle := true
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

homepage := Some(url("https://github.com/viajobien/busy"))
licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0"))
pomExtra := {
  <scm>
    <url>git@github.com:viajobien/busy.git</url>
    <connection>scm:git@github.com:viajobien/busy.git</connection>
  </scm>
  <developers>
    <developer>
      <id>LeonhardtDavid</id>
      <name>David Leonhardt</name>
      <url>https://github.com/LeonhardtDavid</url>
    </developer>
    <developer>
      <id>lucasmatw</id>
      <name>Lucas</name>
      <url>https://github.com/lucasmatw</url>
    </developer>
  </developers>
}

