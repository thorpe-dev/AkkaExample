name := "TestActors"

version := "1.0"

scalaVersion := "2.10.0-RC5"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.0-RC5" cross CrossVersion.full
