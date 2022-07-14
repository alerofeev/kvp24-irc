package com.github.ghilsidoll.irc.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.github.ghilsidoll.irc.controller.ChatSceneController
import com.github.ghilsidoll.irc.event.{LoginPosted, MessagePosted, SessionEvent}

object UserActor {
  def apply(controller: ChatSceneController): Behavior[SessionEvent] =
    Behaviors.setup { context =>

    val selfName: String = context.self.path.name

    Behaviors.receiveMessage {
      case MessagePosted(message, from, to) =>
        if (from == selfName) {
          controller.displayMessage(from, message, 0)
        } else if (to == selfName) {
          controller.displayMessage(from, message, 1)
        } else if (to == "") {
          controller.displayMessage(from, message)
        }
        Behaviors.same
      case LoginPosted(login) =>
        if (login != selfName) {
          context.log.info(s"[____USER____] Login received: $login")
          controller.addRecipient(login)
        } else {
          context.log.info(s"[____USER____] Login posted by $login")
        }
        Behaviors.same
    }
  }
}
