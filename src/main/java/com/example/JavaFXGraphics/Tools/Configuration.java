package com.example.JavaFXGraphics.Tools;

import com.example.JavaFXGraphics.Objects.Player;
import com.example.JavaFXGraphics.Objects.Token;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Configuration {
    private static final File folderPath = new File("Config");
    private static final File filePath = new File(folderPath + File.separator + "config.json");
    private static Token[] tokenConfig;

    private Configuration() {}

    private static void LoadKeys() {
        String[] keys = {
                "disable_bot",
                "amount_to_add",
                "high_score",
                "output_debug",
                "verbose_log_file",
                "coloured_output",
                "show_ui"
        };
        for (int i = 0; i < tokenConfig.length; i++) {
            tokenConfig[i] = new Token(keys[i], "");
        }
    }

    /*
    Interact with these only
     */
    public static void MapAndWriteConfig() {
        MapKeys(true);
        WriteConfig();
    }

    public static void ReadConfigAndMap() {
        int arraySize = getFileLength();
        if (arraySize == -1 || arraySize == 0) {
            tokenConfig = new Token[7];
        } else {
            tokenConfig = new Token[arraySize-2];
        }
        LoadKeys();
        ReadConfig();
        MapKeys(arraySize == -1 || arraySize == 0);
    }

    private static void MkDirs()  {
        try {
            if (!folderPath.exists()) {
                boolean status = folderPath.mkdir();
                if (status) {
                    Logger.INFO.LogSilently("Created config directory");
                }
            }
            if (!filePath.exists()) {
                boolean status = filePath.createNewFile();
                if (status) {
                    Logger.INFO.LogSilently("Created config file");
                }
            }
        } catch (Exception e) {
            Logger.ERROR.LogException(e);
        }
    }

    private static String[] RemoveQuotes(String key, String value) {
        StringBuilder valueMutable = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) == '"') {
                continue;
            }
            valueMutable.append(key.charAt(i));
        }
        StringBuilder keyMutable = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '"' || value.charAt(i) == ',') {
                continue;
            }
            keyMutable.append(value.charAt(i));
        }
        return new String[] {valueMutable.toString(), keyMutable.toString()};
    }

    private static String PrintCorrectType(Token current) {
        if (current.getIsNumber() || current.getIsBoolean()) {
            // 2 spaces instead of tabs
            return "  \"" + current.getKey() + "\"" + ": " + current.getValue();
        }
        // 2 spaces instead of tabs
        return "  \"" + current.getKey() + "\"" + ": \"" + current.getValue() + "\"";
    }

    /*
    Add new entries here
     */
    private static void MapKeys(boolean update) {
        for (int i = 0; i < tokenConfig.length; i++) {
            tokenConfig[i] = TokenTypeCheck(tokenConfig[i]);
            Logger.DEBUG.Log("Current: " + tokenConfig[i].toString());
            switch (tokenConfig[i].getKey().replace("\t", "")) {
                case "disable_bot":
                    if (update) {
                        tokenConfig[i].setValue(String.valueOf(Player.getDisableBot()));
                        break;
                    }
                    Player.setDisableBot(BooleanParse(tokenConfig[i].getValue(), false));
                    break;
                case "amount_to_add":
                    if (update) {
                        tokenConfig[i].setValue(String.valueOf(Player.getAmountToAdd()));
                        break;
                    }
                    if (Integer.parseInt(tokenConfig[i].getValue()) == 0) {
                        Player.setAmountToAdd(Player.getAmountToAdd());
                        break;
                    }
                    Player.setAmountToAdd(Integer.parseInt(tokenConfig[i].getValue()));
                    break;
                case "high_score":
                    if (update) {
                        tokenConfig[i].setValue(String.valueOf(Player.getHighScore()));
                        break;
                    }
                    Player.setHighScore(Integer.parseInt(tokenConfig[i].getValue()));
                    break;
                case "output_debug":
                    if (update) {
                        tokenConfig[i].setValue(String.valueOf(Logger.getDebugOutput()));
                        break;
                    }
                    Logger.setDebugOutput(BooleanParse(tokenConfig[i].getValue(), false));
                    break;
                case "show_ui":
                    if (update) {
                        tokenConfig[i].setValue(String.valueOf(Player.getShowUI()));
                        break;
                    }
                    Player.setShowIU(BooleanParse(tokenConfig[i].getValue(), true));
                    break;
                case "verbose_log_file":
                    if (update) {
                        tokenConfig[i].setValue(String.valueOf(Logger.getVerboseLogFile()));
                        break;
                    }
                    Logger.setVerboseLogFile(BooleanParse(tokenConfig[i].getValue(), false));
                    break;
                case "coloured_output":
                    if (update) {
                        tokenConfig[i].setValue(String.valueOf(Logger.getColouredOutput()));
                        break;
                    }
                    Logger.setColouredOutput(BooleanParse(tokenConfig[i].getValue(), false));
                    break;
            }
        }
    }

    private static int getFileLength() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int fileLength = 0;
            while (br.readLine() !=null ) {
                fileLength++;
            }
            return fileLength;
        } catch (Exception e) {
            Logger.CRITICAL.LogException(e, "Unable to get the length of file");
            return -1;
        }
    }

    private static boolean BooleanParse(String value, boolean returnValue) {
        value = value.replace(" ", "");
        Logger.DEBUG.Log("value=" + value);
        if (value.equals("true") || value.equals("false")) {
            return value.equals("true");
        }
        if (!value.isEmpty()) Logger.ERROR.LogSilently("Key value \"" + value +"\" is not valid. Expected \"true\" or \"false\".");
        return returnValue;
    }

    private static void WriteConfig() {
        MkDirs();
        try(FileWriter fw = new FileWriter(filePath, false)) {
            fw.write("{\n");
            int length = tokenConfig.length;
            int currentElement = 0;
            for (Token current : tokenConfig) {
                Logger.DEBUG.Log("Key \"" + current.getKey() + "\", Value \"" + current.getValue() + "\"", true);
                if ((currentElement+1) == length) {
                    fw.write(PrintCorrectType(current) + "\n");
                } else {
                    fw.write(PrintCorrectType(current) + ",\n");
                    currentElement++;
                }
            }
            fw.write("}");
        } catch (Exception e) {
            Logger.ERROR.LogException(e);
        }
    }

    private static void ReadConfig() {
        int lineCount = 0;
        int tokenCount = 0;
        MkDirs();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] splitLine;
            while ((line = br.readLine()) != null) {
                lineCount++;
                String[] ignoreList = {"{", "}", "[", "]"};
                boolean skip = false;
                for (String ignore : ignoreList) {
                    if (line.equals(ignore)) {
                        skip = true;
                        break;
                    }
                }
                if (skip) continue;
                splitLine = line.split(":",2);
                if (splitLine.length == 2) {
                    splitLine = RemoveQuotes(splitLine[0], splitLine[1]);
                    Logger.DEBUG.Log("Before check: key =" + splitLine[0] + " value =" +splitLine[1]);
                    tokenConfig[tokenCount] = TokenTypeCheck(new Token(splitLine[0].trim(), splitLine[1].trim()));
                    Logger.DEBUG.Log(tokenConfig[tokenCount].toString());
                    tokenCount++;
                } else {
                    Logger.ERROR.LogSilently("Invalid line at " + lineCount + ": \"" + line + "\", expected format: \"key: value\". Continue reading the file.");
                }
            }
        } catch (Exception e) {
            Logger.ERROR.LogException(e);
        }
    }

    private static boolean CheckBoolean(String value) {
        return (
                value.replace(" ", "").equalsIgnoreCase("true")
                        ||
                        value.replace(" ", "").equalsIgnoreCase("false")
        );
    }

    private static Token TokenTypeCheck(Token current) {
        if (CheckBoolean(current.getValue())) {
            current.setBoolean(true);
        }
        Pattern pattern= Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(current.getValue());
        if (matcher.find()) {
            current.setNumber(true);
        }
        Logger.DEBUG.Log("isBoolean is " + current.getIsBoolean() + " isNumber is " + current.getIsNumber());
        return current;
    }
}