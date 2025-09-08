package com.example.JavaFXGraphics.Graphics;

import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.control.Label;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Window {
    private static Stage primaryStage;
    private static final ArrayList<Label> labelList = new ArrayList<>();
    private static final AtomicBoolean atomicBoolean = new AtomicBoolean();

    private Window() {}

    public static void AddLabelToList(Label newLabel) {
        labelList.add(newLabel);
    }

    public static void setAtomicBoolean(boolean value) {
        atomicBoolean.set(value);
    }

    public static void setPrimaryStage(Stage newPrimaryStage) {
        primaryStage = newPrimaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static boolean getAtomicBoolean() {
        return atomicBoolean.get();
    }

    public static ArrayList<Label> getLabelList() {
        return labelList;
    }

    public static Label getLabel(String labelId) {
        for (Label label : labelList) {
            if (label.getId().equals(labelId)) {
                return label;
            }
        }
        return null;
    }

    public static void removeLabel(String labelId) {
        for (int i = 0; i < labelList.size(); i++) {
            if (labelList.get(i).getId().equals(labelId)) {
                labelList.remove(i);
                return;
            }
        }
    }
}
