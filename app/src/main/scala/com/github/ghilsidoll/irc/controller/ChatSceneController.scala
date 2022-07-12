package com.github.ghilsidoll.irc.controller

import com.github.ghilsidoll.irc.actor.RootBehavior
import com.github.ghilsidoll.irc.actor.RootBehavior.userActor
import com.github.ghilsidoll.irc.event.MessagePosted
import com.typesafe.config.ConfigFactory
import javafx.application.Platform
import javafx.event.{Event, EventHandler}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Label, ScrollPane, TextField}
import javafx.scene.image.ImageView
import javafx.scene.{Node, Parent, Scene}
import javafx.scene.layout.{BorderPane, VBox}
import javafx.stage.{Screen, Stage}
import akka.actor.typed.ActorSystem
import javafx.scene.input.{KeyCode, KeyEvent}

import java.util.Objects

class ChatSceneController {

  private var username: String = _

  @FXML
  protected var chatContainer: VBox = _

  @FXML
  protected var chatPreviewContainer: VBox = _

  @FXML
  protected var chatScrollPane: ScrollPane = _

  @FXML
  protected var loginLabel: Label = _

  @FXML
  protected var mainScene: BorderPane = _

  @FXML
  protected var messageTextField: TextField = _

  @FXML
  protected var sendMessageButton: ImageView = _

  def startup(port: Int, systemName: String): Unit = {
    val config = ConfigFactory.parseString(s"""akka.remote.artery.canonical.port=$port
      akka.cluster.seed-nodes=["akka://$systemName@127.0.0.1:25251",
      "akka://$systemName@127.0.0.1:25252"]""".stripMargin)
      .withFallback(ConfigFactory.load("application.conf"))

    ActorSystem(RootBehavior.behavior, systemName, config)
  }

  def loadScene(event: Event): Unit = {
    val screenBounds = Screen.getPrimary.getVisualBounds
    val window = event.getSource.asInstanceOf[Node].getScene.getWindow
    val stage = window.asInstanceOf[Stage]
    val root: Parent = FXMLLoader.load(Objects.requireNonNull(getClass.getResource("/view/chatScene.fxml")))

    window.setX((screenBounds.getWidth - 1000) / 2)
    window.setY((screenBounds.getHeight - 760) / 2)

    stage.setScene(new Scene(root, 1000, 760))
  }

  private def displayMessage(login: String, content: String): Unit = {
    val loader:FXMLLoader = new FXMLLoader(Objects.requireNonNull(getClass.getResource(
      "/view/template/messageBoxScene.fxml")))
    val node: VBox = loader.load()
    loader.getController.asInstanceOf[MessageBoxSceneController].setContent(login, content)
    chatContainer.getChildren.add(node)
  }

  private def sendMessage(): Unit = {

    if (messageTextField.getText.nonEmpty) {
      userActor ! MessagePosted(messageTextField.getText)
      displayMessage(username, messageTextField.getText)
    }

    chatScrollPane.setVvalue(1d)
    messageTextField.setText("")
    messageTextField.requestFocus()
  }

  def initialize(): Unit = {
    username = userActor.path.name

    Platform.runLater(() => mainScene.requestFocus())
    mainScene.setOnMouseClicked(_ => mainScene.requestFocus())

    messageTextField.setOnKeyPressed(new EventHandler[KeyEvent]() {
      override def handle(event: KeyEvent): Unit = {
        if (event.getCode == KeyCode.ENTER) {
          sendMessage()
        }
      }
    })

    loginLabel.setText(username)

    sendMessageButton.setOnMouseClicked(_ => {
      sendMessage()
    })
  }
}
