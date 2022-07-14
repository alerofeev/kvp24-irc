package com.github.ghilsidoll.irc.event

import akka.cluster.ClusterEvent.MemberEvent

sealed trait SessionEvent
final case class MessagePosted(message: String, from: String, to: String) extends SessionEvent
final case class LoginPosted(login: String) extends SessionEvent