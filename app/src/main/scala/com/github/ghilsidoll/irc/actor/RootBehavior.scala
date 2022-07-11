package com.github.ghilsidoll.irc.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object RootBehavior {

  sealed trait Command
  final case class CreateUser(username: String) extends Command
  final case class PostMessage(message: String) extends Command

  val behavior: Behavior[Command] = Behaviors.setup { context =>

    val userActor = context.spawn(UserActor.user, context.system.name)

    Behaviors.receiveMessage {
      case CreateUser(username) =>
        context.log.info(s"[ROOT BEHAVIOR] User created with name $username")
        Behaviors.same
      case PostMessage(msg) =>
        context.log.info(s"[ROOT BEHAVIOR] User posted message: $msg")
        Behaviors.same
    }
  }
}
