package com.github.ghilsidoll.irc.actor

import akka.NotUsed
import akka.actor.typed.{ActorRef, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import com.github.ghilsidoll.irc.event.SessionEvent

object RootBehavior {

  final var userActor: ActorRef[SessionEvent] = _

  val behavior: Behavior[NotUsed] = Behaviors.setup { context =>
    userActor = context.spawn(UserActor.user, context.system.name)



    Behaviors.receiveSignal {
      case (_, Terminated(_)) => Behaviors.stopped
    }
  }
}
