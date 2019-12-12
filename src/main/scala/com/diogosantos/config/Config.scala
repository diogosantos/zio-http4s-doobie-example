package com.diogosantos.config

import pureconfig.loadConfigOrThrow
import zio.{TaskR, Task, ZIO}
import pureconfig.generic.auto._


case class Config(apiConfig: ApiConfig, dbConfig: DbConfig)

case class ApiConfig(endpoint: String, port: Int)

case class DbConfig(url: String, user: String, password: String)


trait Configuration extends Serializable {
  val config: Configuration.Service[Any]
}

object Configuration {

  trait Service[R] {
    val load: TaskR[R, Config]
  }

  trait Live extends Configuration {
    val config: Service[Any] = new Service[Any] {
      override val load: Task[Config] = ZIO.effectTotal(loadConfigOrThrow[Config])
    }
  }

  // for testing purposes
//  trait Test extends Configuration {
//    val config: Service[Any] = new Service[Any] {
//      override val load: Task[Config] = ZIO.effectTotal(Config(???, ???))
//    }
//  }

}

