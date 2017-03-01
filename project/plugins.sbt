// The Typesafe repository
resolvers += Resolver.typesafeRepo("releases")

// Eclipse sbt plugin
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.1.0")

// Check style
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")

// Coverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")
addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.1.0")

// Publishing
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.1")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "1.1")

