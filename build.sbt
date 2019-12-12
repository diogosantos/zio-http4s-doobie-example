

scalacOptions ++= Seq(
  "-Ypartial-unification"
)

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

  "com.h2database" % "h2" % H2Version
)
