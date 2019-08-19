package codes.nibby.autopi.screen;

import codes.nibby.autopi.AutoPi;
import codes.nibby.autopi.ui.ColorScheme;
import codes.nibby.autopi.ui.InputHandler;
import codes.nibby.autopi.ui.animation.ScreenScrollType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The animated background universal to all AutoPi screens
 *
 * Author: Kevin Yang
 * Creation Date: 10/06/16.
 */
public class Stage {

    static final float PARALLAX_FACTOR = 0.75f;
    static final float SMOOTH_FACTOR = 5;

    // Offset to scroll to
    private float targetOffsetX = 0;
    private float targetOffsetY = 0;

    // Current offset
    private float offsetX = 0;
    private float offsetY = 0;

    // Collection of animated objects on -screen
    private List<Shape> objects = new ArrayList<>();

    // Circular shape that follows mouse cursor
    private Shape mouseShape;
    private boolean drawShape = true;

    public void render(Graphics2D g) {
        // Draws all animated objects
        for(Shape object : objects) {
            object.render(g, offsetX, offsetY);
        }
    }

    public void update(InputHandler input) {
        // Clamps offset movement if the velocity is too low
        if(Math.abs(targetOffsetX - offsetX) < 0.02f
                && Math.abs(targetOffsetY - offsetY) < 0.02f) {
            offsetX = targetOffsetX;
            offsetY = targetOffsetY;
        } else {
            // Performs smooth scrolling through linear interpolation
            float deltaX = (targetOffsetX - offsetX) / AutoPi.TARGET_FPS / SMOOTH_FACTOR;
            float deltaY = (targetOffsetY - offsetY) / AutoPi.TARGET_FPS / SMOOTH_FACTOR;
            offsetX += deltaX;
            offsetY += deltaY;

            if(Math.abs(deltaX) < 0.005f && Math.abs(deltaY) < 0.005f) {
                offsetX = targetOffsetX;
                offsetY = targetOffsetY;
            }
        }

        for(int i = 0; i < objects.size(); i++) {
            Shape obj = objects.get(i);
            obj.update(input);

            // If a shape has 0 transparency, it will be removed
            // and a new shape will be generated.
            if(obj.shouldRemove()) {
                objects.remove(i);
                --i;
            }
        }

        if(objects.size() < 4 && drawShape) {
            // Shape generation, its position size and growth is random
            float x = (float) (Math.random() * AutoPi.APP_WIDTH) -targetOffsetX;
            float y = (float) (Math.random() * AutoPi.APP_HEIGHT) - targetOffsetY;
            float alpha = (float) (Math.random() * 1.0f);
            float growth = (float) (Math.random() * 1) + 0.001f;
            float strokeWidth = 1f + (float) (Math.random() * 3f);

            Color c = ColorScheme.get("secondary.lighter");
            int r = c.getRed();
            int g = c.getGreen();
            int b = c.getBlue();

            addObject(new Shape(Shape.SQUARE, x, y, 1f, 1f, growth, strokeWidth, alpha, r, g, b));
        }

//      TODO: Disabled for performance reasons on device
//        // Redraws the mouse circle
//        boolean respawnMouse = false;
//        if (mouseShape != null && input.isMousePressed(MouseEvent.BUTTON1)) {
//            respawnMouse = true;
//        }
//
//        if(mouseShape == null || respawnMouse) {
//            int mx = input.getMouseX();
//            int my = input.getMouseY();
//            float growth = 0.25f;
//            float alpha = 0.3f;
//
//            Color c = ColorScheme.get("stage.ripple");
//            int r = c.getRed();
//            int g = c.getGreen();
//            int b = c.getBlue();
//            mouseShape = new Shape(Shape.CIRCLE, mx - offsetX, my - offsetY, 1f, 1f, growth, 3f ,alpha, r, g, b);
//
//            addObject(mouseShape);
//        }

    }

    public void setDrawShape(boolean flag) {
        drawShape = flag;
    }

    /**
     * Sets the target scroll direction for a screen transition.
     *
     * @param transition Transition scroll direction.
     */
    public void pan(ScreenScrollType transition) {
        targetOffsetX -= (float) transition.getXShift() * AutoPi.APP_WIDTH * PARALLAX_FACTOR;
        targetOffsetY -= (float) transition.getYShift() * AutoPi.APP_HEIGHT * PARALLAX_FACTOR;
    }

    private void addObject(Shape object) {
        objects.add(object);
    }

    /**
     * A representation of an animated object in the scene.
     */
    class Shape {

        // Shape type constants
        static final int SQUARE = 0;
        static final int CIRCLE = 1;

        // On-sceren bounds
        float x;
        float y;
        float width;
        float height;

        // Color
        float alpha;
        int r, g, b;

        // Expand rate
        float growth;

        // Shape type
        int type;

        // Line width
        float strokeWidth = 1;

        Shape(int type, float x, float y, float w, float h, float growth, float strokeWidth, float a, int r, int g, int b) {
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
            this.alpha = a;
            this.strokeWidth = strokeWidth;
            this.growth = growth;

            this.r = r;
            this.g = g;
            this.b = b;
            this.type = type;
        }

        void render(Graphics2D g, float offsetX, float offsetY) {
            g.setColor(new Color(r, this.g, b, (int) (alpha * 255)));
            g.setStroke(new BasicStroke(strokeWidth));

            switch(type) {
                case CIRCLE:
                    g.drawOval((int) (x + offsetX), (int) (y + offsetY), (int) width, (int) height);
                    break;
                case SQUARE:
                    g.drawRect((int) (x + offsetX), (int) (y + offsetY), (int) width, (int) height);
                    break;
            }
        }

        void update(InputHandler input) {
            // Grows shape
            x -= growth;
            y -= growth;
            width += growth * 2;
            height += growth * 2;
            alpha -= 0.002f;
        }

        boolean shouldRemove() {
            return alpha <= 0f;
        }
    }
}
