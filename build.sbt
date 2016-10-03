import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences.{AlignSingleLineCaseStatements, BooleanPreferenceDescriptor, DoubleIndentClassDeclaration, SpacesAroundMultiImports}

name := "busy"
organization := "com.viajobien"
version := "0.1.0"
scalaVersion := "2.11.8"

lazy val root = project in file(".")

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value
compileScalastyle <<= compileScalastyle triggeredBy (compile in Compile)

ScalariformKeys.preferences := {

  case object DoubleIndentMethodDeclaration extends BooleanPreferenceDescriptor {
    val key = "doubleIndentMethodDeclaration"
    val description = "Double indent a method's parameters"
    val defaultValue = false
  }

  ScalariformKeys.preferences.value
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(DoubleIndentMethodDeclaration, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(SpacesAroundMultiImports, true)
}

coverageMinimum := 70
coverageFailOnMinimum := true
coverageHighlighting := false
coverageExcludedPackages := "<empty>;Reverse.*;views.*;mongo.*;global.*;router.*"

incOptions := incOptions.value.withNameHashing(true)

libraryDependencies ++= {
  val playVersion = "2.5.6"
  val akkaVersion = "2.4.9"
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

pomExtra := {
  <url>https://github.com/viajobien/busy</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
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

