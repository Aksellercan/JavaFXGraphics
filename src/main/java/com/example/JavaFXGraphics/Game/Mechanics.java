package com.example.JavaFXGraphics.Game;

import com.example.JavaFXGraphics.Objects.Enemy;
import com.example.JavaFXGraphics.Objects.Object;
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
    private static Object[] objectArray;
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
        StartGame(Player.getSprite(), enemy, root);
        JSONParser.MapAndWriteConfig();
    }

    public static void StartGame(ImageView playerSprite, Enemy enemy, Group root) {
        /*
        Spawn sprites in random locations
         */
        objectArray = new Object[Player.getAmountToAdd()];
        UpdateHudElements();
        Window.setAtomicBoolean(true);
        Player.setScore(0);
        CalculateNextPosition(playerSprite);
        SpawnManyInRandomLocations(root);
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
        for (Object object : objectArray) {
            root.getChildren().remove(object.getSprite());
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
                        Logger.DEBUG.Log("Enemy move up", false);
                        enemy.translateYProperty().set(enemy.getTranslateY() - 20);
                    } else {
                        Logger.DEBUG.Log("Enemy move down", false);
                        enemy.translateYProperty().set(enemy.getTranslateY() + 20);
                    }
                    continue;
                }
                if (sprite.getTranslateX() != enemy.getTranslateX()) {
                    if (sprite.getTranslateX() < enemy.getTranslateX()) {
                        Logger.DEBUG.Log("Enemy move left", false);
                        enemy.translateXProperty().set(enemy.getTranslateX() - 20);
                    } else {
                        Logger.DEBUG.Log("Enemy move right", false);
                        enemy.translateXProperty().set(enemy.getTranslateX() + 20);
                    }
                }
            }
        }
    }

    public static void SpawnManyInRandomLocations(Group root) {
        for (int i = 0; i < Player.getAmountToAdd(); i++) {
            Object loopable;
            if (Object.getPowerUps()) {
                Random rand = new Random();
                if (rand.nextBoolean()) {
                    loopable = new Object(new ImageView(new Image("Sprites/anchor.png")), "anchor" + i + ".png");
                    loopable.setDoubleXP(true);
                } else {
                    loopable = new Object(new ImageView(new Image("Sprites/nether.png")), "nether" + i + ".png");
                }
            } else {
                loopable = new Object(new ImageView(new Image("Sprites/nether.png")), "nether" + i + ".png");
            }

            CalculateNextPosition(loopable.getSprite());
            Logger.DEBUG.Log("2X Power Up: " + loopable.getDoubleXP());
            objectArray[i] = loopable;
            root.getChildren().add(loopable.getSprite());
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
            SpawnManyInRandomLocations(root);
        }
        for (Object value : objectArray) {
            if (value.getTaken()) continue;
            ImageView object = value.getSprite();
            if ((player.getTranslateY() == object.getTranslateY() && player.getTranslateX() == object.getTranslateX())) {
                Logger.DEBUG.Log("\nPlayer Location: X " + player.getTranslateX() + " Y " + player.getTranslateY() +
                        ".\nObject Location: X " + object.getTranslateX() + " Y " + object.getTranslateY(), false);
                root.getChildren().remove(object);
                if (value.getDoubleXP()) {
                    Player.setScore(Player.getScore() + (2 * Player.getBasePoint()));
                } else {
                    Player.setScore(Player.getScore() + Player.getBasePoint());
                }
                value.setTaken(true);
                FindLabelById("scoreLabel").setText("Score: " + Player.getScore());
                leftOnField--;
                Logger.DEBUG.Log("Removed: " + object.getId() + ", amount left on field: " + leftOnField);
                FindLabelById("numberOfBlocksOnFieldLabel").setText("Left: " + leftOnField);
                UpdateHudElements();
                break;
            }
        }
    }

    private static Label FindLabelById(String labelId) {
        Label findLabel = Window.getLabel(labelId);
        if (findLabel == null) {
            throw new NullPointerException("Label by ID " + labelId + " not found");
        }
        Logger.DEBUG.Log("Found label: " + findLabel + ", ID: " + findLabel.getId(), false);
        return findLabel;
    }

    private static Button FindButtonById(String buttonId) {
        Button findButton = Window.getButton(buttonId);
        if (findButton == null) {
            throw new NullPointerException("Label by ID " + buttonId + " not found");
        }
        Logger.DEBUG.Log("Found label: " + findButton + ", ID: " + findButton.getId(), false);
        return findButton;
    }

    private static void UpdateHudElements() {
        FindLabelById("numberOfBlocksOnFieldLabel").setText("Left: " + Player.getAmountToAdd());
        FindLabelById("highScoreLabel").setText("High Score: " + (Player.getHighScore() < Player.getScore() ? Player.getScore() : Player.getHighScore()));
        FindLabelById("scoreLabel").setText("Score: " + Player.getScore());
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
