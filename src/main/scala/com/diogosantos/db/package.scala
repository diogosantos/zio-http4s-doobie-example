package com.diogosantos
import com.diogosantos.model.{User, UserId}
import zio.{TaskR, ZIO}

package object db extends Persistence.Service[Persistence] {
  override val createTable: TaskR[Persistence, Unit] = ZIO.accessM(_.userPersistence.createTable)

  override def get(id: UserId): TaskR[Persistence, User] = ZIO.accessM(_.userPersistence.get(id))

  override def create(user: User): TaskR[Persistence, User] = ZIO.accessM(_.userPersistence.create(user))

  override def delete(id: UserId): TaskR[Persistence, Unit] = ZIO.accessM(_.userPersistence.delete(id))
}
