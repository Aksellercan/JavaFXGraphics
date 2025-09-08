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
    private final AtomicInteger score = new AtomicInteger();
    private final Label buttonLabel = new Label();
    private final Button retryButton = new Button();
    private final Label highScoreLabel = new Label();
    private final Label numberOfBlocksOnFieldLabel = new Label();
    private final Label scoreLabel = new Label();
    private int amountToAdd;
    private final AtomicInteger HighScore = new AtomicInteger();

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
        HighScore.set(Player.getHighScore());
        amountToAdd = Player.getAmountToAdd();
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
        highScoreLabel.setText("High Score: " + HighScore.get());
        scoreLabel.setText("Score: " + score.get());
        numberOfBlocksOnFieldLabel.setText("Left: " + amountToAdd);
        enemy.getSprite().setId("crafter.png");
        Group root = new Group();
        /*
        Restart button functions
         */
        retryButton.setOnAction(e -> {
            Logger.DEBUG.Log("Button " + e.toString());
            GameHUD(root);
            Mechanics.CleanField(root);
            Mechanics.SpawnManyInRandomLocations(amountToAdd, root);
            Mechanics.StartGame(Player.getSprite(), enemy);
            scoreLabel.setText("Score: " + score.get());
            stage.setTitle("JavaFX Graphics Test: " + Player.getName());
            count.set(0);
            JSONParser.MapAndWriteConfig();
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
        retryButton.translateXProperty().set(465);
        retryButton.translateYProperty().set(370);
        /*
        Add children
         */
        Window.AddLabelToList(numberOfBlocksOnFieldLabel);
        Window.AddLabelToList(highScoreLabel);
        Window.AddLabelToList(scoreLabel);
        Window.setAtomicBoolean(true);
        if (!Enemy.getDisableBot()) root.getChildren().add(enemy.getSprite());
        GameHUD(root);
        root.getChildren().add(Player.getSprite());
        stage.setScene(scene);
        /*
        Start game
         */
        Mechanics.StartGame(Player.getSprite(), enemy);
        Mechanics.SpawnManyInRandomLocations(amountToAdd, root);
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
                        Logger.DEBUG.Log("move up " + e.getText());
                        break;
                    case LEFT:
                    case A:
                        if (!(Player.getSprite().getTranslateX() <= 0))
                            Player.getSprite().translateXProperty().set(Player.getSprite().getTranslateX() - 20);
                        Logger.DEBUG.Log("move left " + e.getText());
                        break;
                    case RIGHT:
                    case D:
                        if (!(Player.getSprite().getTranslateX() >= 980))
                            Player.getSprite().translateXProperty().set(Player.getSprite().getTranslateX() + 20);
                        Logger.DEBUG.Log("move right " + e.getText());
                        break;
                    case DOWN:
                    case S:
                        if (!(Player.getSprite().getTranslateY() >= 740))
                            Player.getSprite().translateYProperty().set(Player.getSprite().getTranslateY() + 20);
                        Logger.DEBUG.Log("move down " + e.getText());
                        break;
                }
                Logger.DEBUG.Log("X axis " + Player.getSprite().getTranslateX());
                Logger.DEBUG.Log("Y axis " + Player.getSprite().getTranslateY());
                /*
                Monitor game status and increment score
                 */
                Mechanics.CheckAndRemove(Player.getSprite(), root);
            } else {
                if (count.get() < 1) {
                    if (HighScore.get() < score.get()) {
                        HighScore.set(score.get());
                    }
                    stage.setTitle("Game Over!");
                    Logger.INFO.Log("Game Over!");
                    Window.setAtomicBoolean(false);
                    GameOverScreen(root);
                }
                count.getAndIncrement();
            }
        });
        stage.show();
    }

    private void GameHUD(Group root) {
        if (Player.getShowUI()) {
            root.getChildren().add(numberOfBlocksOnFieldLabel);
            root.getChildren().add(highScoreLabel);
            root.getChildren().add(scoreLabel);
        }
        root.getChildren().remove(buttonLabel);
        root.getChildren().remove(retryButton);
    }

    private void GameOverScreen(Group root) {
        if (Player.getShowUI()) {
            root.getChildren().remove(numberOfBlocksOnFieldLabel);
            root.getChildren().remove(highScoreLabel);
            root.getChildren().remove(scoreLabel);
        }
        root.getChildren().add(buttonLabel);
        root.getChildren().add(retryButton);
        buttonLabel.setText("Score: " + score.get());
        Player.setHighScore(HighScore.get());
    }
}
