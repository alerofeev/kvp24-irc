package com.github.ghilsidoll.irc

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.io.IOException
import java.util.Objects

class App extends Application {
  @throws(classOf[IOException])
  override def start(primaryStage: Stage): Unit = {
    val root: Parent = FXMLLoader.load(Objects.requireNonNull(getClass.getResource("/com/github/ghilsidoll/" +
      "irc/view/mainScene.fxml")))
    primaryStage.setScene(new Scene(root, 1000, 760))
    primaryStage.setTitle("kv24-irc")
    primaryStage.setResizable(false)
    primaryStage.show()
    // primaryStage.setMinWidth(320)
    // primaryStage.setMinHeight(480)
  }
}


