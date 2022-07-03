package com.github.ghilsidoll.irc.controller

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane

class MainSceneController {

    @FXML
    protected var mainScene: BorderPane = _

    @FXML
    def initialize(): Unit = {
        Platform.runLater(() => mainScene.requestFocus())
        mainScene.setOnMouseClicked(_ => mainScene.requestFocus())
    }
}
