package com.github.ghilsidoll.irc.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object UserActor {
  var userName: String = _

  sealed trait Command
  case class SendTestMessage(message: String) extends Command

  def apply(): Behavior[Command] = Behaviors.setup[Command] { context =>

    Behaviors.receiveMessage {
      case SendTestMessage(message) =>
        context.log.info(s"[USER] Message received. Content: $message")

        Behaviors.same
    }
  }
}
