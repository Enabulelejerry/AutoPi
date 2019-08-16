package codes.nibby.autopi;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;

public class AutoPi extends Canvas implements Runnable {

    public static final String APP_TITLE = "AutoPi";
    public static final AppConfig config = new AppConfig();

    private volatile boolean running = false;
    private Thread thread;

    private Graphics2D graphics;
    private VolatileImage screenImage;

    static {
        config.load();
    }

    AutoPi() {
        int appWidth = config.getInt(AppConfig.Key.DISPLAY_WIDTH);
        int appHeight = config.getInt(AppConfig.Key.DISPLAY_HEIGHT);
        Dimension size = new Dimension(appWidth, appHeight);
        setSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
    }

    private void initialize() {

        BufferStrategy strategy = getBufferStrategy();
        if (strategy == null) {
            createBufferStrategy(config.getInt(AppConfig.Key.BUFFER_SIZE));
            requestFocus();
        }

        if (screenImage == null) {
            int appWidth = config.getInt(AppConfig.Key.DISPLAY_WIDTH);
            int appHeight = config.getInt(AppConfig.Key.DISPLAY_HEIGHT);
            screenImage = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration()
                    .createCompatibleVolatileImage(appWidth, appHeight, VolatileImage.TRANSLUCENT);
        }
    }

    @Override
    public void run() {
        initialize();

        int fps = 0;
        long fpsTimer = System.currentTimeMillis();
        int targetFps = config.getInt(AppConfig.Key.TARGET_FPS);
        double nsPerTick = 1000000000.0d / targetFps;
        long lastUpdateTime = System.nanoTime();
        double delta = 0d;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastUpdateTime) / nsPerTick;
            lastUpdateTime = now;
            boolean shouldRender = false;

            while (delta >= 1) {
                shouldRender = true;
                update();
                delta--;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (shouldRender) {
                fps++;
                render();
            }

            if (System.currentTimeMillis() - fpsTimer > 1000) {
                if (config.getBoolean(AppConfig.Key.DEBUG_MODE)) {
                    System.out.printf("%d FPS%n", fps);
                    fps = 0;
                    fpsTimer += 1000;
                }
            }
        }
    }

    private void render() {
        if (screenImage != null) {
            this.graphics = screenImage.createGraphics();

            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, getWidth(), getHeight());

            BufferStrategy strategy = getBufferStrategy();
            if (strategy != null) {
                Graphics _g = strategy.getDrawGraphics();
                _g.drawImage(screenImage, 0, 0, null);
                strategy.show();
            }
        }
    }

    private void update() {

    }

    public void start() {
        if (!running) {
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (running) {
            running = false;
        }
    }
}
