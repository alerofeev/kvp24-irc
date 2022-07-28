package com.github.alerofeev.irc.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.github.alerofeev.irc.controller.ChatSceneController
import com.github.alerofeev.irc.event.{MessagePosted, SessionEvent}

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
    }
  }
}
