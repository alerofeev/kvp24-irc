package com.github.alerofeev.irc.actor

import com.github.alerofeev.irc.controller.MainSceneController
import com.github.alerofeev.irc.event.{LoginPosted, LoginRemoved, MessagePosted, SessionEvent}
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.github.alerofeev.irc.modifier.Modifiers

object UserActor {
  def apply(controller: MainSceneController): Behavior[SessionEvent] =
    Behaviors.setup { context =>

    val selfName: String = context.self.path.name

    Behaviors.receiveMessage {

      case MessagePosted(message, from, _) if from == selfName =>
        controller.displayMessage(from, message, Modifiers.YOURS)
        Behaviors.same
      case MessagePosted(message, from, to) if to == selfName =>
        controller.displayMessage(from, message, Modifiers.PRIVATE)
        Behaviors.same
      case MessagePosted(message, from, to) if to.isEmpty =>
        controller.displayMessage(from, message)
        Behaviors.same
      case MessagePosted(_, _, _) => Behaviors.same
      case LoginPosted(login) =>
        if (login != selfName) {
          controller.addRecipient(login)
        }
        Behaviors.same
      case LoginRemoved(login) =>
        controller.removeRecipient(login)
        Behaviors.same
    }
  }
}
