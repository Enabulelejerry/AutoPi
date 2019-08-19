package codes.nibby.autopi;

import codes.nibby.autopi.asset.Fonts;
import codes.nibby.autopi.screen.LoadingScreen;
import codes.nibby.autopi.screen.Screen;
import codes.nibby.autopi.screen.SplashScreen;
import codes.nibby.autopi.screen.Stage;
import codes.nibby.autopi.ui.ColorScheme;
import codes.nibby.autopi.ui.InputHandler;
import codes.nibby.autopi.ui.animation.ScreenScrollType;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.util.Stack;

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

    /** Screen list. Only the top screen will be shown and updated. */
    private Stack<Screen> screens = new Stack<>();

    /** Transition type flag. */
    private ScreenScrollType screenScrollType;

    /** Target transition screen. */
    private Screen transitScreen;

    /** Transition state flag */
    private boolean transition;

    /** Transition animation counter */
    private int transitionPhase;

    /** Alternative color scheme, automatically switched if night time. */
    private boolean darkMode = false;

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
        setCursor(null);
    }

    private void initialize() {
        Fonts.load();
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

        pushScreen(new LoadingScreen());
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
            graphics.setColor(ColorScheme.get("primary.background"));
            graphics.fillRect(0, 0, getWidth(), getHeight());
            stage.render(graphics);

            // If animated, draw the old screen as well
            if(transition && transitionPhase == 1) {
                if (screens.size() > 1) {
                    Screen old = screens.get(screens.size() - 2);
                    old.render(graphics);
                }
            }

            // Draw the top-most level screen
            getCurrentScreen().render(graphics);

            BufferStrategy strategy = getBufferStrategy();
            if (strategy != null) {
                Graphics _g = strategy.getDrawGraphics();
                _g.drawImage(screenImage, 0, 0, null);
                strategy.show();
            }
        }
    }

    private void update() {
        if (input.isKeyPressed(KeyEvent.VK_SPACE)) {
            setDarkMode(!isDarkMode());
        }
        stage.update(input);
        getCurrentScreen().update(input);

        /*
            Updates the scene sliding animation when there is a transition.
            Each phase of the animation is determined by the `transitionPhase` counter.

            Counter:    Phase:
            0           Phase out, wait for all transition-out animations to be played in old scene.
            1           Slide: polls new screen from a chosen direction.
            2           Finalize: Trims offsets and resets all transition variables.
         */
        if(transition) {
            switch(transitionPhase) {
                case 0: // Await for exit animation to complete

                    // This flag returns true when all components have finished transition-out
                    // animation in the old scene.
                    if(getCurrentScreen().canTransitOut()) {
                        if(transitScreen == null) {
                            //Pop to previous screen
                            Screen previous = screens.get(screens.size() - 2);
                            previous.setDrawOffset(screenScrollType.getXShift() * APP_WIDTH, screenScrollType.getYShift() * APP_HEIGHT);
                        } else {
                            //Add new screen into stack
                            transitScreen.setDrawOffset(screenScrollType.getXShift() * APP_WIDTH, screenScrollType.getYShift() * APP_HEIGHT);
                            pushScreen(transitScreen);
                        }

                        // Adjust animated backdrop offset
                        stage.pan(screenScrollType);
                        transitionPhase++;
                    }
                    break;

                /*
                    Transition animation is done here. The newly added scene is assigned an offset of
                    -WIDTH, 0 (poll from left),
                     WIDTH, 0 (poll from right),
                     0, HEIGHT (poll from below),
                     0, -HEIGHT (poll from above)

                     For each update tick (60 in one second), the old scene will be pulled into the
                     direction as opposed to the new screen, giving a 'slide' effect.
                 */
                case 1:
                    // Identify the screen to transition into
                    Screen target = (transitScreen != null) ? transitScreen : screens.get(screens.size() - 2);
                    target.update(input);

                    // Calculate the slide speed using a linear interpolation algorithm
                    float xVelocity = target.getDrawOffsetX() / 8f;
                    float yVelocity = target.getDrawOffsetY() / 8f;

                    // Find the new offset of the new screen after this update
                    float newOffsetX = (int) (target.getDrawOffsetX() + xVelocity);
                    float newOffsetY = (int) (target.getDrawOffsetY() + yVelocity);

                    target.setDrawOffset((int) newOffsetX, (int) newOffsetY);

                    // Identify the screen to move away from
                    Screen old = (transitScreen != null) ? screens.get(screens.size() - 2) : getCurrentScreen();
                    old.update(input);

                    // Find the new offset of the old screen after this u pdate
                    float oldOffsetX = old.getDrawOffsetX() + xVelocity;
                    float oldOffsetY = old.getDrawOffsetY() + yVelocity;
                    old.setDrawOffset((int) oldOffsetX, (int) oldOffsetY);

                    // The velocity factor will decrease indefinitely as it reaches 0
                    // This will clamp the offset to the target if the velocity falls below
                    // a threshold so the user doesn't have to wait for too long.
                    if(Math.abs(xVelocity) < 0.025f && Math.abs(yVelocity) < 0.025f) {
                        target.setDrawOffset(0, 0);

                        // If there are no new transit screens, then this animation
                        // is for returning to a previous screen.
                        if(transitScreen == null) {
                            target.onTransitionIn();
                            popScreen();
                        }

                        transitionPhase++;
                    }
                    break;

                /*
                    At this point most of the animation is complete.
                    This transition mechanism is reset and ready to be used again.
                 */
                case 2:
                    transition = false;

                    if(transitScreen != null) {
                        transitScreen.onTransitionIn();
                    }
                    break;
            }
        }

        input.update();
    }

    /**
     * Adds a new screen to the top of the stack.
     *
     * @param screen New screen object
     */
    public void pushScreen(Screen screen) {
        screen.onAdd(this);
        screens.push(screen);
    }

    /**
     * Removes the top-level screen off the render stack.
     *
     * @return The removed screen object.
     */
    public Screen popScreen() {
        return screens.pop();
    }

    /**
     * Prepares the program for a screen transition.
     * ScreenScrollType defines the direction from which the new screen
     * will be polled from. If this animation is used to return to a previous
     * menu, then the `screen` parameter will be null.
     *
     * @param scrollType Transition scroll direction.
     * @param screen Target screen.
     */
    public void performScreenTransition(ScreenScrollType scrollType, Screen screen) {
        // Prompt all components on existing screen to 'animate out'
        getCurrentScreen().onTransitionOut();

        // Initialize transition mechanism variables
        this.transitScreen = screen;
        this.screenScrollType = scrollType;
        transition = true;
        transitionPhase = 0;
    }

    private Screen getCurrentScreen() {
        return screens.peek();
    }

    public void setDarkMode(boolean flag) {
        this.darkMode = flag;
        for (Screen screen : screens) {
            screen.refreshColorScheme();
        }
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

    public boolean isDarkMode() {
        return darkMode;
    }
}
