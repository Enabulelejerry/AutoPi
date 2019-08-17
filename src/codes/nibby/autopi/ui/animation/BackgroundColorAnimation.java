package codes.nibby.autopi.ui.animation;

import codes.nibby.autopi.ui.Component;

import java.awt.*;

/**
 * <p>Change the background color of a component through a linear interpolation.</p>
 *
 * <b>Created 9/06/16.</b>
 *
 * @author Kevin Yang
 */
public class BackgroundColorAnimation extends Animation {

    /** Target color to transition into */
    private Color target;

    /** Length of animation, in milliseconds */
    private int duration;

    public BackgroundColorAnimation(Color target, int duration) {
        this.target = target;
        this.duration = duration;
    }

    @Override
    public void updateAnimation(Component c) {
        // Calculates transitional color
        Color bg = c.getBackgroundColor();

        int elapsedTime = (int) (System.currentTimeMillis() - getStartTime());
        float progress = (float) elapsedTime / (float) duration;

        // Linear interpolation
        int r = (int) ((1f - progress) * (float) bg.getRed() + progress * (float) target.getRed());
        int g = (int) ((1f - progress) * (float) bg.getGreen() + progress * (float) target.getGreen());
        int b = (int) ((1f - progress) * (float) bg.getBlue() + progress * (float) target.getBlue());

        // Correct boundaries
        if (r > 255)    r = 255;
        if (r < 0)      r = 0;
        if (g > 255)    g = 255;
        if (g < 0)      g = 0;
        if (b > 255)    b = 255;
        if (b < 0)      b = 0;

        c.setBackgroundColor(new Color(r, g, b), false);
    }

    @Override
    protected void resetAnimation() {
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() - getStartTime() > duration;
    }

    public Color getTarget() {
        return target;
    }

    public int getDuration() {
        return duration;
    }
}
