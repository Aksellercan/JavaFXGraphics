package com.example.JavaFXGraphics.Tools.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface LoggerQueueInterface {
    //Queue
    ConcurrentLinkedQueue<LogObject> logQueue = new ConcurrentLinkedQueue<>();
}
