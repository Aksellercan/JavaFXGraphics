package com.example.JavaFXGraphics.Objects;

import javafx.scene.image.ImageView;

public class Enemy {
    private ImageView sprite;
    private static int speed = 180;
    private static boolean disableBot = false;

    public Enemy(ImageView sprite) {
        this.sprite = sprite;
    }

    public ImageView getSprite() {
        return this.sprite;
    }

    public void setSprite() {
        this.sprite = sprite;
    }

    public static int getSpeed() {
        return speed;
    }

    public static boolean getDisableBot() {
        return disableBot;
    }

    public static void setSpeed(int newSpeed) {
        speed = newSpeed;
    }

    public static void setDisableBot(boolean newBool) {
        disableBot = newBool;
    }
}
