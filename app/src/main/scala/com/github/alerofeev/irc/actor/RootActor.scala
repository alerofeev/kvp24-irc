package com.github.alerofeev.irc.actor

import akka.actor.typed.Behavior
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.pubsub.Topic.Publish
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.ClusterEvent.{MemberEvent, MemberUp}
import akka.cluster.typed.Cluster
import com.github.alerofeev.irc.controller.MainSceneController
import com.github.alerofeev.irc.event.{LoginPosted, MessagePosted, SessionEvent}

object RootActor {

  sealed trait Command
  final case class PostMessage(message: String, to: String) extends Command
  private final case class MemberChange(event: MemberEvent) extends Command

  def apply(controller: MainSceneController): Behavior[Command] = {
    Behaviors.setup { context =>
      val user = context.spawn(UserActor(controller), controller.getLogin)

      val topic = context.spawn(Topic[SessionEvent]("main-topic"), "MainTopic")
      topic ! akka.actor.typed.pubsub.Topic.Subscribe(user)

      val cluster = Cluster(context.system)
      cluster.subscriptions ! akka.cluster.typed.Subscribe(context.messageAdapter(MemberChange),
        classOf[MemberEvent])

      Behaviors.receiveMessage {
        case PostMessage(message, to) =>
          topic ! Publish(MessagePosted(message, user.path.name, to))
          Behaviors.same
        case MemberChange(event) =>
          event match {
            case MemberUp(_) =>

              val thread = new Thread {
                override def run(): Unit = {
                  Thread.sleep(3000)
                  topic ! Publish(LoginPosted(user.path.name))
                }
              }
              thread.start()

            case _: MemberEvent => // ignored
          }
          Behaviors.same
      }
    }
  }
}
