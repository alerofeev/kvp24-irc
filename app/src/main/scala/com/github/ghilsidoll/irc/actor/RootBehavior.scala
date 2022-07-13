package com.github.ghilsidoll.irc.actor

import akka.actor.typed.Behavior
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.pubsub.Topic.{Publish, Subscribe}
import akka.actor.typed.scaladsl.Behaviors
import com.github.ghilsidoll.irc.controller.ChatSceneController
import com.github.ghilsidoll.irc.event.{MessagePosted, SessionEvent}

object RootBehavior {

  sealed trait Command
  final case class PostMessage(message: String, to: String) extends Command

  def apply(controller: ChatSceneController): Behavior[Command] = {
    Behaviors.setup { context =>
      val user = context.spawn(UserActor(controller), controller.getLogin)
      val topic = context.spawn(Topic[SessionEvent]("test-topic"), "TestTopic")

      topic ! Subscribe(user)

      Behaviors.receiveMessage {
        case PostMessage(message, to) =>
          topic ! Publish(MessagePosted(message, user.path.name, to))
          Behaviors.same
      }
    }
  }
}
