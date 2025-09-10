package com.example.JavaFXGraphics.Graphics;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.control.Label;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Window {
    private static Stage primaryStage;
    private static Group root;
    private static final ArrayList<Label> labelList = new ArrayList<>();
    private static final ArrayList<Button> buttonList = new ArrayList<>();
    private static final AtomicBoolean atomicBoolean = new AtomicBoolean();

    private Window() {}

    public static void setRoot(Group newRoot) {
        root = newRoot;
    }

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

    public static Group getRoot() {
        return root;
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

    public static Button getButton(String buttonId) {
        for (Button button : buttonList) {
            if (button.getId().equals(buttonId)) {
                return button;
            }
        }
        return null;
    }

    public static void removeButton(String buttonId) {
        for (int i = 0; i < buttonList.size(); i++) {
            if (buttonList.get(i).getId().equals(buttonId)) {
                buttonList.remove(i);
                return;
            }
        }
    }

    public static void AddButtonToList(Button newButton) {
        buttonList.add(newButton);
    }
}
