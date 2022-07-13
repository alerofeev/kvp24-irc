package com.github.ghilsidoll.irc.event

sealed trait SessionEvent
final case class MessagePosted(message: String, from: String) extends SessionEvent