package codes.nibby.autopi.ui;

import codes.nibby.autopi.Main;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorScheme {

//    // Main action buttons
//    public static final Color PRIMARY_UI_BASE = new Color(80, 150, 68);
//    public static final Color PRIMARY_HOVER = new Color(131, 197, 98);
//    public static final Color PRIMARY_UI_FOREGROUND = new Color(255, 255, 255);
//
//    // Standard button background and foreground
//    public static final Color SECONDARY_UI_BASE = new Color(231, 231, 231);
//    public static final Color SECONDARY_UI_HOVER = new Color(240, 240, 240);
//    public static final Color SECONDARY_UI_FOREGROUND = new Color(35, 35, 35);
//
//    // Text Field
//    public static final Color TEXTFIELD_UI_BASE = new Color(200, 200, 200);
//    public static final Color TEXTFIELD_UI_FOCUS = new Color(231, 231, 231);
//    public static final Color TEXTFIELD_UI_GREEN = new Color(131, 197, 98);
//    public static final Color TEXTFIELD_UI_RED = new Color(215, 111, 90);
//
//    // Toggle Buttons
//    public static final Color TOGGLED_UI_BASE = new Color(170, 230, 255);
//    public static final Color TOGGLED_UI_HOVER = new Color(173, 220, 229);
//    public static final Color TOGGLED_UI_FOREGROUND = new Color(0, 0, 0);
//
//    // Default screen background and foreground
//    public static final Color PRIMARY_FOREGROUND = new Color(50, 50, 50);
//    public static final Color PRIMARY_BACKGROUND = new Color(255, 255, 255);

    private static final Map<String, Color> PRESETS = new HashMap<>();

    static {
        { // Light mode
            // Main "action" button
            PRESETS.put("light.primary.standard", new Color(80, 150, 68));
            PRESETS.put("light.primary.lighter", new Color(131, 197, 98));
            PRESETS.put("light.primary.button.foreground", new Color(255, 255, 255));
            // Default screen background and foreground
            PRESETS.put("light.primary.foreground", new Color(0, 0, 0));
            PRESETS.put("light.primary.background", new Color(255, 255, 255));

            // Standard button background and foreground
            PRESETS.put("light.secondary.standard", new Color(231, 231, 231));
            PRESETS.put("light.secondary.lighter", new Color(244, 244, 244));
            PRESETS.put("light.secondary.foreground", new Color(35, 35, 35));

            // Text Field
            PRESETS.put("light.textfield.base", new Color(200, 200, 200));
            PRESETS.put("light.textfield.focus", new Color(231, 231, 231));

            // Toggle Buttons
            PRESETS.put("light.toggle.base", new Color(170, 230, 255));
            PRESETS.put("light.toggle.lighter", new Color(173, 235, 255));
            PRESETS.put("light.toggle.foreground", new Color(0, 0, 0));

            PRESETS.put("light.stage.ripple", new Color(133, 188, 229));
        }

        { // Dark mode
            PRESETS.put("dark.primary.standard", new Color(212, 155, 0));
            PRESETS.put("dark.primary.lighter", new Color(255, 187, 0));
            PRESETS.put("dark.primary.button.foreground", new Color(0, 0, 0));
            // Default screen background and foreground
            PRESETS.put("dark.primary.foreground", new Color(240, 240, 240));
            PRESETS.put("dark.primary.background", new Color(35, 35, 35));

            // Standard button background and foreground
            PRESETS.put("dark.secondary.standard", new Color(156, 156, 156));
            PRESETS.put("dark.secondary.lighter", new Color(176, 176, 176));
            PRESETS.put("dark.secondary.foreground", new Color(240, 240, 240));

            // Text Field
            PRESETS.put("dark.textfield.base", new Color(200, 200, 200));
            PRESETS.put("dark.textfield.focus", new Color(231, 231, 231));

            // Toggle Buttons
            PRESETS.put("dark.toggle.base", new Color(170, 230, 255));
            PRESETS.put("dark.toggle.lighter", new Color(173, 235, 255));
            PRESETS.put("dark.toggle.foreground", new Color(0, 0, 0));

            PRESETS.put("dark.stage.ripple", new Color(255, 187, 0));
        }
    }

    public static Color get(String key) {
        boolean darkMode = Main.getApp().isDarkMode();

        return PRESETS.get((darkMode ? "dark" : "light") + "." + key);
    }

}
