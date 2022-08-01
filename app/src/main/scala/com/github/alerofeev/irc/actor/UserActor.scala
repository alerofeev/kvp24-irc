package com.github.alerofeev.irc.actor

import com.github.alerofeev.irc.controller.MainSceneController
import com.github.alerofeev.irc.event.{LoginPosted, MessagePosted, SessionEvent}
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object UserActor {
  def apply(controller: MainSceneController): Behavior[SessionEvent] =
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
          controller.addRecipient(login)
        }
        Behaviors.same
    }
  }
}
