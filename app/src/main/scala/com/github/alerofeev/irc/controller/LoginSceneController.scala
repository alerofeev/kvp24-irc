package com.github.alerofeev.irc.controller

import javafx.application.Platform
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.event.Event
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, TextField}
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
  protected var addressTextField: TextField = _

  @FXML
  protected var loginButton: Button = _

  def isLoginValid(login: String): Boolean = login.length >= 3

  def isPortValid(port: String): Boolean = !port.isBlank

  def isAddressValid(address: String): Boolean =
    address.matches("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b")

  def login(event: Event): Unit = {
    val login = loginTextField.getText
    val address = addressTextField.getText
    val port = portTextField.getText

    if (isLoginValid(login) && isAddressValid(address) && isPortValid(port)) {
      val chatSceneController = new ChatSceneController(login)

      chatSceneController.startup(address, port, chatSceneController)

      val loader: FXMLLoader = new FXMLLoader(Objects.requireNonNull(
        getClass.getResource("/view/chatScene.fxml")))

      loader.setController(chatSceneController)

      chatSceneController.loadScene(event, loader)

      // TODO: add progress bar

    }

    // TODO: add error message

  }

  def initialize(): Unit = {
    Platform.runLater(() => mainScene.requestFocus())
    mainScene.setOnMouseClicked(_ => mainScene.requestFocus())

    loginTextField.textProperty().addListener(new ChangeListener[String]()  {
      override def changed(observable: ObservableValue[_ <: String], oldValue: String, newValue: String): Unit = {

        if (!newValue.matches("[\\\\da-zA-Z]")) {
          loginTextField.setText(newValue.replaceAll("[^\\a-zA-Z]", ""))
        }

        if (newValue.length > 16) {
          loginTextField.setText(newValue.substring(0, 16))
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

    // TODO add validation for IP-address field

    loginButton.setOnAction(event => {
      login(event)
    })
  }
}
