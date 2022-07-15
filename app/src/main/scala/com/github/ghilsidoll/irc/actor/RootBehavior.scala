package com.github.ghilsidoll.irc.actor

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.pubsub.Topic.Publish
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.ClusterEvent.{MemberEvent, MemberUp, ReachabilityEvent}
import akka.cluster.typed.Cluster
import com.github.ghilsidoll.irc.controller.ChatSceneController
import com.github.ghilsidoll.irc.event.{LoginPosted, MessagePosted, SessionEvent}

object RootBehavior {

  sealed trait Command
  final case class PostMessage(message: String, to: String) extends Command
  private final case class MemberChange(memberEvent: MemberEvent) extends Command
  private final case class ReachabilityChange(reachabilityEvent: ReachabilityEvent) extends Command

  def apply(controller: ChatSceneController): Behavior[Command] = {
    Behaviors.setup { context =>
      implicit val cluster: Cluster = Cluster(context.system)
      val user = context.spawn(UserActor(controller), controller.getLogin)
      val topic = context.spawn(Topic[SessionEvent]("main-topic"), "MainTopic")

      topic ! akka.actor.typed.pubsub.Topic.Subscribe(user)

      val memberEventAdapter: ActorRef[MemberEvent] = context.messageAdapter(MemberChange)
      cluster.subscriptions ! akka.cluster.typed.Subscribe(memberEventAdapter, classOf[MemberEvent])

      val reachabilityAdapter = context.messageAdapter(ReachabilityChange)
      cluster.subscriptions ! akka.cluster.typed.Subscribe(reachabilityAdapter, classOf[ReachabilityEvent])

      Behaviors.receiveMessage {
        case PostMessage(message, to) =>
          topic ! Publish(MessagePosted(message, user.path.name, to))
          Behaviors.same
        case MemberChange(memberEvent) =>
          memberEvent match {
            case MemberUp(_) =>
              topic ! Publish(LoginPosted(user.path.name))
            case _: MemberEvent => // ignored
          }
          Behaviors.same

        case ReachabilityChange(reachabilityEvent) =>
          reachabilityEvent match {
            case _: ReachabilityEvent => // ignored
          }
          Behaviors.same
      }
    }
  }
}
