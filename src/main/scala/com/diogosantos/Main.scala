package com.diogosantos

import zio.console.{getStrLn, putStrLn}
import zio.{App, ZIO}

object Main extends App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] =
    appLogic.fold(_ => 1, _ => 0)

  val appLogic =
    for {
      _    <- putStrLn("Hello! This is your ZIO app.")
      name <- getStrLn
      _    <- putStrLn(s"Nice to see you here $name! Welcome!")
    } yield ()

}
