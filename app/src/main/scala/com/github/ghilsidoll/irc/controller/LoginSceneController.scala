package com.github.ghilsidoll.irc.controller

import javafx.application.Platform
import javafx.event.Event
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Node, Parent, Scene}
import javafx.scene.control.{Button, TextField}
import javafx.scene.layout.BorderPane
import javafx.stage.{Screen, Stage}

import java.util.Objects

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

    error
  }

  private def loadMainScene(event: Event): Unit = {
    val screenBounds = Screen.getPrimary.getVisualBounds
    val window = event.getSource.asInstanceOf[Node].getScene.getWindow
    val stage = window.asInstanceOf[Stage]
    val root: Parent = FXMLLoader.load(Objects.requireNonNull(getClass.getResource("/com/github/ghilsidoll/" +
      "irc/view/mainScene.fxml")))

    stage.setScene(new Scene(root, 1000, 760))

    window.setX((screenBounds.getWidth - stage.getWidth) / 2)
    window.setY((screenBounds.getHeight - stage.getHeight) / 2)
  }

  def initialize(): Unit = {
    Platform.runLater(() => mainScene.requestFocus())
    mainScene.setOnMouseClicked(_ => mainScene.requestFocus())

    loginButton.setOnAction(event => {

      if (!isLoginValid(loginTextField.getText)) {
        loadMainScene(event)
      } else {
       // TODO: add error message
      }
    })
  }
}
