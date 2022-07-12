package com.github.ghilsidoll.irc.actor

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.github.ghilsidoll.irc.event.{MessagePosted, SessionEvent}

object UserActor {

  val user: Behavior[SessionEvent] =
    Behaviors.receive { (context, message) => {
      message match {
        case MessagePosted(msg) =>
          context.log.info(s"[USER ACTOR] test message: $msg")
          Behaviors.same
      }
    }
  }
}
