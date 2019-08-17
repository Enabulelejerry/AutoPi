package codes.nibby.autopi.ui.animation;

import codes.nibby.autopi.ui.Component;
import java.awt.*;

/**
 * <p>This animation restores all UI augmentations to its original visual state.</p>
 *
 * <b>Created 9/06/16.</b>
 *
 * @author Kevin Yang
 */
public class RestoreAnimation extends Animation {

    /** Original component x */
    private int x;

    /** Original component y */
    private int y;

    /** Original component width */
    private int width;

    /** Original component height */
    private int height;

    /** Original component background color */
    private Color background;

    /** Original component foreground color */
    private Color foreground;

    /** Animation duration */
    private int duration;

    public RestoreAnimation(int duration) {
        this.duration = duration;
    }

    @Override
    public void updateAnimation(Component c) {
        // Calculate the percent progress the animation has completed
        int elapsedTime = (int) (System.currentTimeMillis() - getStartTime());
        float progress = (float) elapsedTime / (float) duration;

        Color bg = getTransitionalColor(c.getBackgroundColor(), background, progress);
        c.setBackgroundColor(bg, false);

        // Restores original position using a quarsi-linear-interpolation algorithm
        int dx = (int) ((c.getDrawX() - c.getX()) * progress);
        int dy = (int) ((c.getDrawY() - c.getY()) * progress);
        int dw = (int) ((c.getDrawWidth() - c.getWidth()) * progress);
        int dh = (int) ((c.getDrawHeight() - c.getHeight()) * progress);

        // Updates draw position and dimensions
        c.setDrawPosition(c.getDrawX() - dx, c.getDrawY() - dy);
        c.setDrawDimensions(c.getDrawWidth() - dw, c.getDrawHeight() - dh);
    }

    /**
     * Calculates the transition color at a specific point in time
     * @param from Original color
     * @param to Transition color
     * @param progress Animation progress
     * @return The intermediate transition color
     */
    private Color getTransitionalColor(Color from, Color to, float progress) {
        if (progress > 1.0f) progress = 1.0f;
        if (progress < 0.0f) progress = 0.0f;

        int r = (int) ((1f - progress) * (float) from.getRed() + progress * (float) to.getRed());
        int g = (int) ((1f - progress) * (float) from.getGreen() + progress * (float) to.getGreen());
        int b = (int) ((1f - progress) * (float) from.getBlue() + progress * (float) to.getBlue());

        if (r > 255)    r = 255;
        if (r < 0)      r = 0;
        if (g > 255)    g = 255;
        if (g < 0)      g = 0;
        if (b > 255)    b = 255;
        if (b < 0)      b = 0;

        return new Color(r, g, b);
    }

    @Override
    protected void resetAnimation() {

    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() - getStartTime() > duration;
    }

    // Encapsulation methods

    /**
     * Defines the boundaries of the component to restore to
     * @param x Component X
     * @param y Component y
     * @param w Component width
     * @param h Component height
     */
    public void setRestoreBounds(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    /**
     *
     * @return Restore component x
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @param x Restore component x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     *
     * @return Restore component y
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param y Restore component y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     *
     * @return Restore component width
     */
    public int getWidth() {
        return width;
    }

    /**
     *
     * @param width Restore component width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return Restore component height
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @param height Restore component height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *
     * @return Restore component background
     */
    public Color getBackground() {
        return background;
    }

    /**
     *
     * @param background Target component background
     */
    public void setBackground(Color background) {
        this.background = background;
    }

    /**
     *
     * @return Restore component foreground
     */
    public Color getForeground() {
        return foreground;
    }

    /**
     *
     * @param foreground Target component foreground
     */
    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    /**
     *
     * @return Total animation duration
     */
    public int getDuration() {
        return duration;
    }
}
