package com.github.ghilsidoll.irc

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.io.IOException
import java.util.Objects

object App {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[App], args: _*)
  }
}

class App extends Application {
  @throws(classOf[IOException])
  override def start(primaryStage: Stage): Unit = {
    val root: Parent = FXMLLoader.load(Objects.requireNonNull(getClass.getResource("/view/loginScene.fxml")))
    primaryStage.setScene(new Scene(root))
    primaryStage.setTitle("kv24-irc")
    //primaryStage.setResizable(false)
    primaryStage.show()
  }
}

