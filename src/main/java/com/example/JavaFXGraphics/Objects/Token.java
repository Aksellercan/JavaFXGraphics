package com.example.JavaFXGraphics.Objects;

import com.example.JavaFXGraphics.Tools.Logger.Logger;

public class Token {
    private String key;
    private String value;
    private boolean isNumber;
    private boolean isBoolean;
    private Token[] innerArray;

    public Token(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //getters
    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public Token[] getInnerArray() {
        return this.innerArray;
    }

    public boolean getIsNumber() {
        if (this.isBoolean) {
            return false;
        }
        return this.isNumber;
    }

    public boolean getIsBoolean() {
        if (this.isNumber) {
            return false;
        }
        return this.isBoolean;
    }

    //setters
    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setInnerArray(Token[] innerArray) {
        this.innerArray = innerArray;
    }

    public void addTokenToInnerArray(Token newInnerToken) {
        if (innerArray.length == 0) {
            Logger.ERROR.Log("Inner array not initialized");
            return;
        }
        innerArray[innerArray.length-1] = newInnerToken;
    }

    public void setBoolean(boolean isBoolean) {
        this.isBoolean = isBoolean;
    }

    public void setNumber(boolean isNumber) {
        this.isNumber = isNumber;
    }

    @Override
    public String toString() {
        return "Key: " + this.key + ", Value: " + this.value + ". States: isBoolean: " + this.isBoolean + ", isNumber: " + this.isNumber;
    }
}
