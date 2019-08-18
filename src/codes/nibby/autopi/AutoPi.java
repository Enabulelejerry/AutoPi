package codes.nibby.autopi;

import codes.nibby.autopi.screen.Stage;
import codes.nibby.autopi.ui.InputHandler;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;

public class AutoPi extends Canvas implements Runnable {

    public static final String APP_TITLE = "AutoPi";
    public static int APP_WIDTH = 0, APP_HEIGHT = 0;
    public static int TARGET_FPS = 30;
    public static final AppConfig config = new AppConfig();

    private volatile boolean running = false;
    private Thread thread;

    private InputHandler input;
    private Graphics2D graphics;
    private VolatileImage screenImage;
    private Stage stage;

    static {
        config.load();
    }

    AutoPi() {
        input = new InputHandler();
        addMouseListener(input);
        addMouseMotionListener(input);
        addKeyListener(input);

        APP_WIDTH = config.getInt(AppConfig.Key.DISPLAY_WIDTH);
        APP_HEIGHT = config.getInt(AppConfig.Key.DISPLAY_HEIGHT);
        Dimension size = new Dimension(APP_WIDTH, APP_HEIGHT);
        setSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
    }

    private void initialize() {
        stage = new Stage();

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
        TARGET_FPS = config.getInt(AppConfig.Key.TARGET_FPS);
        double nsPerTick = 1000000000.0d / TARGET_FPS;
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
            // TODO change this later
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, getWidth(), getHeight());
            stage.render(graphics);

            BufferStrategy strategy = getBufferStrategy();
            if (strategy != null) {
                Graphics _g = strategy.getDrawGraphics();
                _g.drawImage(screenImage, 0, 0, null);
                strategy.show();
            }
        }
    }

    private void update() {
        stage.update(input);
        input.update();
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

    public Graphics2D getDrawGraphics() {
        return graphics;
    }

    public InputHandler getInputHandler() {
        return input;
    }
}
