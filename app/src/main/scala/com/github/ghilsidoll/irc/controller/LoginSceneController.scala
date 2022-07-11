package com.github.ghilsidoll.irc.controller

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField}
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

    // TODO add validation by symbols

    // TODO add validation by uniqueness

    !error
  }

  def initialize(): Unit = {
    Platform.runLater(() => mainScene.requestFocus())
    mainScene.setOnMouseClicked(_ => mainScene.requestFocus())

    loginButton.setOnAction(event => {
      val loginText = loginTextField.getText

      if (isLoginValid(loginText)) {
        val chatSceneController = new ChatSceneController()
        chatSceneController.loadScene(event)
        chatSceneController.startup(25251, loginText)

        // TODO add progress bar

      } else {

       // TODO add error message

      }
    })
  }
}
