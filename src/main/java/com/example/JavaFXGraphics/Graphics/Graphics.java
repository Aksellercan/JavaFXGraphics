package com.example.JavaFXGraphics.Graphics;

import com.example.JavaFXGraphics.Game.Mechanics;
import com.example.JavaFXGraphics.Objects.Enemy;
import com.example.JavaFXGraphics.Objects.Player;
import com.example.JavaFXGraphics.Tools.Files.JSONParser;
import com.example.JavaFXGraphics.Tools.Logger.Logger;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.concurrent.atomic.AtomicInteger;

public class Graphics extends Application {
    private final Label buttonLabel = new Label();
    private final Button retryButton = new Button();
    private final Label highScoreLabel = new Label();
    private final Label numberOfBlocksOnFieldLabel = new Label();
    private final Label scoreLabel = new Label();

    @Override
    public void start(Stage stage) {
        /*
        Window setup
         */
        stage.setWidth(1000);
        stage.setHeight(800);
        stage.setResizable(false);
        stage.setOnCloseRequest( e -> {
            Logger.INFO.Log("Closing... Reason: " + e.getEventType());
            Window.setAtomicBoolean(false);
            JSONParser.MapAndWriteConfig();
        });
        Logger.INFO.Log("Started...");
        JSONParser.ReadConfigAndMap();
        /*
        Create object
         */
        Player.setSprite(new ImageView(new Image("/Sprites/leaves.png")));
        Enemy enemy = new Enemy(new ImageView(new Image("/Sprites/crafter.png")));
        /*
        Add objects
         */
        AtomicInteger count = new AtomicInteger();
        retryButton.setText("Restart");
        enemy.getSprite().setId("crafter.png");
        Group root = new Group();
        root.setId("Background Root");
        Window.setRoot(root);
        Logger.INFO.Log("Root: " + Window.getRoot().getId());
        Logger.INFO.Log("Player base point value: " + Player.getBasePoint());

        /*
        Restart button functions
         */
        retryButton.setOnAction(e -> {
            Logger.DEBUG.Log("Button " + e.getEventType());
            Mechanics.GameHUD();
            Mechanics.RestartGame(enemy, root);
            count.set(0);
        });
        stage.setTitle("JavaFX Graphics Test: " + Player.getName());
        Scene scene = new Scene(root, stage.getHeight(), stage.getWidth());
        scene.setCamera(new PerspectiveCamera());
        Logger.INFO.Log("Player name: " + Player.getName());
        /*
        Set coordinates
         */
        numberOfBlocksOnFieldLabel.translateXProperty().set(680);
        numberOfBlocksOnFieldLabel.translateYProperty().set(740);
        numberOfBlocksOnFieldLabel.setId("numberOfBlocksOnFieldLabel");
        highScoreLabel.translateXProperty().set(780);
        highScoreLabel.translateYProperty().set(740);
        highScoreLabel.setId("highScoreLabel");
        scoreLabel.translateXProperty().set(920);
        scoreLabel.translateYProperty().set(740);
        scoreLabel.setId("scoreLabel");
        buttonLabel.translateXProperty().set(470);
        buttonLabel.translateYProperty().set(345);
        buttonLabel.setId("buttonLabel");
        retryButton.translateXProperty().set(465);
        retryButton.translateYProperty().set(370);
        retryButton.setId("retryButton");
        /*
        Add children
         */
        Window.AddLabelToList(numberOfBlocksOnFieldLabel);
        Window.AddLabelToList(highScoreLabel);
        Window.AddLabelToList(scoreLabel);
        Window.AddLabelToList(buttonLabel);
        Window.AddButtonToList(retryButton);
        Window.setAtomicBoolean(true);
        if (!Enemy.getDisableBot()) root.getChildren().add(enemy.getSprite());
        Mechanics.GameHUD();
        root.getChildren().add(Player.getSprite());
        stage.setScene(scene);
        /*
        Start game
         */
        Mechanics.StartGame(Player.getSprite(), enemy, root);
        /*
        Keypress Event Listener
         */
        count.set(0);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (Window.getAtomicBoolean()) {
                switch (e.getCode()) {
                    case UP:
                    case W:
                        if (!(Player.getSprite().getTranslateY() <= 0))
                            Player.getSprite().translateYProperty().set(Player.getSprite().getTranslateY() - 20);
                        Logger.DEBUG.Log("move up " + e.getText(), false);
                        break;
                    case LEFT:
                    case A:
                        if (!(Player.getSprite().getTranslateX() <= 0))
                            Player.getSprite().translateXProperty().set(Player.getSprite().getTranslateX() - 20);
                        Logger.DEBUG.Log("move left " + e.getText(), false);
                        break;
                    case RIGHT:
                    case D:
                        if (!(Player.getSprite().getTranslateX() >= 980))
                            Player.getSprite().translateXProperty().set(Player.getSprite().getTranslateX() + 20);
                        Logger.DEBUG.Log("move right " + e.getText(), false);
                        break;
                    case DOWN:
                    case S:
                        if (!(Player.getSprite().getTranslateY() >= 740))
                            Player.getSprite().translateYProperty().set(Player.getSprite().getTranslateY() + 20);
                        Logger.DEBUG.Log("move down " + e.getText(), false);
                        break;
                }
                Logger.DEBUG.Log("X axis " + Player.getSprite().getTranslateX(), false);
                Logger.DEBUG.Log("Y axis " + Player.getSprite().getTranslateY(), false);
                /*
                Monitor game status and increment score
                 */
                Mechanics.MonitorGameStatus();
            } else if (count.get() == 0) {
                if (Player.getHighScore() < Player.getScore()) {
                    Player.setHighScore(Player.getScore());
                }
                Mechanics.GameOverScreen();
            }
        });
        stage.show();
        Window.setPrimaryStage(stage);
    }
}
