package com.example.JavaFXGraphics.Tools;

import java.io.*;
import java.util.HashMap;

public final class Configuration {
    private static final File folderPath = new File("Config");
    private static final File filePath = new File(folderPath + File.separator + "config.json");
    private static final HashMap<String, String> configMap = new HashMap<>();
    private static int high_Score;

    private Configuration() {}

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

    public static int GetHigh_Score() {
        return high_Score;
    }

    public static void SetHigh_Score(int set_high_Score) {
        high_Score = set_high_Score;
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

    public static void ReadConfig() {
        int lineCount = 0;
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
                    FormatLine(lineCount, line, splitLine);
                } else {
                    Logger.ERROR.LogSilently("Invalid line at " + lineCount + ": \"" + line + "\", expected format: \"key: value\". Continue reading the file.");
                }

            }
            for (String key : configMap.keySet()) {
                UpdateConfig(key, false);
            }
        } catch (Exception e) {
            Logger.ERROR.LogException(e);
        }
    }

    private static void FormatLine(int lineNumber, String line, String[] splitLine) {
        String key = splitLine[0].trim();
        if (!configMap.containsKey(key)) {
            Logger.ERROR.LogSilently("Invalid line at " + lineNumber + ": \"" + line + "\", Key \"" + key + "\" not found. Continue reading the file.");
            return;
        }
        String value = splitLine[1].trim();
        if (value.isEmpty()) Logger.ERROR.LogSilently("Invalid line at " + lineNumber + ":" + " Value of key \"" + key +"\" cannot be empty.");
        configMap.put(key, value);
    }

    public static void WriteConfig() {
        MkDirs();
        try(FileWriter fw = new FileWriter(filePath, false)) {
            fw.write("{\n");
            int hashMapSize = configMap.size();
            int currentElement = 0;
            for (String key : configMap.keySet()) {
                UpdateConfig(key, true);
                if(configMap.get(key) == null || configMap.get(key).isEmpty()) continue;
                Logger.DEBUG.Log("Key \"" + key + "\", Value \"" + configMap.get(key) + "\"", false);
                if (hashMapSize == 1) {
                    fw.write("\t\"" + key + "\"" + ": \"" + configMap.get(key) + "\"\n");
                    break;
                }
                if ((currentElement+1) == hashMapSize) {
                    fw.write("\t\"" + key + "\"" + ": \"" + configMap.get(key) + "\"\n");
                } else {
                    fw.write("\t\"" + key + "\"" + ": \"" + configMap.get(key) + "\",\n");
                    currentElement++;
                }
            }
            fw.write("}");
        } catch (Exception e) {
            Logger.ERROR.LogException(e);
        }
    }

    private static boolean BooleanParse(String value, boolean returnValue) {
        if (value.equals("true") || value.equals("false")) {
            return value.equals("true");
        }
        if (!value.isEmpty()) Logger.ERROR.LogSilently("Key value \"" + value +"\" is not valid. Expected \"true\" or \"false\".");
        return returnValue;
    }

    private static void UpdateConfig(String key, boolean update) {
        switch (key) {
            case "High_Score":
                if (update) {
                    configMap.put(key, String.valueOf(GetHigh_Score()));
                    break;
                }
                SetHigh_Score(Integer.parseInt(configMap.get(key)));
                break;
            case "output_debug":
                if (update) {
                    configMap.put(key, String.valueOf(Logger.getDebugOutput()));
                    break;
                }
                Logger.setDebugOutput(BooleanParse(configMap.get(key), false));
                break;
            case "verbose_log_file":
                if (update) {
                    configMap.put(key, String.valueOf(Logger.getVerboseLogFile()));
                    break;
                }
                Logger.setVerboseLogFile(BooleanParse(configMap.get(key), false));
                break;
            case "coloured_output":
                if (update) {
                    configMap.put(key, String.valueOf(Logger.getColouredOutput()));
                    break;
                }
                Logger.setColouredOutput(BooleanParse(configMap.get(key), false));
                break;
        }
    }

    public static void AddToConfigMap(String... keys) {
        if (!configMap.isEmpty()) {
            return;
        }
        for (String key : keys) {
            configMap.put(key, "");
        }
    }
}
