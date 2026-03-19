package com.example.JavaFXGraphics.Tools.Logger;

public interface LoggerFormatInterface {
    String dateSeverityFormat(LogObject log);
    void colourOutput(LogObject log, String fullMessage);
}
