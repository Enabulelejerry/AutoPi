package codes.nibby.autopi;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AppConfig {

    private static final String CONFIG_FILE = "config.txt";
    private Map<String, String> configMap;

    public enum Key {
        DEBUG_MODE,
        DISPLAY_WIDTH,
        DISPLAY_HEIGHT,
        TARGET_FPS,
        BUFFER_SIZE,
        HIDE_CURSOR
    }

    public AppConfig() {
        configMap = new HashMap<>();
    }

    /*
        Loads contents from config.txt
     */
    public void load() {
        try (Scanner scanner = new Scanner(Paths.get(CONFIG_FILE))) {
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.startsWith("#") || line.isEmpty())
                    continue;

                String[] data = line.split("=");

                String key = data[0];
                String value = data[1];

                put(key, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "", AutoPi.APP_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void put(String key, String value) {
        configMap.put(key, value);
    }

    public String get(Key key) {
        return configMap.get(key.name());
    }

    public boolean getBoolean(Key key) {
        return Boolean.parseBoolean(get(key));
    }

    public int getInt(Key key) {
        return Integer.parseInt(get(key));
    }
}
