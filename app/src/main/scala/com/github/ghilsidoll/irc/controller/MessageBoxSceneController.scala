package com.github.ghilsidoll.irc.controller

import javafx.fxml.FXML
import javafx.scene.control.Label

class MessageBoxSceneController {

  @FXML
  protected var loginLabel: Label = _

  @FXML
  protected var messageContentLabel: Label = _

  @FXML
  protected var youLabel: Label = _

  def setContent(login: String, messageContent: String, isYou: Boolean): Unit = {
    loginLabel.setText(login)
    messageContentLabel.setText(messageContent)

    if (isYou) {
      youLabel.setVisible(true)
    }
  }
}
