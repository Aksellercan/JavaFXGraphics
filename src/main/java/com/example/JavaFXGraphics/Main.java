package com.example.JavaFXGraphics;

import com.example.JavaFXGraphics.Graphics.Graphics;
import com.example.JavaFXGraphics.Tools.Logger.LoggerBackend;

public class Main {
    public static void main(String[] args) {
        Thread loggerThread = new Thread(new LoggerBackend());
        loggerThread.setDaemon(true);
        loggerThread.start();
        Graphics.launch(Graphics.class, args);
    }
}
