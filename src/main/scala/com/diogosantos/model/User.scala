package com.diogosantos.model

final case class UserId(value: Int)

final case class User(id: UserId, name: String)

final case class UserNotFound(id: UserId) extends Exception
