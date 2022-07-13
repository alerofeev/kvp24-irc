package com.github.ghilsidoll.irc.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.github.ghilsidoll.irc.event.{MessagePosted, SessionEvent}

object UserActor {
  def apply(): Behavior[SessionEvent] = Behaviors.setup { context =>

    Behaviors.receiveMessage {
      case MessagePosted(message, from) =>
        context.log.info(s"[MESSAGE] $from: $message")
        Behaviors.same
    }
  }
}
