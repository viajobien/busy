import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

name := "busy"
organization := "com.viajobien"
version := "0.2.0"
scalaVersion := "2.12.3"

crossScalaVersions := Seq("2.11.11")

lazy val root = project in file(".")

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
scalastyleFailOnError := true
compileScalastyle := scalastyle.in(Compile).toTask("").value
compileScalastyle in Compile := (compileScalastyle in Compile).dependsOn(SbtScalariform.autoImport.scalariformFormat in Compile).value
compile in Compile := (compile in Compile).dependsOn(compileScalastyle in Compile).value

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DoubleIndentMethodDeclaration, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(SpacesAroundMultiImports, true)

coverageMinimum := 75
coverageFailOnMinimum := true
coverageHighlighting := false
coverageExcludedPackages := "<empty>exceptions.*;"

incOptions := incOptions.value.withNameHashing(true)

libraryDependencies ++= {
  val playVersion = "2.6.3"
  Seq(
    "com.typesafe.play" %% "play-json" % "2.6.3",
    "com.typesafe.play" %% "play-ws" % playVersion,
    "com.typesafe.play" %% "play" % playVersion,
    "com.google.inject" % "guice" % "4.1.0" exclude("com.google.guava", "guava"),
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % Test,
    "org.mockito" % "mockito-core" % "2.8.47" % Test
  )
}

parallelExecution in Test := false

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

