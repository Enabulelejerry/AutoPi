package codes.nibby.autopi.screen;

import codes.nibby.autopi.ui.Component;
import codes.nibby.autopi.ui.InputHandler;
import codes.nibby.autopi.ui.animation.Animation;
import codes.nibby.autopi.ui.animation.AnimationType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * The abstract layer of a viewable region on-screen. Since it is a generic
 * component, it is both used within each 'app' as well as main menu screens.
 *
 * Each screen manages a set of interactive components to carry out a task.
 *
 * <b>First created 9/06/16.</b>
 * <b>Ported on 19/08/19.</b>
 *
 * @author Kevin Yang
 */
public abstract class Screen  {

    // TODO Layout specifications

    // Render depth attributes
    // TODO: Remember why I added this 3 years ago...
    private static final int Z_ORDER_DEPTH = 3;
    private static final int Z_ORDER_DEFAULT = 1;

    /*
        Components on the screen are managed in z-orders. Higher z-orders are rendered first.

         z-orders range from 0 - 2, with 1 being the default.
     */
    private Map<Integer, List<Component>> components = new HashMap<>();

    //Render offset hints
    private int drawOffsetX = 0;
    private int drawOffsetY = 0;

    public Screen() {

        //Initialize z-order hash map with capacity as defined by Z_ORDER_DEPTH
        for(int i = 0; i < Z_ORDER_DEPTH; i++) {
            components.put(i, new ArrayList<>());
        }
    }

    public void render(Graphics2D g) {
        //Iterates component list, starting from z-depth of 0 and draw each component to screen
        for(int i = 0; i < Z_ORDER_DEPTH; i++) {
            List<Component> componentList = components.get(i);

            for(Component c : componentList) {
                c.render(g);
            }
        }
    }

    public void update(InputHandler input) {
        //Handles mouse and keyboard input and animation updates for each component
        for(int i = 0; i < Z_ORDER_DEPTH; i++) {
            List<Component> componentList = components.get(i);

            for(Component c : componentList) {
                c.setDrawOffset(drawOffsetX, drawOffsetY);
                c.update(input);
            }
        }
    }

    public void onTransitionIn() {
        for(int key : components.keySet()) {
            List<Component> componentsList = components.get(key);

            for(Component component : componentsList) {
                component.playAnimation(AnimationType.SCREEN_TRANSITION_IN);
//                if(component instanceof TextField) {
//                    Main.getApp().getInputHandler().addKeyListener((TextField) component);
//                }
            }
        }
    }

    public void onTransitionOut() {
        for(int key : components.keySet()) {
            List<Component> componentsList = components.get(key);

            for(Component component : componentsList) {
                component.playAnimation(AnimationType.SCREEN_TRANSITION_OUT);

//                if(component instanceof TextField) {
//                    graphmi.getInputHandler().removeKeyListener((TextField) component);
//                }
            }
        }
    }

    public boolean canTransitOut() {
        for(int key : components.keySet()) {
            List<Component> componentsList = components.get(key);

            for(Component component : componentsList) {
                AnimationType lastPlayed = component.getLastPlayedAnimationType();
                Animation animation = component.getAnimation(AnimationType.SCREEN_TRANSITION_OUT);

                if(lastPlayed == null)
                    return false;

                if((animation != null && !animation.isFinished()))
                    return false;
            }
        }

        return true;
    }

    public void addComponent(Component component) {
        addComponent(component, Z_ORDER_DEFAULT);
    }

    void addComponent(Component component, int zDepth) {
        if(zDepth >= 0 && zDepth < Z_ORDER_DEPTH) {
            components.get(zDepth).add(component);
            component.onAdd(this);
        }
    }

    public void onAdd() {

    }

    public void setDrawOffset(int x, int y) {
        this.drawOffsetX = x;
        this.drawOffsetY = y;

        for(int i = 0; i < Z_ORDER_DEPTH; i++) {
            List<Component> componentList = components.get(i);

            for(Component c : componentList) {
                c.setDrawOffset(drawOffsetX, drawOffsetY);
            }
        }
    }

    public int getDrawOffsetX() {
        return drawOffsetX;
    }

    public int getDrawOffsetY() {
        return drawOffsetY;
    }

}
