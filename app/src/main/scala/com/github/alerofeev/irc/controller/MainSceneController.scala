package com.github.alerofeev.irc.controller

import com.github.alerofeev.irc.actor.RootActor
import com.github.alerofeev.irc.actor.RootActor.{Logout, PostMessage}
import com.github.alerofeev.irc.modifier.Modifiers
import com.github.alerofeev.irc.modifier.Modifiers.Modifiers

import akka.actor.typed.ActorSystem

import javafx.application.Platform
import javafx.event.Event
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, ChoiceBox, Label, ScrollPane, TextField}
import javafx.scene.{Node, Scene}
import javafx.scene.layout.{BorderPane, VBox}
import javafx.stage.{Screen, Stage, WindowEvent}

import java.util.Objects

class MainSceneController(private val login: String) {

  private final var actorSystem: ActorSystem[RootActor.Command] = _

  private var recipientSet: Set[String] = Set("Group")

  @FXML
  protected var chatContainer: VBox = _

  @FXML
  protected var chatScrollPane: ScrollPane = _

  @FXML
  protected var loginLabel: Label = _

  @FXML
  protected var mainScene: BorderPane = _

  @FXML
  protected var messageTextField: TextField = _

  @FXML
  protected var recipientChoiceBox: ChoiceBox[String] = _

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
      actorSystem ! Logout(login)
      actorSystem.terminate()
    })
  }

  def displayMessage(login: String, content: String, modifier: Modifiers = Modifiers.PUBLIC): Unit = {
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

  def displayRecipientList(): Unit = {
    val lastValue: String = recipientChoiceBox.getValue

    Platform.runLater(() => {
      recipientChoiceBox.getItems.clear()
      recipientSet.foreach(value => {
        recipientChoiceBox.getItems.add(value)
      })
      recipientChoiceBox.setValue(lastValue)
    })
  }


  def addRecipient(recipientLogin: String): Unit = {
    recipientSet += recipientLogin
    displayRecipientList()
  }

  def removeRecipient(recipientLogin: String): Unit = {
    recipientSet -= recipientLogin
    displayRecipientList()
  }

  def initialize(): Unit = {
    Platform.runLater(() => mainScene.requestFocus())

    mainScene.setOnMouseClicked(_ => mainScene.requestFocus())

    loginLabel.setText(login)

    sendMessageButton.setOnAction(_ => {
      if (messageTextField.getText.nonEmpty) {
        val recipient: String = recipientChoiceBox.getValue
        actorSystem ! PostMessage(messageTextField.getText,
          if (recipient == null || recipient == "Group") "" else recipient)
      }
    })
  }
}
