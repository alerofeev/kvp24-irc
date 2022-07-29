package com.github.alerofeev.irc.system

import com.github.alerofeev.irc.actor.RootActor
import com.github.alerofeev.irc.controller.MainSceneController

import akka.actor.typed.ActorSystem
import com.typesafe.config.ConfigFactory

object ChatActorSystem {

  def startup(address: String, port: String, controller: MainSceneController): ActorSystem[RootActor.Command] = {
    val config = ConfigFactory.parseString(
      s"""akka.remote.artery.canonical.port=$port
         |akka.remote.artery.canonical.hostname=$address
         |akka.cluster.seed-nodes=["akka://chat@$address:25251"]"""
        .stripMargin)
      .withFallback(ConfigFactory.load("application.conf"))

    ActorSystem(RootActor(controller), "chat", config)
  }
}
