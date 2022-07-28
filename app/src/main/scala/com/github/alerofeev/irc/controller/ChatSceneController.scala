package com.github.alerofeev.irc.controller

import akka.actor.typed.ActorSystem
import com.github.alerofeev.irc.actor.RootBehavior
import com.github.alerofeev.irc.actor.RootBehavior.PostMessage
import com.typesafe.config.ConfigFactory
import javafx.application.Platform
import javafx.event.{Event, EventHandler}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Label, ScrollPane, TextField}
import javafx.scene.image.ImageView
import javafx.scene.{Node, Scene}
import javafx.scene.layout.{BorderPane, VBox}
import javafx.stage.{Screen, Stage, WindowEvent}
import javafx.scene.input.{KeyCode, KeyEvent}

import java.util.Objects

class ChatSceneController(private val login: String) {

  @FXML
  protected var chatContainer: VBox = _

  @FXML
  protected var chatPreviewContainer: VBox = _

  @FXML
  protected var chatScrollPane: ScrollPane = _

  @FXML
  protected var loginLabel: Label = _

  @FXML
  protected var mainScene: BorderPane = _

  @FXML
  protected var messageTextField: TextField = _

  @FXML
  protected var recipientTextField: TextField = _

  @FXML
  protected var sendMessageButton: ImageView = _

  private final var actorSystem: ActorSystem[RootBehavior.Command] = _

  /**
   * Возвращает логин
   * @return
   */
  def getLogin: String = {
    login
  }

  /**
   * Запускает акторную систему
   * @param port _
   * @param controller контроллер ChatScene
   */
  def startup(address: String, port: String, controller: ChatSceneController): Unit = {
    val config = ConfigFactory.parseString(
      s"""akka.remote.artery.canonical.port=$port
         |akka.remote.artery.canonical.hostname=$address
         |akka.cluster.seed-nodes=["akka://chat@$address:25251", "akka://chat@$address:25252"]"""
        .stripMargin)
      .withFallback(ConfigFactory.load("application.conf"))

    actorSystem = ActorSystem(RootBehavior(controller), "chat", config)
  }

  /**
   * Загружает сцену ChatScene
   * @param event _
   * @param loader _
   */
  def loadScene(event: Event, loader: FXMLLoader): Unit = {
    val screenBounds = Screen.getPrimary.getVisualBounds
    val window = event.getSource.asInstanceOf[Node].getScene.getWindow
    val stage = window.asInstanceOf[Stage]

    window.setX((screenBounds.getWidth - 628) / 2)
    window.setY((screenBounds.getHeight - 660) / 2)

    stage.setScene(new Scene(loader.load(), 628, 660))

    stage.setOnCloseRequest((_: WindowEvent) => {
      actorSystem.terminate()
      Platform.exit()
      System.exit(0)
    })
  }

  /**
   *
   * @param login _
   * @param content содержимое сообщения
   * @param modifier модификатор доступа
   */
   def displayMessage(login: String, content: String, modifier: Int = -1): Unit = {
     Platform.runLater(() => {
       val loader: FXMLLoader = new FXMLLoader(Objects.requireNonNull(getClass.getResource(
         "/view/template/messageBoxScene.fxml")))
       val node: VBox = loader.load()
       loader.getController.asInstanceOf[MessageBoxSceneController].setContent(login, content, modifier)
       chatContainer.getChildren.add(node)

       chatScrollPane.setVvalue(1d)
       messageTextField.setText("")
       messageTextField.requestFocus()
     })
  }

  /**
   * Отправляет сообщение в акторную систему
   */
  private def sendMessage(): Unit = {

    if (messageTextField.getText.nonEmpty) {
      val recipient: String = recipientTextField.getText
      actorSystem ! PostMessage(messageTextField.getText,
        if (recipient == null || recipient.isBlank) "" else recipient)
    }

    // TODO: add validation for message

  }

  def initialize(): Unit = {
    Platform.runLater(() => mainScene.requestFocus())

    mainScene.setOnMouseClicked(_ => mainScene.requestFocus())

    messageTextField.setOnKeyPressed(new EventHandler[KeyEvent]() {
      override def handle(event: KeyEvent): Unit = {
        if (event.getCode == KeyCode.ENTER) {
          sendMessage()
        }
      }
    })

    loginLabel.setText(login)

    sendMessageButton.setOnMouseClicked(_ => {
      sendMessage()
    })
  }
}
