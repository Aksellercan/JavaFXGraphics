package com.example.JavaFXGraphics.Objects;

import javafx.scene.image.ImageView;

public final class Player {
    private static ImageView sprite;
    private static int highScore = 0;
    private static int amountToAdd = 10;
    private static boolean disableBot = false;
    private static boolean showIU = true;

    private Player() {}

    public static ImageView getSprite() {
        return sprite;
    }

    public static int getHighScore() {
        return highScore;
    }

    public static int getAmountToAdd() {
        return amountToAdd;
    }

    public static boolean getDisableBot() {
        return disableBot;
    }

    public static boolean getShowUI() {
        return showIU;
    }

    public static void setSprite(ImageView newSprite) {
        sprite = newSprite;
        sprite.setId("Player");
    }

    public static void setHighScore(int newHighScore) {
        highScore = newHighScore;
    }

    public static void setAmountToAdd(int newAmountToAdd) {
        amountToAdd = newAmountToAdd;
    }

    public static void setDisableBot(boolean newDisableBot) {
        disableBot = newDisableBot;
    }

    public static void setShowIU(boolean newShowUI) {
        showIU = newShowUI;
    }
}
