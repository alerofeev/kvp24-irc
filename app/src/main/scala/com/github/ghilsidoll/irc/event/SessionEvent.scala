package com.github.ghilsidoll.irc.event

sealed trait SessionEvent
  final case class SessionGranted() extends SessionEvent

  final case class SessionDenied() extends SessionEvent

  final case class MessagePosted(message: String) extends SessionEvent

  final case class MessageReceived(userName: String, message: String) extends SessionEvent


