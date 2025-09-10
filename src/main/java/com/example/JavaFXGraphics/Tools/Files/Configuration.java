package com.example.JavaFXGraphics.Tools.Files;

import com.example.JavaFXGraphics.Objects.Enemy;
import com.example.JavaFXGraphics.Objects.Player;
import com.example.JavaFXGraphics.Objects.Token;
import com.example.JavaFXGraphics.Tools.Logger.Logger;
import java.io.File;
import java.io.IOException;

/**
 * Abstract class to share same token array
 */
abstract class Configuration {
    /**
     * Saved Tokens array
     */
    protected static Token[] tokenConfig;
    /**
     * Configuration folder path
     */
    private static final File folderPath = new File("Config");
    /**
     * Specify keys to write when config file is not present (first time launch).
     * Add Entries here to be loaded when config file is non-existent or empty
     * @param arraySize Using getFileLength() method which returns the file line count or 0
     */
    protected static Token[] LoadKeys(int arraySize) {
        String[] keys = {
                "disable_bot",
                "amount_to_add",
                "high_score",
                "output_debug",
                "verbose_log_file",
                "player_name",
                "coloured_output",
                "show_ui",
                "enemy_speed"
        };
//        if (arraySize == 0) {
//            tokenConfig = new Token[8];
//        } else {
//            if ((arraySize < keys.length)) {
//                tokenConfig = new Token[keys.length];
//            } else {
//                tokenConfig = new Token[arraySize];
//            }
//        }
        tokenConfig = new Token[keys.length];
        for (int i = 0; i < tokenConfig.length; i++) {
            tokenConfig[i] = new Token(keys[i], "");
            Logger.DEBUG.Log(tokenConfig[i].toString());
        }
        return tokenConfig;
    }

    /*
    Add new entries here
     */
    protected static void MapKeys(boolean update) {
        for (Token token : tokenConfig) {
            switch (token.getKey().replace("\t", "")) {
                case "player_name":
                    if (update) {
                        token.setValue(Player.getName());
                        break;
                    }
                    Player.setName(token.getValue());
                    break;
                case "disable_bot":
                    if (update) {
                        token.setValue(String.valueOf(Enemy.getDisableBot()));
                        break;
                    }
                    Enemy.setDisableBot(BooleanParse(token.getValue(), false));
                    break;
                case "enemy_speed":
                    if (update) {
                        token.setValue(String.valueOf(Enemy.getSpeed()));
                        break;
                    }
                    Enemy.setSpeed(Integer.parseInt(token.getValue()));
                    break;
                case "amount_to_add":
                    if (update) {
                        token.setValue(String.valueOf(Player.getAmountToAdd()));
                        break;
                    }
                    if (Integer.parseInt(token.getValue()) == 0) {
                        Player.setAmountToAdd(Player.getAmountToAdd());
                        break;
                    }
                    Player.setAmountToAdd(Integer.parseInt(token.getValue()));
                    break;
                case "high_score":
                    if (update) {
                        token.setValue(String.valueOf(Player.getHighScore()));
                        break;
                    }
                    Player.setHighScore(Integer.parseInt(token.getValue()));
                    break;
                case "output_debug":
                    if (update) {
                        token.setValue(String.valueOf(Logger.getDebugOutput()));
                        break;
                    }
                    Logger.setDebugOutput(BooleanParse(token.getValue(), false));
                    break;
                case "show_ui":
                    if (update) {
                        token.setValue(String.valueOf(Player.getShowUI()));
                        break;
                    }
                    Player.setShowIU(BooleanParse(token.getValue(), true));
                    break;
                case "verbose_log_file":
                    if (update) {
                        token.setValue(String.valueOf(Logger.getVerboseLogFile()));
                        break;
                    }
                    Logger.setVerboseLogFile(BooleanParse(token.getValue(), false));
                    break;
                case "coloured_output":
                    if (update) {
                        token.setValue(String.valueOf(Logger.getColouredOutput()));
                        break;
                    }
                    Logger.setColouredOutput(BooleanParse(token.getValue(), false));
                    break;
            }
        }
    }

    /**
     * Checks if directory exists, if it doesn't it creates it and returns the file path
     * @param fileNameWithExtension Name of file to check and create
     * @return  Full path of the file
     * @throws IOException  If creating folder fails throws IOException
     */
    protected static File MkDirs(String fileNameWithExtension) throws IOException {
        if (!folderPath.exists()) {
            boolean status = folderPath.mkdir();
            if (status) {
                throw new IOException("Failed to create config directory");
            }
            Logger.INFO.LogSilently("Created config directory");
        }
        File filePath = new File(folderPath + File.separator + fileNameWithExtension);
        if (!filePath.exists()) {
            boolean status = filePath.createNewFile();
            if (!status) {
                throw new IOException("Failed to create config file");
            }
            Logger.INFO.LogSilently("Created config file");
        }
        return filePath;
    }

    /**
     * BooleanParse reimplementation but with default return value in case string is invalid
     * @param value String value to parse as boolean
     * @param returnValue   Default return value if string is invalid
     * @return  default value or boolean parsed
     */
    private static boolean BooleanParse(String value, boolean returnValue) {
        value = value.replace(" ", "");
        Logger.DEBUG.Log("value=" + value);
        if (value.equals("true") || value.equals("false")) {
            return value.equals("true");
        }
        if (!value.isEmpty()) Logger.ERROR.LogSilently("Key value \"" + value +"\" is not valid. Expected \"true\" or \"false\".");
        return returnValue;
    }
}
