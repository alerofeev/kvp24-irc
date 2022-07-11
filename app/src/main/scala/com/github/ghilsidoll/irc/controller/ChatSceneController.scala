package com.github.ghilsidoll.irc.controller

import com.github.ghilsidoll.irc.actor.RootBehavior
import akka.actor.typed.ActorSystem
import com.github.ghilsidoll.irc.actor.RootBehavior.{Command, PostMessage}
import com.typesafe.config.ConfigFactory
import javafx.application.Platform
import javafx.event.Event
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, TextField}
import javafx.scene.{Node, Parent, Scene}
import javafx.scene.layout.{BorderPane, VBox}
import javafx.stage.{Screen, Stage}

import java.util.Objects

class ChatSceneController {

  @FXML
  protected var mainScene: BorderPane = _

  @FXML
  protected var chatPreviewContainer: VBox = _

  @FXML
  protected var sendMessageButton: Button = _

  @FXML
  protected var messageTextField: TextField = _

  private final var system: ActorSystem[Command] = _

  def startup(port: Int, systemName: String): Unit = {
    val config = ConfigFactory.parseString(s"""akka.remote.artery.canonical.port=$port
      akka.cluster.seed-nodes=["akka://$systemName@127.0.0.1:25251",
      "akka://$systemName@127.0.0.1:25252"]""".stripMargin)
      .withFallback(ConfigFactory.load("application.conf"))

    system = ActorSystem(RootBehavior.behavior, systemName, config)
  }

  def loadScene(event: Event): Unit = {
    val screenBounds = Screen.getPrimary.getVisualBounds
    val window = event.getSource.asInstanceOf[Node].getScene.getWindow
    val stage = window.asInstanceOf[Stage]
    val root: Parent = FXMLLoader.load(Objects.requireNonNull(getClass.getResource("/view/chatScene.fxml")))

    stage.setScene(new Scene(root, 1000, 760))

    window.setX((screenBounds.getWidth - stage.getWidth) / 2)
    window.setY((screenBounds.getHeight - stage.getHeight) / 2)
  }

  def initialize(): Unit = {
    Platform.runLater(() => mainScene.requestFocus())
    mainScene.setOnMouseClicked(_ => mainScene.requestFocus())
    
  }
}