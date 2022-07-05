package com.github.ghilsidoll.irc.controller

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.layout.{BorderPane, HBox, VBox}

import java.util

class MainSceneController {

  @FXML
  protected var mainScene: BorderPane = _

  @FXML
  protected var chatPreviewContainer: VBox = _

  def initialize(): Unit = {
    Platform.runLater(() => mainScene.requestFocus())
    mainScene.setOnMouseClicked(_ => mainScene.requestFocus())


  }
}
