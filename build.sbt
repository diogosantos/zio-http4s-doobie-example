

scalacOptions ++= Seq(
  "-Ypartial-unification"
)

val CirceVersion = "0.12.3"
val CirceVersionExtras = "0.12.2"
val Http4sVersion = "0.20.13"
val ZIOVersion = "1.0.0-RC8-4"
val DoobieVersion = "0.7.0-M5"
val H2Version = "1.4.199"
val PureConfigVersion = "0.11.0"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % ZIOVersion,
  "dev.zio" %% "zio-interop-cats" % ZIOVersion,

  "com.github.pureconfig" %% "pureconfig" % PureConfigVersion,

  "org.tpolecat" %% "doobie-core" % DoobieVersion,
  "org.tpolecat" %% "doobie-h2" % DoobieVersion,

  "com.h2database" % "h2" % H2Version,

  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,

  "io.circe" %% "circe-generic" % CirceVersion,
  "io.circe" %% "circe-generic-extras" % CirceVersionExtras,
)
