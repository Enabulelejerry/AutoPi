package codes.nibby.autopi.asset;

import codes.nibby.autopi.AutoPi;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Fonts {

    public static Font BASE_RALEWAY;
    public static Font STANDARD_RALEWAY;
    public static Font BOLD_RALEWAY;
    public static Font HEADER_RALEWAY;

    private static boolean loaded = false;

    public static final void load() {
        if (loaded)
            return;

        try {
            InputStream stream = Fonts.class.getResourceAsStream("/fonts/Raleway-Light.ttf");
            BASE_RALEWAY = Font.createFont(Font.TRUETYPE_FONT, stream);
            STANDARD_RALEWAY = derive(BASE_RALEWAY, Font.PLAIN, 24f);
            BOLD_RALEWAY = derive(STANDARD_RALEWAY, Font.BOLD, 24f);
            stream.close();

            stream = Fonts.class.getResourceAsStream("/fonts/Raleway-Thin.ttf");
            Font base = Font.createFont(Font.TRUETYPE_FONT, stream);
            HEADER_RALEWAY = derive(base, Font.PLAIN, 64f);
            stream.close();
            loaded = true;
        } catch (FontFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while deriving application fonts.",
                    AutoPi.APP_TITLE, JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An IO error occurred while deriving application fonts.",
                    AutoPi.APP_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    public static final Font derive(Font base, int fontStyle) {
        return base.deriveFont(fontStyle, base.getSize());
    }

    public static final Font derive(Font base, int fontStyle, float fontSize) {
        return base.deriveFont(fontStyle, fontSize);
    }
}
