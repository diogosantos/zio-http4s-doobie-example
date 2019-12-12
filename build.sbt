

val ZIOVersion = "1.0.0-RC17"
val ZIOInteropCatsVersion = "2.0.0.0-RC4"
val DoobieVersion = "0.7.0-M5"
val PureConfigVersion = "0.11.0"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % ZIOVersion,
  "dev.zio" %% "zio-interop-cats" % ZIOInteropCatsVersion,

  "com.github.pureconfig" %% "pureconfig" % PureConfigVersion,
  
  "org.tpolecat" %% "doobie-core" % DoobieVersion,
  "org.tpolecat" %% "doobie-h2" % DoobieVersion
)
