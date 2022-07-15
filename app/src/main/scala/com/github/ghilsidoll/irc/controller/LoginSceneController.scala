package com.github.ghilsidoll.irc.controller

import javafx.application.Platform
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.{Event, EventHandler}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, TextField}
import javafx.scene.input.{KeyCode, KeyEvent}
import javafx.scene.layout.BorderPane

import java.util.Objects

class LoginSceneController {

  @FXML
  protected var mainScene: BorderPane = _

  @FXML
  protected var loginTextField: TextField = _

  @FXML
  protected var portTextField: TextField = _

  @FXML
  protected var loginButton: Button = _

  def isLoginValid(login: String): Boolean = {
    var error = false

    if (login.length > 32 || login.length < 3) {
      error = true
    }

    // TODO: add validation by symbols

    // TODO: add validation by uniqueness

    !error
  }

  def isPortValid(port: String): Boolean = {
    var error = false

    if (port.isBlank) {
      error = true
    }

    !error
  }

  def login(event: Event): Unit = {
    val login = loginTextField.getText
    val port = portTextField.getText

    if (isLoginValid(login) && isPortValid(port)) {
      val chatSceneController = new ChatSceneController(login)

      chatSceneController.startup(port.toInt, chatSceneController)

      val loader: FXMLLoader = new FXMLLoader(Objects.requireNonNull(
        getClass.getResource("/view/chatScene.fxml")))

      loader.setController(chatSceneController)

      chatSceneController.loadScene(event, loader)

      // TODO: add progress bar

    } else {

      // TODO: add error message

    }
  }

  def initialize(): Unit = {
    Platform.runLater(() => mainScene.requestFocus())
    mainScene.setOnMouseClicked(_ => mainScene.requestFocus())

    loginTextField.setOnKeyPressed(new EventHandler[KeyEvent]() {
      override def handle(event: KeyEvent): Unit = {
        if (event.getCode == KeyCode.ENTER) {
          login(event)
        }
      }
    })

    portTextField.textProperty().addListener(new ChangeListener[String]()  {
      override def changed(observable: ObservableValue[_ <: String], oldValue: String, newValue: String): Unit = {
        if (!newValue.matches("\\d*")) {
          portTextField.setText(newValue.replaceAll("\\D", ""))
        }
        if (newValue.length > 5) {
          portTextField.setText(newValue.substring(0, 5))
        }
      }
    })

    loginButton.setOnAction(event => {
      login(event)
    })
  }
}
