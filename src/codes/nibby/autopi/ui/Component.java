package codes.nibby.autopi.ui;

import codes.nibby.autopi.screen.Screen;
import codes.nibby.autopi.ui.animation.Animation;
import codes.nibby.autopi.ui.animation.AnimationType;
import codes.nibby.autopi.ui.animation.RestoreAnimation;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Component {

    /*
        A unique ID is assigned to this component whenever it is added to a Screen object.
     */
    private int id;
    private static int nextId = 0;

    // Functional positional attributes
    private int x;
    private int y;
    private int width;
    private int height;

    // Visible bounds as shown on screen
    private int dx;
    private int dy;
    private int dWidth;
    private int dHeight;

    // Draw offset (indicated by background pan animation)
    private int offsetX;
    private int offsetY;

    // Component color attributes
    private Color backgroundColor;
    private Color foregroundColor;
    private Map<AnimationType, Animation> animations = new HashMap<>();
    private Animation currentAnim;
    private AnimationType lastPlayedAnimType;

    // State control
    protected ComponentState state;
    protected boolean visible = true;

    public Component(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setDrawPosition(x, y);
        setDrawDimensions(width, height);
        state = ComponentState.NORMAL;
    }

    public abstract void render(Graphics2D g);

    public void update(InputHandler input) {
        if (currentAnim != null) {
            currentAnim.update();

            if (currentAnim.isFinished())
                currentAnim = null;
        }
    }

    public void updateState(ComponentState state) {
        if (this.state != state) {
            // Plays animation once upon state transition
            AnimationType entryAnimType = state.getAnimationType();
            playAnimation(entryAnimType);
        }
        this.state = state;
    }

    public void playAnimation(AnimationType type) {
        lastPlayedAnimType = type;
        currentAnim = animations.get(type);

        if(currentAnim != null) {
            currentAnim.play();
        }
    }

    public void setAnimation(AnimationType type, Animation animation) {
        animation.onAdd(this);
        animations.put(type, animation);
    }

    // Retrieves an animation event for component
    public Animation getAnimation(AnimationType type) {
        return animations.get(type);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPosition(int x, int y, boolean updateDrawPos) {
        this.x = x;
        this.y = y;
        if(updateDrawPos)
            setDrawPosition(x, y);
    }

    public void setDrawPosition(int x, int y) {
        this.dx = x;
        this.dy = y;
    }

    public void setDimension(int width, int height, boolean updateDrawDim) {
        this.width = width;
        this.height = height;
        if(updateDrawDim)
            setDrawDimensions(width, height);
    }

    public void setDrawDimensions(int width, int height) {
        this.dWidth = width;
        this.dHeight = height;
    }

    public int getDrawX() {
        return dx;
    }

    public int getDrawY() {
        return dy;
    }

    public int getDrawWidth() {
        return dWidth;
    }

    public int getDrawHeight() {
        return dHeight;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color background, boolean permanent) {
        this.backgroundColor = background;

        if(permanent) {
            Animation restore = getAnimation(AnimationType.RESTORE);
            if (restore != null)
                ((RestoreAnimation) restore).setBackground(background);
        }
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foreground, boolean permanent) {
        this.foregroundColor = foreground;

        if(permanent) {
            Animation restore = getAnimation(AnimationType.RESTORE);
            if (restore != null)
                ((RestoreAnimation) restore).setForeground(foreground);
        }
    }

    public AnimationType getLastPlayedAnimationType() {
        return lastPlayedAnimType;
    }

    public void setDrawOffset(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    public void onAdd(Screen screen) {
        id = nextId;
        nextId++;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getId() {
        return id;
    }
}