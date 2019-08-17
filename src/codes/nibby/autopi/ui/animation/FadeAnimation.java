package codes.nibby.autopi.ui.animation;

import java.awt.*;
import codes.nibby.autopi.ui.Component;

/**
 * <p>A simple linear 1 or 2 phase fade animation.</p>
 *
 * <b>Created 3/07/16</b>
 *
 * @author Kevin Yang
 */
public class FadeAnimation extends Animation {

    /** Fade mode: Fade-in only */
    public static final int TYPE_FADE_IN = 0;

    /** Fade mode: Fade out only */
    public static final int TYPE_FADE_OUT = 1;

    /** Fade mode: Fade in and out */
    public static final int TYPE_FADE_IN_OUT = 2;

    /** Fade mode: Fade out and in */
    public static final int TYPE_FADE_OUT_IN = 3;

    /** Fade type */
    private int type;

    /** Fade duration */
    private int duration;

    /** Current component transparency */
    private float alpha;

    public FadeAnimation(int type, int duration) {
        this.type = type;
        this.duration = duration;

        resetAnimation();
    }

    @Override
    public void updateAnimation(Component c) {
        //Keeps track of animation progress, calculated as a percentage
        float progress = (float) (System.currentTimeMillis() - getStartTime()) / (float) duration;

        // Update alpha transparency based on progress
        switch(type) {
            case TYPE_FADE_IN:
                alpha = progress;
                break;

            case TYPE_FADE_IN_OUT:
                if(progress < 0.5f)
                    alpha = progress * 2;
                else
                    alpha = (1 - (progress - 0.5f) * 2);
                break;
            case TYPE_FADE_OUT:
                alpha = 1 - progress;
                break;

            case TYPE_FADE_OUT_IN:
                if(progress < 0.5f)
                    alpha = (1 - (progress - 0.5f)) * 2;
                else
                    alpha = progress * 2;
                break;
        }

        if(alpha > 1.0f)
            alpha = 1.0f;

        if(alpha < 0f)
            alpha = 0f;

        // Modify component visuals
        Color bg = c.getBackgroundColor();
        if(bg != null)
            c.setBackgroundColor(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), (int) (alpha * 255f)), false);

        Color fg = c.getForegroundColor();
        if(fg != null)
            c.setForegroundColor(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), (int) (alpha * 255f)), false);
    }

    @Override
    public void onAdd(Component c) {
        super.onAdd(c);

        // Apply animation profile to component
        Color bg = c.getBackgroundColor();
        if(bg != null)
            c.setBackgroundColor(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), (int) (alpha * 255f)), false);

        Color fg = c.getForegroundColor();
        if(fg != null)
            c.setForegroundColor(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), (int) (alpha * 255f)), false);
    }

    @Override
    protected void resetAnimation() {
        //Reset alpha values
        switch(type) {
            case TYPE_FADE_IN:
            case TYPE_FADE_IN_OUT:
                alpha = 1.0f;
                break;

            case TYPE_FADE_OUT:
            case TYPE_FADE_OUT_IN:
                alpha = 0.0f;
                break;
        }
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() - getStartTime() > duration;
    }
}
