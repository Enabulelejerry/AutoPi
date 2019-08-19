package codes.nibby.autopi.ui.animation;

import codes.nibby.autopi.ui.ColorScheme;
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

    /** Target color to transition into. This is a ColorScheme key. */
    private String targetColorKey;

    /** Length of animation, in milliseconds */
    private int duration;

    public BackgroundColorAnimation(String target, int duration) {
        this.targetColorKey = target;
        this.duration = duration;
    }

    @Override
    public void updateAnimation(Component c) {
        // Calculates transitional color
        Color bg = c.getBackgroundColor();

        int elapsedTime = (int) (System.currentTimeMillis() - getStartTime());
        float progress = (float) elapsedTime / (float) duration;

        // Linear interpolation
        Color targetColor = ColorScheme.get(targetColorKey);
        int r = (int) ((1f - progress) * (float) bg.getRed() + progress * (float) targetColor.getRed());
        int g = (int) ((1f - progress) * (float) bg.getGreen() + progress * (float) targetColor.getGreen());
        int b = (int) ((1f - progress) * (float) bg.getBlue() + progress * (float) targetColor.getBlue());

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

    public String getTargetColorKey() {
        return targetColorKey;
    }

    public int getDuration() {
        return duration;
    }
}
