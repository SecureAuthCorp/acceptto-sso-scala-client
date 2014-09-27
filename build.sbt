name := "acceptto-sso-scala-client"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

resolvers ++= Seq(
  "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
  "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "scribe-java-mvn-repo" at "https://raw.github.com/fernandezpablo85/scribe-java/mvn-repo/")

libraryDependencies ++= Seq(
  "org.pac4j"         % "play-pac4j_scala" % "1.3.0-SNAPSHOT",
  "org.pac4j"         % "pac4j-cas"            % "1.6.0-SNAPSHOT"
)  

