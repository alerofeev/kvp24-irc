package com.github.ghilsidoll.irc.controller

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.javadsl.Behaviors
import com.typesafe.config.ConfigFactory
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.layout.{BorderPane, VBox}

class MainSceneController {

  @FXML
  protected var mainScene: BorderPane = _

  @FXML
  protected var chatPreviewContainer: VBox = _


  object RootBehaviour {
    def apply(): Behavior[Nothing] = Behaviors.setup[Nothing] { context =>

      Behaviors.empty
    }
  }

  def startup(port: Int): Unit = {
    val config = ConfigFactory.parseString(s"""akka.remote.artery.canonical.port=${port}""").withFallback(
      ConfigFactory.load("application.config"))

    ActorSystem[Nothing](RootBehaviour(), "ClusterSystem", config)
  }

  def initialize(): Unit = {
    Platform.runLater(() => mainScene.requestFocus())
    mainScene.setOnMouseClicked(_ => mainScene.requestFocus())
  }
}
