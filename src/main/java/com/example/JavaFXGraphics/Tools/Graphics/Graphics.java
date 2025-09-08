package com.example.JavaFXGraphics.Tools.Graphics;

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
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Graphics extends Application {
    private final AtomicInteger score = new AtomicInteger();
    private final AtomicBoolean continueGame = new AtomicBoolean();
    private ImageView[] objectArray;
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
        stage.setTitle("JavaFX Graphics Test");
        stage.setOnCloseRequest( e -> {
            Logger.INFO.Log("Closing... Reason: " + e.getEventType());
            continueGame.set(false);
            JSONParser.MapAndWriteConfig();
        });
        Logger.INFO.Log("Started...");
        UpdateSessionSettings();
        HighScore.set(Player.getHighScore());
        amountToAdd = Player.getAmountToAdd();
        objectArray = new ImageView[amountToAdd];
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
            CleanField(root);
            SpawnManyInRandomLocations(amountToAdd, root);
            StartGame(Player.getSprite(), enemy);
            scoreLabel.setText("Score: " + score.get());
            stage.setTitle("JavaFX Graphics Test");
            count.set(0);
            JSONParser.MapAndWriteConfig();
        });
        Scene scene = new Scene(root, stage.getHeight(), stage.getWidth());
        scene.setCamera(new PerspectiveCamera());
        /*
        Set coordinates
         */
        numberOfBlocksOnFieldLabel.translateXProperty().set(680);
        numberOfBlocksOnFieldLabel.translateYProperty().set(740);
        highScoreLabel.translateXProperty().set(780);
        highScoreLabel.translateYProperty().set(740);
        scoreLabel.translateXProperty().set(920);
        scoreLabel.translateYProperty().set(740);
        buttonLabel.translateXProperty().set(470);
        buttonLabel.translateYProperty().set(345);
        retryButton.translateXProperty().set(465);
        retryButton.translateYProperty().set(370);
        /*
        Add children
         */
        if (!Enemy.getDisableBot()) root.getChildren().add(enemy.getSprite());
        GameHUD(root);
        root.getChildren().add(Player.getSprite());
        stage.setScene(scene);
        /*
        Start game
         */
        StartGame(Player.getSprite(), enemy);
        SpawnManyInRandomLocations(amountToAdd, root);
        /*
        Keypress Event Listener
         */
        count.set(0);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (continueGame.get()) {
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
                CheckAndRemove(Player.getSprite(), root);
            } else {
                if (count.get() < 1) {
                    if (HighScore.get() < score.get()) {
                        HighScore.set(score.get());
                    }
                    stage.setTitle("Game Over!");
                    Logger.INFO.Log("Game Over!");
                    continueGame.set(false);
                    GameOverScreen(root);
                }
                count.getAndIncrement();
            }
        });
        stage.show();
    }

    private void CleanField(Group root) {
        for (ImageView object : objectArray) {
            root.getChildren().remove(object);
        }
    }

    private void CheckAndRemove(ImageView player, Group root) {
        if (amountToAdd == 0) {
            amountToAdd = Player.getAmountToAdd();
            SpawnManyInRandomLocations(amountToAdd, root);
        }
        for (int i = 0; i < objectArray.length; i++){
            if (objectArray[i] == null) continue;
            ImageView object = objectArray[i];
            if ((player.getTranslateY() == object.getTranslateY() && player.getTranslateX() == object.getTranslateX())) {
                Logger.DEBUG.Log("\nPlayer Location: X " + player.getTranslateX() + " Y " + player.getTranslateY() + ".\nObject Location: X " + object.getTranslateX() + " Y " + object.getTranslateY());
                root.getChildren().remove(object);
                objectArray[i] = null;
                Logger.DEBUG.Log("Removed: " + object.getId() + ", amount left on field: " + amountToAdd);
                score.getAndIncrement();
                scoreLabel.setText("Score: " + score.get());
                amountToAdd--;
                numberOfBlocksOnFieldLabel.setText("Left: " + amountToAdd);
                Logger.DEBUG.Log("Left on field: " + amountToAdd);
                break;
            }
        }
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

    private void StartGame(ImageView playerSprite, Enemy enemy) {
        /*
        Spawn sprites in random locations
         */
        continueGame.set(true);
        score.set(0);
        CalculateNextPosition(playerSprite);
        CalculateNextPosition(enemy.getSprite());
        if (!Enemy.getDisableBot()) {
            Thread moveEnemySpriteThread = new Thread(() -> {
                try {
                    MoveEnemySprite(enemy.getSprite(), playerSprite);
                } catch (InterruptedException e) {
                    Logger.ERROR.LogException(e);
                }
            });
            moveEnemySpriteThread.start();
        }
    }

    private void MoveEnemySprite(ImageView enemy, ImageView sprite) throws InterruptedException {
        /*
        get current location of sprite
        if its on same X coordination move down the Y axis
        if its on same Y coordination move down the X axis
        Runs concurrently
        */
        Logger.INFO.Log("Enemy speed: " + Enemy.getSpeed());
        while (continueGame.get()) {
            if ((sprite.getTranslateX() == enemy.getTranslateX()) && (sprite.getTranslateY() == enemy.getTranslateY())) {
                Logger.INFO.Log("Player got caught!");
                continueGame.set(false);
                break;
            }
            Thread.sleep(Enemy.getSpeed());
            if (!(sprite.getTranslateX() == enemy.getTranslateX()) || !(sprite.getTranslateY() == enemy.getTranslateY())) {
                if (sprite.getTranslateY() != enemy.getTranslateY()) {
                    if (sprite.getTranslateY() < enemy.getTranslateY()) {
                        Logger.DEBUG.Log("Enemy move up");
                        enemy.translateYProperty().set(enemy.getTranslateY() - 20);
                    } else {
                        Logger.DEBUG.Log("Enemy move down");
                        enemy.translateYProperty().set(enemy.getTranslateY() + 20);
                    }
                    continue;
                }
                if (sprite.getTranslateX() != enemy.getTranslateX()) {
                    if (sprite.getTranslateX() < enemy.getTranslateX()) {
                        Logger.DEBUG.Log("Enemy move left");
                        enemy.translateXProperty().set(enemy.getTranslateX() - 20);
                    } else {
                        Logger.DEBUG.Log("Enemy move right");
                        enemy.translateXProperty().set(enemy.getTranslateX() + 20);
                    }
                }
            }
        }
    }

    private void SpawnManyInRandomLocations(int amount, Group root) {
        for (int i = 0; i < amount; i++) {
            ImageView loopable = new ImageView(new Image("Sprites/nether.png"));
            loopable.setId("nether" + i + ".png");
            CalculateNextPosition(loopable);
            objectArray[i] = loopable;
            root.getChildren().add(loopable);
        }
    }

    private void CalculateNextPosition(ImageView sprite) {
        Random rand = new Random();
        int randX;
        int randY;
        do {
            randX = rand.nextInt(980);
            randY = rand.nextInt(740);
        } while (!((randX % 20 == 0) && (randY % 20 == 0)));
        sprite.translateXProperty().set(randX);
        sprite.translateYProperty().set(randY);
        Logger.INFO.Log(sprite.getId() + ": X = " + sprite.getTranslateX() + ", Y = " + sprite.getTranslateY());
    }

    private void UpdateSessionSettings() {
        JSONParser.ReadConfigAndMap();
    }
}
