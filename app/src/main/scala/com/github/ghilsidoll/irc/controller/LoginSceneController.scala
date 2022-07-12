package com.github.ghilsidoll.irc.controller

import javafx.application.Platform
import javafx.event.{Event, EventHandler}
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField}
import javafx.scene.input.{KeyCode, KeyEvent}
import javafx.scene.layout.BorderPane

class LoginSceneController {

  @FXML
  protected var mainScene: BorderPane = _

  @FXML
  protected var loginTextField: TextField = _

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

  def login(event: Event): Unit = {
    val loginText = loginTextField.getText

    if (isLoginValid(loginText)) {
      val chatSceneController = new ChatSceneController()
      chatSceneController.startup(25251, loginText)
      chatSceneController.loadScene(event)

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

    loginButton.setOnAction(event => {
      login(event)
    })
  }
}
