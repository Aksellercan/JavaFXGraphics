package com.example.JavaFXGraphics.Objects;

import javafx.scene.image.ImageView;

public final class Player {
    private static String name = "default";
    private static ImageView sprite;
    private static long score = 0;
    private static int basePoint = 2;
    private static long highScore = 0;
    private static int amountToAdd = 10;
    private static boolean showIU = true;

    private Player() {}

    public static String getName() {
        return name;
    }

    public static long getScore() {
        return score;
    }

    public static int getBasePoint() {
        return basePoint;
    }

    public static ImageView getSprite() {
        return sprite;
    }

    public static long getHighScore() {
        return highScore;
    }

    public static int getAmountToAdd() {
        return amountToAdd;
    }

    public static boolean getShowUI() {
        return showIU;
    }

    public static void setName(String newName) {
        name = newName;
    }

    public static void setScore(long newScore) {
        score = newScore;
    }

    public static void setBasePoint(int newBasePoint) {
        basePoint = newBasePoint;
    }

    public static void setSprite(ImageView newSprite) {
        sprite = newSprite;
        sprite.setId("Player");
    }

    public static void setHighScore(long newHighScore) {
        highScore = newHighScore;
    }

    public static void setAmountToAdd(int newAmountToAdd) {
        amountToAdd = newAmountToAdd;
    }

    public static void setShowIU(boolean newShowUI) {
        showIU = newShowUI;
    }
}
