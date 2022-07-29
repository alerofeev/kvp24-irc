package com.github.alerofeev.irc.controller

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.paint.Color

class MessageBoxSceneController {

  @FXML
  protected var loginLabel: Label = _

  @FXML
  protected var messageContentLabel: Label = _

  @FXML
  protected var modifierLabel: Label = _

  def setContent(login: String, messageContent: String, modifier: Int = -1): Unit = {
    loginLabel.setText(login)
    messageContentLabel.setText(messageContent)

    modifier match {
      case -1 =>
      case 0 =>
        modifierLabel.setText("[Вы]")
        modifierLabel.setTextFill(Color.rgb(38, 228, 89))
      case 1 =>
        modifierLabel.setText("[Вам]")
        modifierLabel.setTextFill(Color.rgb(255, 65, 75))
    }
  }
}
