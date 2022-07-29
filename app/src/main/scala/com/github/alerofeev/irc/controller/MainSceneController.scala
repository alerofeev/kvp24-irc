package com.github.alerofeev.irc.controller

import com.github.alerofeev.irc.actor.RootActor
import com.github.alerofeev.irc.actor.RootActor.PostMessage

import akka.actor.typed.ActorSystem

import javafx.application.Platform
import javafx.event.Event
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, Label, ScrollPane, TextField}
import javafx.scene.{Node, Scene}
import javafx.scene.layout.{BorderPane, VBox}
import javafx.stage.{Screen, Stage, WindowEvent}

import java.util.Objects

class MainSceneController(private val login: String) {

  private final var actorSystem: ActorSystem[RootActor.Command] = _

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
  protected var recipientTextField: TextField = _

  @FXML
  protected var sendMessageButton: Button = _

  def setActorSystem(actorSystem: ActorSystem[RootActor.Command]): Unit = this.actorSystem = actorSystem

  def getLogin: String = login

  def loadScene(event: Event, loader: FXMLLoader): Unit = {
    val screenBounds = Screen.getPrimary.getVisualBounds
    val window = event.getSource.asInstanceOf[Node].getScene.getWindow
    val stage = window.asInstanceOf[Stage]

    window.setX((screenBounds.getWidth - 628) / 2)
    window.setY((screenBounds.getHeight - 660) / 2)

    stage.setScene(new Scene(loader.load(), 628, 660))

    stage.setOnCloseRequest((_: WindowEvent) => {
      actorSystem.terminate()
      Platform.exit()
      System.exit(0)
    })
  }

  def displayMessage(login: String, content: String, modifier: Int = -1): Unit = {
    Platform.runLater(() => {
      val loader: FXMLLoader = new FXMLLoader(Objects.requireNonNull(getClass.getResource(
        "/view/template/messageBoxScene.fxml")))
      val node: VBox = loader.load()
      loader.getController.asInstanceOf[MessageBoxSceneController].setContent(login, content, modifier)
      chatContainer.getChildren.add(node)

      chatScrollPane.setVvalue(1d)
      messageTextField.setText("")
      messageTextField.requestFocus()
    })
  }

  private def sendMessage(): Unit = {
    if (messageTextField.getText.nonEmpty) {
      val recipient: String = recipientTextField.getText
      actorSystem ! PostMessage(messageTextField.getText,
        if (recipient == null || recipient.isBlank) "" else recipient)
    }
  }

  def initialize(): Unit = {
    Platform.runLater(() => mainScene.requestFocus())

    mainScene.setOnMouseClicked(_ => mainScene.requestFocus())

    loginLabel.setText(login)

    sendMessageButton.setOnAction(_ => {
      sendMessage()
    })
  }
}
