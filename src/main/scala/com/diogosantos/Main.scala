package com.diogosantos


import com.diogosantos.config.Configuration
import com.diogosantos.db.Persistence
import com.diogosantos.http.Api
import cats.effect.ExitCode
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import zio.blocking.Blocking
import zio.clock.Clock
import zio.{Task, TaskR, ZIO, _}
import zio.interop.catz._


object Main extends App {

  override def run(args: List[String]): ZIO[Main.Environment, Nothing, Int] = {

    type AppEnvironment = Clock with Persistence

    type AppTask[A] = TaskR[AppEnvironment, A]

    val program: ZIO[Main.Environment, Throwable, Unit] = for {
      config <- config.load.provide(Configuration.Live)

      blockingEC <- blocking.blockingExecutor.map(_.asEC).provide(Blocking.Live)

      transactionR = Persistence.mkTransactor(
        config.dbConfig,
        Platform.executor.asEC,
        blockingEC
      )

      httpApp = Router[AppTask]("/users" -> Api(s"${config.apiConfig.endpoint}/users").route).orNotFound

      server = ZIO.runtime[AppEnvironment].flatMap { implicit rts =>
        db.createTable *>
          BlazeServerBuilder[AppTask]
            .bindHttp(config.apiConfig.port, "0.0.0.0")
            .withHttpApp(CORS(httpApp))
            .serve
            .compile[AppTask, AppTask, ExitCode]
            .drain

      }

      program <- transactionR.use { transactor =>
        server.provideSome[Environment] { _ =>
          new Clock.Live with Persistence.Live {
            override protected def tnx: doobie.Transactor[Task] = transactor
          }
        }

      }

    } yield program


    program.fold(_ => 1, _ => 0)

  }


}
