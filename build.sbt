name := "Game Translation"

version := "1.0.0"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalaVersion := "2.11.11"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean,SbtWeb)

libraryDependencies ++= Seq(
  javaWs,
  javaJdbc,
  filters,
  cache,
  javaJpa,
  evolutions,
  jdbc,
  //ここから
  "mysql" % "mysql-connector-java" % "8.0.7-dmr",
  "com.typesafe.play" %% "play-mailer" % "5.0.0",
  "com.typesafe.play.modules" %% "play-modules-redis" % "2.5.0",
  "net.arnx" % "jsonic" % "1.3.10",
  "eu.bitwalker" % "UserAgentUtils" % "1.20",
  "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.0-akka-2.4.x",
  "org.jsoup" % "jsoup" % "1.10.2",
  "com.stripe" % "stripe-java" % "5.7.1"
)

resolvers += Resolver.typesafeRepo("releases")
resolvers += Resolver.sonatypeRepo("releases")
