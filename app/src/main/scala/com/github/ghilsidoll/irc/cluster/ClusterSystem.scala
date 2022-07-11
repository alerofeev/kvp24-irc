package com.github.ghilsidoll.irc.cluster

import akka.actor.typed.ActorSystem
import com.github.ghilsidoll.irc.actor.RootBehavior.Command

class ClusterSystem {
  private final var system: ActorSystem[Command] = _


}
