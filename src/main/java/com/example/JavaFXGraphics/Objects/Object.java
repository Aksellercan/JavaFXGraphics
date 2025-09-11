package com.example.JavaFXGraphics.Objects;

import javafx.scene.image.ImageView;

public class Object {
    private ImageView sprite;
    private boolean doubleXP;

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

    public boolean getDoubleXP() {
        return this.doubleXP;
    }

    public void setDoubleXP(boolean doubleXP) {
        this.doubleXP = doubleXP;
    }
}
