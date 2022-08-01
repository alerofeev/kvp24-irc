package com.github.alerofeev.irc.controller

import com.github.alerofeev.irc.modifier.Modifiers
import com.github.alerofeev.irc.modifier.Modifiers.Modifiers

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

  def setContent(from: String, messageContent: String, modifier: Modifiers = Modifiers.PUBLIC): Unit = {
    loginLabel.setText(from)
    messageContentLabel.setText(messageContent)

    modifier match {
      case Modifiers.PUBLIC => // ignored
      case Modifiers.PRIVATE =>
        modifierLabel.setText("[Вам]")
        modifierLabel.setTextFill(Color.rgb(255, 65, 75))
      case Modifiers.YOURS =>
        modifierLabel.setText("[Вы]")
        modifierLabel.setTextFill(Color.rgb(38, 228, 89))
    }
  }
}
