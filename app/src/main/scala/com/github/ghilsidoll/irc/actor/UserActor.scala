package com.github.ghilsidoll.irc.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.github.ghilsidoll.irc.controller.ChatSceneController
import com.github.ghilsidoll.irc.event.{MessagePosted, SessionEvent}

object UserActor {
  def apply(controller: ChatSceneController): Behavior[SessionEvent] = Behaviors.setup { context =>

    Behaviors.receiveMessage {
      case MessagePosted(message, from) =>
        controller.displayMessage(from, message, from == context.self.path.name)
        context.log.info(s"[MESSAGE] $from: $message")
        Behaviors.same
    }
  }
}
