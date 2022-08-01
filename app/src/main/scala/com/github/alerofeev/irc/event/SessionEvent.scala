package com.github.alerofeev.irc.event

sealed trait SessionEvent
final case class MessagePosted(message: String, from: String, to: String) extends SessionEvent
final case class LoginPosted(login: String) extends SessionEvent
