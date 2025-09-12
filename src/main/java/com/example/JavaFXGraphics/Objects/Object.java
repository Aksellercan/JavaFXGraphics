package com.example.JavaFXGraphics.Objects;

import javafx.scene.image.ImageView;

public class Object {
    private ImageView sprite;
    private boolean doubleXP;
    private boolean taken;
    private static boolean powerUps = true;

    public Object(ImageView sprite, String id) {
        this.sprite = sprite;
        this.sprite.setId(id);
    }

    public ImageView getSprite() {
        return this.sprite;
    }

    public void setSprite(ImageView sprite) {
        this.sprite = sprite;
    }

    public boolean getTaken() {
        return this.taken;
    }

    public static boolean getPowerUps() {
        return powerUps;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public static void setPowerUps(boolean newPowerUps) {
        powerUps = newPowerUps;
    }

    public boolean getDoubleXP() {
        return this.doubleXP;
    }

    public void setDoubleXP(boolean doubleXP) {
        this.doubleXP = doubleXP;
    }
}
