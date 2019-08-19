package codes.nibby.autopi.screen;

import codes.nibby.autopi.AutoPi;
import codes.nibby.autopi.Main;
import codes.nibby.autopi.asset.Asset;
import codes.nibby.autopi.asset.Assets;
import codes.nibby.autopi.asset.Fonts;
import codes.nibby.autopi.ui.Button;
import codes.nibby.autopi.ui.ColorScheme;
import codes.nibby.autopi.ui.InputHandler;
import codes.nibby.autopi.ui.animation.ScreenScrollType;

import java.awt.*;
import java.util.Queue;

public class LoadingScreen extends Screen {

    private long startTime = -1;
    private boolean doneLoading = false;
    private boolean doneTransition = false;
    private int initSize = -1;
    private int percent;

    public void render(Graphics2D g) {
        // Draws progress text
        g.setColor(ColorScheme.get("primary.foreground"));
        g.setFont(Fonts.STANDARD_RALEWAY);

        String text = "Loading... (" + percent + "%)";

        FontMetrics metrics = g.getFontMetrics();
        int x = AutoPi.APP_WIDTH / 2 - metrics.stringWidth(text) / 2 + getDrawOffsetY();
        int y = AutoPi.APP_HEIGHT / 2 - metrics.getHeight() / 2 + getDrawOffsetX();
        g.drawString(text, x, y);
    }

    @Override
    public void update(InputHandler input) {
        super.update(input);

        if(startTime == -1) {
            startTime = System.currentTimeMillis();
        }

        if(!doneLoading && System.currentTimeMillis() - startTime > 1000) {
            Queue<Asset> assetQueue = Assets.getLoadQueue();
            if(initSize == -1)
                initSize = assetQueue.size();

            if (assetQueue.size() > 0) {
                percent = (int) (((float) initSize - (float) assetQueue.size() - 1) / (float) initSize * 100.0f);
                Asset asset = assetQueue.poll();
                asset.load();
            } else {
                doneLoading = true;
                startTime = System.currentTimeMillis();
            }
        }

        if(!doneTransition && doneLoading && System.currentTimeMillis() - startTime > 500) {
            Main.getApp().performScreenTransition(ScreenScrollType.NO_SCROLL, new SplashScreen());

            doneTransition = true;
        }
    }

    @Override
    public void buttonClicked(Button b) {

    }

    @Override
    public void buttonHovered(Button b) {

    }
}
