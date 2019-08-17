package codes.nibby.autopi.ui;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Maps mouse co-ordinates and listens for specific key-presses.</p>
 *
 * <p>Each keypress is mapped into a HashMap (Dictionary) with its corresponding
 * virtual key code as the dictionary key. State is represented in two states: current and past,
 * this allows identification for a key press and key hold, similarly, this concept also applies
 * to mouse click and mouse hold.</p>
 *
 * <p>As the standard Java listeners are state-based, for a FPS program this is necessary so that
 * the input can be polled instead of listened for.</p>
 *
 * <b>Created 9/06/16.</b>
 *
 * @author Kevin Yang
 */
public class InputHandler implements MouseListener, MouseMotionListener, KeyListener {

    /** Mouse x-position */
    private int mouseX;

    /** Mouse y-position */
    private int mouseY;

    /**  Mouse pressed state of 3 mouse buttons **/
    // 0 = left, 1 = middle (scroll wheel), 2 = right
    private boolean[] mouseState = { false, false, false };

    /**
     * Last mouse state of the last 3 buttons, indicies are same as above.
     */
    private boolean[] lastMouseState  = { false, false, false };

    /** Registrar of all keys pressed. */
    private Map<Integer, Key> keymap = new HashMap<>();

    /** List of key event triggers. */
    private List<KeyListener> keyListeners = new ArrayList<>();

    public InputHandler() {

    }

    public void update() {
        for(Integer keycode : keymap.keySet()) {
            //Update state data for mapped keys
            Key key = keymap.get(keycode);
            key.wasPressed = key.pressed;
            keymap.put(keycode, key);
        }

        for(int i = 0; i < 3; i++) {
            //Update state data for mouse
            lastMouseState[i] = mouseState[i];
            mouseState[i] = false;
        }
    }

    // Implemented methods from KeyListener interface
    // These are invoked automatically by the Java GUI
    // when a key is pressed on the canvas component.
    @Override
    public void keyTyped(KeyEvent e) {
        for(KeyListener l : keyListeners) {
            l.keyTyped(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        keymap.putIfAbsent(key, new Key());
        keymap.get(key).pressed = true;

        for(KeyListener l : keyListeners) {
            l.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        keymap.putIfAbsent(key, new Key());
        keymap.get(key).pressed = false;

        for(KeyListener l : keyListeners) {
            l.keyReleased(e);
        }
    }

    // Implemented methods from MouseListener interface
    // These are invoked automatically by the Java GUI
    // when a mouse button is pressed on the canvas component.
    @Override
    public void mouseClicked(MouseEvent e) {
        // Updates mouse positions
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Updates mouse positions
        mouseX = e.getX();
        mouseY = e.getY();
        int button = e.getButton() - 1;
        mouseState[button] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Updates mouse positions
        mouseX = e.getX();
        mouseY = e.getY();
        int button = e.getButton() - 1;
        mouseState[button] = false;
    }

    // Implemented methods from MouseMotionListener interface
    // These are invoked automatically by the Java GUI
    // when a mouse is moved across the canvas component.
    @Override
    public void mouseDragged(MouseEvent e) {
        // Updates mouse positions
        mouseX = e.getX();
        mouseY = e.getY();
        int button = e.getButton() - 1;
        mouseState[button] = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Updates mouse positions
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    // Encapsulation methods
    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isMousePressed(int button) {
        return mouseState[button - 1] && !lastMouseState[button - 1];
    }

    public boolean isMouseHeld(int button) {
        return mouseState[button - 1] && lastMouseState[button - 1];
    }

    public boolean isKeyPressed(int keycode) {
        keymap.putIfAbsent(keycode, new Key());

        Key key = keymap.get(keycode);
        return !key.wasPressed && key.pressed;
    }

    public boolean isKeyHeld(int keycode) {
        keymap.putIfAbsent(keycode, new Key());

        Key key = keymap.get(keycode);
        return key.wasPressed && key.pressed;
    }

    public void addKeyListener(KeyListener l) {
        keyListeners.add(l);
    }

    public void removeKeyListener(KeyListener l) {
        keyListeners.remove(l);
    }

    /*
        This nested class describes a single key on the keyboard
     */
    class Key {

        boolean pressed = false;
        boolean wasPressed = false;

    }
}
