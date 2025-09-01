package com.example.JavaFXGraphics;

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
    private final Label buttonLabel = new Label();
    private final Button retryButton = new Button();
    private final Label maxScoreLabel = new Label();
    private final Label scoreLabel = new Label();
    private final int maxScore = 10;

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
            Logger.INFO.Log("Closing...\n" + e.toString());
            continueGame.set(false);
        });
        Logger.setVerboseLogFile(false);
        Logger.setColouredOutput(true);
        Logger.setDebugOutput(true);
        /*
        Add objects
         */
        AtomicInteger count = new AtomicInteger();
        buttonLabel.visibleProperty().set(false);
        retryButton.setText("Restart");
        retryButton.visibleProperty().set(false);
        Logger.DEBUG.Log("Button visible: " + retryButton.isVisible());
        maxScoreLabel.setText("Objective Score: " + maxScore);
        scoreLabel.setText("Score: " + score.get());
        ImageView cImage = new ImageView(new Image("/Sprites/leaves.png"));
        ImageView bImage = new ImageView(new Image("/Sprites/nether.png"));
        ImageView aImage = new ImageView(new Image("/Sprites/crafter.png"));
        /*
        Restart button functions
         */
        retryButton.setOnAction(e -> {
            Logger.DEBUG.Log("Button " + e.toString());
            StartGame(cImage, bImage, aImage);
            retryButton.visibleProperty().set(false);
            scoreLabel.setText("Score: " + score.get());
            buttonLabel.visibleProperty().set(false);
            scoreLabel.visibleProperty().set(true);
            maxScoreLabel.visibleProperty().set(true);
            stage.setTitle("JavaFX Graphics Test");
            count.set(0);
        });
        Group newGroup = new Group();
        Scene newScene = new Scene(newGroup, stage.getHeight(), stage.getWidth());
        newScene.setCamera(new PerspectiveCamera());
        /*
        Set coordinates
         */
        maxScoreLabel.translateXProperty().set(760);
        maxScoreLabel.translateYProperty().set(740);
        scoreLabel.translateXProperty().set(920);
        scoreLabel.translateYProperty().set(740);
        buttonLabel.translateXProperty().set(470);
        buttonLabel.translateYProperty().set(345);
        retryButton.translateXProperty().set(465);
        retryButton.translateYProperty().set(370);
        /*
        Add children
         */
        newGroup.getChildren().add(aImage);
        newGroup.getChildren().add(maxScoreLabel);
        newGroup.getChildren().add(buttonLabel);
        newGroup.getChildren().add(retryButton);
        newGroup.getChildren().add(scoreLabel);
        newGroup.getChildren().add(cImage);
        newGroup.getChildren().add(bImage);
        stage.setScene(newScene);
        /*
        Start game
         */
        StartGame(cImage, bImage, aImage);
        /*
        Keypress Event Listener
         */
        count.set(0);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (continueGame.get()) {
                switch (e.getCode()) {
                    case UP:
                    case W:
                        if (!(cImage.getTranslateY() <= 0))
                            cImage.translateYProperty().set(cImage.getTranslateY() - 20);
                        Logger.DEBUG.Log("move up " + e.getText());
                        break;
                    case LEFT:
                    case A:
                        if (!(cImage.getTranslateX() <= 0))
                            cImage.translateXProperty().set(cImage.getTranslateX() - 20);
                        Logger.DEBUG.Log("move left " + e.getText());
                        break;
                    case RIGHT:
                    case D:
                        if (!(cImage.getTranslateX() >= 980))
                            cImage.translateXProperty().set(cImage.getTranslateX() + 20);
                        Logger.DEBUG.Log("move right " + e.getText());
                        break;
                    case DOWN:
                    case S:
                        if (!(cImage.getTranslateY() >= 740))
                            cImage.translateYProperty().set(cImage.getTranslateY() + 20);
                        Logger.DEBUG.Log("move down " + e.getText());
                        break;
                }
                Logger.DEBUG.Log("X axis " + cImage.getTranslateX());
                Logger.DEBUG.Log("Y axis " + cImage.getTranslateY());
                /*
                Monitor game status and increment score
                 */
                if (CheckStatus(cImage, bImage)) {
                    stage.setTitle("Next!");
                    score.set(score.get()+1);
                    if (!(score.get() == maxScore)) CalculateNextPosition(bImage);
                    scoreLabel.setText("Score: " + score.get());
                }
                if (score.get() == maxScore) {
                    stage.setTitle("You Won!");
                    Logger.INFO.Log("You Won!");
                    continueGame.set(false);
                    GameOverScreen();
                }
            } else {
                if (count.get() < 1) {
                    stage.setTitle("You Lost!");
                    Logger.INFO.Log("You Lost!");
                    continueGame.set(false);
                    GameOverScreen();
                }
                count.getAndIncrement();
            }
        });
        stage.show();
    }

    private void GameOverScreen() {
        maxScoreLabel.visibleProperty().set(false);
        scoreLabel.visibleProperty().set(false);
        buttonLabel.visibleProperty().set(true);
        buttonLabel.setText("Score: " + score.get());
        retryButton.visibleProperty().set(true);
    }

    private void StartGame(ImageView playerSprite, ImageView objectSprite,  ImageView enemySprite) {
        /*
        Spawn sprites in random locations
         */
        continueGame.set(true);
        score.set(0);
        CalculateNextPosition(playerSprite);
        CalculateNextPosition(objectSprite);
        CalculateNextPosition(enemySprite);
        MoveEnemySprite(enemySprite, playerSprite);
    }

    private void MoveEnemySprite(ImageView enemy, ImageView sprite) {
        /*
        get current location of sprite
        if its on same X coordination move down the Y axis
        if its on same Y coordination move down the X axis
        Runs concurrently
        */
        Thread moveThread = new Thread(() -> {
            while (continueGame.get()) {
                if ((sprite.getTranslateX() == enemy.getTranslateX()) && (sprite.getTranslateY() == enemy.getTranslateY())) {
                    Logger.INFO.Log("Player got caught!");
                    continueGame.set(false);
                    break;
                }
                try {
                    Thread.sleep(180);
                } catch (InterruptedException e) {
                    Logger.ERROR.LogException(e);
                    break;
                }
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
        });
        moveThread.start();
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
        Logger.INFO.Log(sprite + "\n-> X = " + sprite.getTranslateX() + "\n-> Y = " + sprite.getTranslateY());
    }

    private boolean CheckStatus(ImageView cImage, ImageView bImage) {
        if ((cImage.getTranslateY() == bImage.getTranslateY() && cImage.getTranslateX() == bImage.getTranslateX())) {
            Logger.DEBUG.Log("Next!");
            return true;
        }
        return false;
    }
}
