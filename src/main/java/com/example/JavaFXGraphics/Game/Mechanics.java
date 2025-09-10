package com.example.JavaFXGraphics.Game;

import com.example.JavaFXGraphics.Objects.Enemy;
import com.example.JavaFXGraphics.Objects.Player;
import com.example.JavaFXGraphics.Tools.Files.JSONParser;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.example.JavaFXGraphics.Graphics.Window;
import com.example.JavaFXGraphics.Tools.Logger.Logger;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public final class Mechanics {
    private static ImageView[] objectArray;
    private static int leftOnField = Player.getAmountToAdd();

    private Mechanics() {}

    public static void RestartGame(Enemy enemy , Group root) {
        Label scoreLabel = Window.getLabel("scoreLabel");
        if (scoreLabel == null) {
            Logger.ERROR.Log("score label is null");
            return;
        }
        scoreLabel.setText("Score: " + Player.getScore());
        Window.getPrimaryStage().setTitle("JavaFX Graphics Test: " + Player.getName());
        CleanField(root);
        SpawnManyInRandomLocations(Player.getAmountToAdd(), root);
        StartGame(Player.getSprite(), enemy);
        JSONParser.MapAndWriteConfig();
    }

    public static void StartGame(ImageView playerSprite, Enemy enemy) {
        /*
        Spawn sprites in random locations
         */
        objectArray = new ImageView[Player.getAmountToAdd()];
        Window.setAtomicBoolean(true);
        Player.setScore(0);
        CalculateNextPosition(playerSprite);
        if (!Enemy.getDisableBot()) {
            CalculateNextPosition(enemy.getSprite());
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

    public static void MonitorGameStatus() {
        CheckAndRemove();
    }

    public static void CleanField(Group root) {
        for (ImageView object : objectArray) {
            root.getChildren().remove(object);
        }
    }

    public static void MoveEnemySprite(ImageView enemy, ImageView sprite) throws InterruptedException {
        /*
        get current location of sprite
        if its on same X coordination move down the Y axis
        if its on same Y coordination move down the X axis
        Runs concurrently
        */
        Logger.INFO.Log("Enemy speed: " + Enemy.getSpeed());
        while (Window.getAtomicBoolean()) {
            if ((sprite.getTranslateX() == enemy.getTranslateX()) && (sprite.getTranslateY() == enemy.getTranslateY())) {
                Logger.INFO.Log("Player got caught!");
                Window.setAtomicBoolean(false);
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

    public static void SpawnManyInRandomLocations(int amount, Group root) {
        for (int i = 0; i < amount; i++) {
            ImageView loopable = new ImageView(new Image("Sprites/nether.png"));
            loopable.setId("nether" + i + ".png");
            CalculateNextPosition(loopable);
            objectArray[i] = loopable;
            root.getChildren().add(loopable);
        }
    }

    public static void CalculateNextPosition(ImageView sprite) {
        Random rand = new Random();
        int randX;
        int randY;
        do {
            randX = rand.nextInt(980);
            randY = rand.nextInt(740);
        } while (!((randX % 20 == 0) && (randY % 20 == 0)));
        sprite.translateXProperty().set(randX);
        sprite.translateYProperty().set(randY);
        Logger.DEBUG.Log(sprite.getId() + ": X = " + sprite.getTranslateX() + ", Y = " + sprite.getTranslateY());
    }

    public static void CheckAndRemove() {
        Group root = Window.getRoot();
        ImageView player = Player.getSprite();
        if (leftOnField == 0) {
            leftOnField = Player.getAmountToAdd();
            SpawnManyInRandomLocations(leftOnField, root);
        }
        for (int i = 0; i < objectArray.length; i++){
            if (objectArray[i] == null) continue;
            ImageView object = objectArray[i];
            if ((player.getTranslateY() == object.getTranslateY() && player.getTranslateX() == object.getTranslateX())) {
                Logger.DEBUG.Log("\nPlayer Location: X " + player.getTranslateX() + " Y " + player.getTranslateY() +
                        ".\nObject Location: X " + object.getTranslateX() + " Y " + object.getTranslateY());
                root.getChildren().remove(object);
                objectArray[i] = null;
                Logger.DEBUG.Log("Removed: " + object.getId() + ", amount left on field: " + leftOnField);
                Player.setScore(Player.getScore()+1);
                FindLabelById("scoreLabel").setText("Score: " + Player.getScore());
                leftOnField--;
                FindLabelById("numberOfBlocksOnFieldLabel").setText("Left: " + leftOnField);
                Logger.DEBUG.Log("Left on field: " + leftOnField);
                break;
            }
        }
    }

    private static Label FindLabelById(String labelId) {
        Label findLabel = Window.getLabel(labelId);
        if (findLabel == null) {
            throw new NullPointerException("Label by ID " + labelId + " not found");
        }
        Logger.DEBUG.Log("Found label: " + findLabel + ", ID: " + findLabel.getId());
        return findLabel;
    }

    private static Button FindButtonById(String buttonId) {
        Button findButton = Window.getButton(buttonId);
        if (findButton == null) {
            throw new NullPointerException("Label by ID " + buttonId + " not found");
        }
        Logger.DEBUG.Log("Found label: " + findButton + ", ID: " + findButton.getId());
        return findButton;
    }

    public static void GameHUD() {
        Group root = Window.getRoot();
        if (Player.getShowUI()) {
            root.getChildren().add(FindLabelById("numberOfBlocksOnFieldLabel"));
            root.getChildren().add(FindLabelById("highScoreLabel"));
            root.getChildren().add(FindLabelById("scoreLabel"));
        }
        root.getChildren().remove(FindLabelById("buttonLabel"));
        root.getChildren().remove(FindButtonById("retryButton"));
    }

    public static void GameOverScreen() {
        Group root = Window.getRoot();
        if (Player.getShowUI()) {
            root.getChildren().remove(FindLabelById("numberOfBlocksOnFieldLabel"));
            root.getChildren().remove(FindLabelById("highScoreLabel"));
            root.getChildren().remove(FindLabelById("scoreLabel"));
        }
        root.getChildren().add(FindLabelById("buttonLabel"));
        root.getChildren().add(FindButtonById("retryButton"));
        FindLabelById("buttonLabel").setText("Score: " + Player.getScore());
        Player.setHighScore(Player.getHighScore());
    }
}
