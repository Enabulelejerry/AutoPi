package codes.nibby.autopi.ui.animation;

import codes.nibby.autopi.ui.Component;

/**
 * <p>This animation augments the shape of a component using linear interpolation.</p>
 *
 * <b>Created 10/06/16.</b>
 *
 * @author Kevin Yang
 */
public class ShapeAnimation extends Animation {

    /** Target bound x */
    private int tx;

    /** Target bound y */
    private int ty;

    /** Target width */
    private int tWidth;

    /** Target height */
    private int tHeight;

    /** Time (in milliseconds) the animation lasts for **/
    private int duration;

    public ShapeAnimation(int tx, int ty, int tw, int th, int duration) {
        this.tx = tx;
        this.ty = ty;
        this.tWidth = tw;
        this.tHeight = th;
        this.duration = duration;
    }

    @Override
    public void updateAnimation(Component c) {
        // Calculates percentage progress of animation cycle
        int elapsedTime = (int) (System.currentTimeMillis() - getStartTime());
        float progress = (float) elapsedTime / (float) duration;

        // Calculates component boundaries using a quarsi linear-interpolation algorithm
        int dx = (int) ((c.getDrawX() - tx) * progress);
        int dy = (int) ((c.getDrawY() - ty) * progress);
        int dw = (int) ((c.getDrawWidth() - tWidth) * progress);
        int dh = (int) ((c.getDrawHeight() - tHeight) * progress);

        // Performs boundary update
        c.setDrawPosition(c.getDrawX() - dx, c.getDrawY() - dy);
        c.setDrawDimensions(c.getDrawWidth() - dw, c.getDrawHeight() - dh);
    }

    @Override
    protected void resetAnimation() {

    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() - getStartTime() > duration;
    }
}
