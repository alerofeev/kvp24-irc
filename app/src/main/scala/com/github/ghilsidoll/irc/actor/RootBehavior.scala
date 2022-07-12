package com.github.ghilsidoll.irc.actor

import akka.NotUsed
import akka.actor.typed.{ActorRef, Behavior, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import com.github.ghilsidoll.irc.event.SessionEvent

object RootBehavior {

  private final var userActor: ActorRef[SessionEvent] = _

  private final var userLogin: String = _

  def setUserLogin(login: String): Unit = {
    userLogin = login
  }

  def getUserLogin: String = {
    userLogin
  }

  def getUserActor: ActorRef[SessionEvent] = {
    userActor
  }

  val behavior: Behavior[NotUsed] = Behaviors.setup { context =>
    userActor = context.spawn(UserActor.user, userLogin)

    Behaviors.receiveSignal {
      case (_, Terminated(_)) => Behaviors.stopped
    }
  }
}
