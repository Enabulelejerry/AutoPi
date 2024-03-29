package codes.nibby.autopi;

import javax.swing.*;
import java.awt.*;

import static codes.nibby.autopi.AppConfig.*;

public class Main  {

    private static AutoPi app;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                app = new AutoPi();
                boolean debug = AutoPi.config.getBoolean(Key.DEBUG_MODE);

                JFrame frame = new JFrame();
                frame.setUndecorated(!debug);
                frame.setTitle(AutoPi.APP_TITLE);
                frame.getContentPane().add(app);
                frame.setResizable(false);
                frame.pack();
                if (!debug) {
                    frame.setAlwaysOnTop(true);
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } else {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLocationRelativeTo(null);
                }
                frame.setVisible(true);

                app.start();
            }
        });
    }

    public static AutoPi getApp() {
        return app;
    }
}
