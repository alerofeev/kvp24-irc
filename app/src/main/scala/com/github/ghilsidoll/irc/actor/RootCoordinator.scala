package com.github.ghilsidoll.irc.actor

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.runtime.Nothing$

object RootCoordinator {
  var user: ActorRef[UserActor.Command] = _

  def sendMessage(message: String): Unit = {
    user ! UserActor.SendTestMessage(message)
  }

  val coordinator: Behavior[Nothing] =  Behaviors.setup { context =>
    user = context.spawn(UserActor(), "user")

    Behaviors.same
  }
}
