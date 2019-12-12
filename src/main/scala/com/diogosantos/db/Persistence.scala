package com.diogosantos.db

import com.diogosantos.model.{User, UserId, UserNotFound}
import doobie.implicits._
import doobie.{Query0, Transactor, Update0}
import zio.interop.catz._
import zio.{RIO, Task}


trait Persistence extends Serializable {
  val userPersistence: Persistence.Service[Any]
}

object Persistence {

  trait Service[R] {
    val createTable: RIO[R, Unit]

    def get(id: UserId): RIO[R, User]

    def create(user: User): RIO[R, User]

    def delete(id: UserId): RIO[R, Unit]
  }

  trait Live extends Persistence {

    protected def tnx: Transactor[Task]

    override val userPersistence: Service[Any] = new Service[Any] {

      override val createTable: Task[Unit] =
        SQL.createTable.run.transact(tnx)
          .foldM(err => Task.fail(err), _ => Task.succeed(()))

      override def get(id: UserId): Task[User] =
        SQL
          .get(id).option.transact(tnx)
          .foldM(
            Task.fail,
            maybeUser => Task.require(UserNotFound(id))(Task.succeed(maybeUser))
          )

      override def create(user: User): Task[User] =
        SQL
          .create(user).run.transact(tnx)
          .foldM(
            err => Task.fail(err),
            _ => Task.succeed(user)
          )


      override def delete(id: UserId): Task[Unit] =
        SQL.delete(id).run.transact(tnx).unit.orDie

    }

    object SQL {
      def createTable: Update0 =
        sql"""CREATE TABLE IF NOT EXISTS Users (id int PRIMARY KEY, name varchar)""".update

      def get(id: UserId): Query0[User] =
        sql"""SELECT * FROM USERS WHERE ID = ${id.value} """.query[User]

      def create(user: User): Update0 =
        sql"""INSERT INTO USERS (ID, NAME) VALUES (${user.id}, ${user.name})""".update

      def delete(id: UserId): Update0 =
        sql"""DELETE FROM USERS WHERE ID = ${id.value}""".update
    }

  }

}
