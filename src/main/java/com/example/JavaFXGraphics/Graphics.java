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
    private final int maxScore = 4;

    @Override
    public void start(Stage stage) {
        stage.setWidth(1000);
        stage.setHeight(800);
        stage.setTitle("JavaFX Graphics Test");
        Button retryButton = new Button();
        retryButton.setText("Retry");
        retryButton.visibleProperty().set(false);
        System.out.println("Button visible: " + retryButton.isVisible());
        Label scoreLabel = new Label();
        scoreLabel.setText("Score: " + score.get());
        ImageView cImage = new ImageView(new Image("/Sprites/leaves.png"));
        ImageView bImage = new ImageView(new Image("/Sprites/nether.png"));
        retryButton.setOnAction(e -> {
            System.out.println("Button " + e.toString());
            RestartGame(cImage, bImage);
            retryButton.visibleProperty().set(false);
            scoreLabel.setText("Score: " + score.get());
            continueGame.set(true);
        });
        Group newGroup = new Group();
        Scene newScene = new Scene(newGroup, stage.getHeight(), stage.getWidth());
        newScene.setCamera(new PerspectiveCamera());
        CalculateNextPosition(cImage);
        CalculateNextPosition(bImage);
        scoreLabel.translateXProperty().set(980);
        scoreLabel.translateXProperty().set(740);
        retryButton.translateXProperty().set(470);
        retryButton.translateYProperty().set(370);
        newGroup.getChildren().add(retryButton);
        newGroup.getChildren().add(scoreLabel);
        newGroup.getChildren().add(cImage);
        newGroup.getChildren().add(bImage);
        stage.setScene(newScene);
        continueGame.set(true);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (continueGame.get()) {
                switch (e.getCode()) {
                    case UP:
                    case W:
                        if (!(cImage.getTranslateY() == 0))
                            cImage.translateYProperty().set(cImage.getTranslateY() - 20);
                        System.out.println("move up " + e.getText());
                        break;
                    case LEFT:
                    case A:
                        if (!(cImage.getTranslateX() == 0))
                            cImage.translateXProperty().set(cImage.getTranslateX() - 20);
                        System.out.println("move left " + e.getText());
                        break;
                    case RIGHT:
                    case D:
                        if (!(cImage.getTranslateX() == 980))
                            cImage.translateXProperty().set(cImage.getTranslateX() + 20);
                        System.out.println("move right " + e.getText());
                        break;
                    case DOWN:
                    case S:
                        if (!(cImage.getTranslateY() == 740))
                            cImage.translateYProperty().set(cImage.getTranslateY() + 20);
                        System.out.println("move down " + e.getText());
                        break;
                }
                System.out.println("X axis " + cImage.getTranslateX());
                System.out.println("Y axis " + cImage.getTranslateY());
                if (CheckStatus(cImage, bImage)) {
                    stage.setTitle("Next!");
                    score.set(score.get()+1);
                    if (!(score.get() == maxScore)) CalculateNextPosition(bImage);
                    scoreLabel.setText("Score: " + score.get());
                }
                if (score.get() == maxScore) {
                    stage.setTitle("You Won!");
                    System.out.println("You Won!");
                    continueGame.set(false);
                    retryButton.visibleProperty().set(true);
                }
            }
        });
        stage.show();
    }

    private void RestartGame(ImageView playerSprite, ImageView enemySprite) {
        CalculateNextPosition(playerSprite);
        CalculateNextPosition(enemySprite);
        score.set(0);
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
        System.out.println(sprite + "\n-> X = " + sprite.getTranslateX() + "\n-> Y = " + sprite.getTranslateY());
    }

    private boolean CheckStatus(ImageView cImage, ImageView bImage) {
        if ((cImage.getTranslateY() == bImage.getTranslateY() && cImage.getTranslateX() == bImage.getTranslateX())) {
            System.out.println("Next!");
            return true;
        }
        return false;
    }
}
