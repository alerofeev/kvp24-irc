package com.github.ghilsidoll.irc.actor

import akka.actor.typed.Behavior
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.pubsub.Topic.{Publish, Subscribe}
import akka.actor.typed.scaladsl.Behaviors
import com.github.ghilsidoll.irc.controller.ChatSceneController
import com.github.ghilsidoll.irc.event.{LoginPosted, MessagePosted, SessionEvent}

object RootBehavior {

  sealed trait Command
  final case class PostMessage(message: String, to: String) extends Command
  final case class PostLogin() extends Command

  def apply(controller: ChatSceneController): Behavior[Command] = {
    Behaviors.setup { context =>
      val user = context.spawn(UserActor(controller), controller.getLogin)
      val topic = context.spawn(Topic[SessionEvent]("main-topic"), "MainTopic")

      topic ! Subscribe(user)

      Behaviors.receiveMessage {
        case PostMessage(message, to) =>
          topic ! Publish(MessagePosted(message, user.path.name, to))
          Behaviors.same
        case PostLogin() =>
          topic ! Publish(LoginPosted(user.path.name))
          Behaviors.same
      }
    }
  }
}
