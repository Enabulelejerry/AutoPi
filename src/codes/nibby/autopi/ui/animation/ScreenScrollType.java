package codes.nibby.autopi.ui.animation;

/**
 * <p>An enumerated list of possible screen transition scroll directions.
 * Used when one screen transitions to the other. The new screen
 * will scroll from a direction listed here.</p>
 *
 * <b>Created 11/06/16.</b>
 *
 * @author Kevin Yang
 */
public enum ScreenScrollType {

    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    NO_SCROLL(0, 0);

    // Starting offset translation of the new screen
    int xShift;
    int yShift;

    ScreenScrollType(int horiShift, int vertShift) {
        xShift = horiShift;
        yShift = vertShift;
    }

    public int getXShift() {
        return xShift;
    }

    public int getYShift() {
        return yShift;
    }
}
