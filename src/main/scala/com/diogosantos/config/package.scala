package com.diogosantos

import zio.TaskR

package object config extends Configuration.Service[Configuration] {
  val load: TaskR[Configuration, Config] = TaskR.accessM(_.config.load)
}
