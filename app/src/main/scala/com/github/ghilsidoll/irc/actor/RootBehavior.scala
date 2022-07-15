package com.github.ghilsidoll.irc.actor

import akka.actor.typed.Behavior
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.pubsub.Topic.Publish
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.typed.Cluster
import com.github.ghilsidoll.irc.controller.ChatSceneController
import com.github.ghilsidoll.irc.event.{MessagePosted, SessionEvent}

object RootBehavior {

  sealed trait Command
  final case class PostMessage(message: String, to: String) extends Command

  def apply(controller: ChatSceneController): Behavior[Command] = {
    Behaviors.setup { context =>
      implicit val cluster: Cluster = Cluster(context.system)
      val user = context.spawn(UserActor(controller), controller.getLogin)
      val topic = context.spawn(Topic[SessionEvent]("main-topic"), "MainTopic")

      topic ! akka.actor.typed.pubsub.Topic.Subscribe(user)

      Behaviors.receiveMessage {
        case PostMessage(message, to) =>
          topic ! Publish(MessagePosted(message, user.path.name, to))
          Behaviors.same
      }
    }
  }
}
