package com.diogosantos.http

import com.diogosantos.db.{Persistence, _}
import com.diogosantos.model.{User, UserId}
import io.circe.generic.auto._
import io.circe.{Decoder, Encoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes}
import zio.TaskR
import zio.interop.catz._


final class Api[R <: Persistence](rootUrl: String) {

  type UserTask[A] = TaskR[R, A]

  implicit def circeJsonDecoder[A](implicit decoder: Decoder[A]): EntityDecoder[UserTask, A] = jsonOf[UserTask, A]

  implicit def circeJsonEncoder[A](implicit decoder: Encoder[A]): EntityEncoder[UserTask, A] = jsonEncoderOf[UserTask, A]

  val dsl: Http4sDsl[UserTask] = Http4sDsl[UserTask]

  import dsl._

  def routes: HttpRoutes[UserTask] = HttpRoutes.of[UserTask] {
    case GET -> Root / IntVar(id) => get(UserId(id)).foldM(_ => NotFound(), Ok(_))
    case request@POST -> Root => request.decode[User] { user =>
      Created(create(user))
    }
    case DELETE -> Root / IntVar(id) =>
      val userId = UserId(id)
      (get(userId) *> delete(userId)).foldM(_ => NotFound(), Ok(_))
  }

}
