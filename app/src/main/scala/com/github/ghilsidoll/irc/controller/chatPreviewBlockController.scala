package com.github.ghilsidoll.irc.controller

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.HBox

class chatPreviewBlockController {

  @FXML
  protected var mainSceneHBox: HBox = _

  @FXML
  protected var usernameLabel: Label = _

  @FXML
  protected var timeLabel: Label = _

  @FXML
  protected var messagePreviewLabel: Label = _

  def initialize(): Unit = {

  }
}
