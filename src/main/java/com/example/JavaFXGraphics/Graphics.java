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
    private final int maxScore = 2;

    @Override
    public void start(Stage stage) {
        /*
        Window setup
         */
        stage.setWidth(1000);
        stage.setHeight(800);
        stage.setResizable(false);
        stage.setTitle("JavaFX Graphics Test");
        stage.setOnCloseRequest( e -> System.out.println("Closing...\n" + e.toString()));
        /*
        Add objects
         */
        Label buttonLabel = new Label();
        buttonLabel.visibleProperty().set(false);
        Button retryButton = new Button();
        retryButton.setText("Restart");
        retryButton.visibleProperty().set(false);
        System.out.println("Button visible: " + retryButton.isVisible());
        Label scoreLabel = new Label();
        scoreLabel.setText("Score: " + score.get());
        ImageView cImage = new ImageView(new Image("/Sprites/leaves.png"));
        ImageView bImage = new ImageView(new Image("/Sprites/nether.png"));
        /*
        Restart button functions
         */
        retryButton.setOnAction(e -> {
            System.out.println("Button " + e.toString());
            RestartGame(cImage, bImage);
            score.set(0);
            retryButton.visibleProperty().set(false);
            scoreLabel.setText("Score: " + score.get());
            buttonLabel.visibleProperty().set(false);
            scoreLabel.visibleProperty().set(true);
            continueGame.set(true);
        });
        Group newGroup = new Group();
        Scene newScene = new Scene(newGroup, stage.getHeight(), stage.getWidth());
        newScene.setCamera(new PerspectiveCamera());
        /*
        Spawn sprites in random locations
         */
        CalculateNextPosition(cImage);
        CalculateNextPosition(bImage);
        /*
        Set coordinates
         */
        scoreLabel.translateXProperty().set(920);
        scoreLabel.translateYProperty().set(740);
        buttonLabel.translateXProperty().set(470);
        buttonLabel.translateYProperty().set(345);
        retryButton.translateXProperty().set(465);
        retryButton.translateYProperty().set(370);
        /*
        Add children
         */
        newGroup.getChildren().add(buttonLabel);
        newGroup.getChildren().add(retryButton);
        newGroup.getChildren().add(scoreLabel);
        newGroup.getChildren().add(cImage);
        newGroup.getChildren().add(bImage);
        stage.setScene(newScene);
        /*
        Keypress Event Listener
         */
        continueGame.set(true);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (continueGame.get()) {
                switch (e.getCode()) {
                    case UP:
                    case W:
                        if (!(cImage.getTranslateY() <= 0))
                            cImage.translateYProperty().set(cImage.getTranslateY() - 20);
                        System.out.println("move up " + e.getText());
                        break;
                    case LEFT:
                    case A:
                        if (!(cImage.getTranslateX() <= 0))
                            cImage.translateXProperty().set(cImage.getTranslateX() - 20);
                        System.out.println("move left " + e.getText());
                        break;
                    case RIGHT:
                    case D:
                        if (!(cImage.getTranslateX() >= 980))
                            cImage.translateXProperty().set(cImage.getTranslateX() + 20);
                        System.out.println("move right " + e.getText());
                        break;
                    case DOWN:
                    case S:
                        if (!(cImage.getTranslateY() >= 740))
                            cImage.translateYProperty().set(cImage.getTranslateY() + 20);
                        System.out.println("move down " + e.getText());
                        break;
                }
                System.out.println("X axis " + cImage.getTranslateX());
                System.out.println("Y axis " + cImage.getTranslateY());
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
                    System.out.println("You Won!");
                    continueGame.set(false);
                    scoreLabel.visibleProperty().set(false);
                    buttonLabel.visibleProperty().set(true);
                    buttonLabel.setText("Score: " + score.get());
                    retryButton.visibleProperty().set(true);
                }
            }
        });
        stage.show();
    }

    private void RestartGame(ImageView playerSprite, ImageView enemySprite) {
        CalculateNextPosition(playerSprite);
        CalculateNextPosition(enemySprite);
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
