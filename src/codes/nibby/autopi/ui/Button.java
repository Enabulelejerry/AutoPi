package codes.nibby.autopi.ui;

import codes.nibby.autopi.AutoPi;
import codes.nibby.autopi.asset.Assets;
import codes.nibby.autopi.asset.Fonts;
import codes.nibby.autopi.ui.animation.AnimationType;
import codes.nibby.autopi.ui.animation.BackgroundColorAnimation;
import codes.nibby.autopi.ui.animation.RestoreAnimation;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>An implementation of component that underpin the functions of a Button</p>
 *
 * <b>Creation 9/06/16.</b>
 * <b>First ported 19/08/19.</b>
 *
 * @author Kevin Yang
 */
public class Button extends Component {

    // Button visual types, normal for standard grey, default for green

    /** Normal button color. */
    public static final int TYPE_NORMAL = 0;

    /** Default button, uses default color scheme. */
    public static final int TYPE_DEFAULT = 1;

    /** Light blue button theme. */
    public static final int TYPE_LIGHT_BLUE = 2;

    /** Text aligns at centre of the component*/
    public static final int ALIGNMENT_CENTRE = 0;

    /** Text aligns towards the left of the button */
    public static final int ALIGNMENT_LEFT = 1;

    /** Text aligns towards the right of the button */
    public static final int ALIGNMENT_RIGHT = 2;


    /** Text align attribute, centered by default */
    private int textAlign = ALIGNMENT_CENTRE;

    /** Button text */
    private String text;

    // State control for button, used for triggering mouse events
    private boolean mouseDown = false;
    private boolean wasHovered = false;

    /** Event trigger list */
    private List<ButtonListener> listeners = new ArrayList<>();

    /**
     * Creates a basic decorated button.
     *
     * @param style Default styling to be applied. See styling constants above.
     * @param text Button text.
     * @param x x-position on screen.
     * @param y y-position on screen.
     * @param width Component width.
     * @param height Component height;
     */
    public Button(int style, String text, int x, int y, int width, int height) {
        super(x, y, width, height);
        setText(text);

        // Based on given style, sets the appropriate color scheme
        setStyle(style);

        // Configure default restore animation
        RestoreAnimation restore = new RestoreAnimation(1000);
        restore.setRestoreBounds(x, y, width, height);
        restore.setBackground(getBackgroundColor());
        restore.setForeground(getForegroundColor());

        setAnimation(AnimationType.RESTORE, restore);
        setAnimation(AnimationType.SCREEN_TRANSITION_IN, restore);
    }

    @Override
    public void render(Graphics2D g) {
        if(!isVisible())
            return;

        // Draw button space
        g.setColor(getBackgroundColor());
        g.fillRect(getDrawX() + getOffsetX(), getDrawY() + getOffsetY(),
                getDrawWidth(), getDrawHeight());

        // Draw font
        if(getDrawWidth() > 0) {
            g.setFont(Fonts.STANDARD_RALEWAY);
            g.setColor(getForegroundColor());
            FontMetrics metrics = g.getFontMetrics();
            int x;
            int y = getDrawY() + getDrawHeight() / 10 * 6;

            // Calculates font position depending on alignment preferences
            switch (textAlign) {
                case ALIGNMENT_CENTRE:
                default:
                    x = getDrawX() + getDrawWidth() / 2 - metrics.stringWidth(text) / 2;
                    break;

                case ALIGNMENT_LEFT:
                    x = getDrawX() + 10;
                    break;

                case ALIGNMENT_RIGHT:
                    x = getDrawX() + getDrawWidth() - 10 - metrics.stringWidth(text);
                    break;
            }
            g.drawString(text, x + getOffsetX(), y + getOffsetY());
        }
    }

    @Override
    public void update(InputHandler input) {
        if(!isVisible())
            return;

        super.update(input);

        /* Mouse interaction mechanism */
        int mx = input.getMouseX();
        int my = input.getMouseY();

        // Check for mouse press and mouse hover events
        if(mx > getDrawX() + getOffsetX() && my > getDrawY() + getOffsetY()
                && mx < getDrawX() + getDrawWidth() + getOffsetX()
                && my < getDrawY() + getDrawHeight() + getOffsetY()) {

            if(input.isMousePressed(MouseEvent.BUTTON1)) {
                updateState(ComponentState.PRESSED);
                mouseDown = true;
            } else {
                if (mouseDown) {
                    fireMousePressEvent();
                    Assets.SFX_CLICK.play();
                    mouseDown = false;
                } else {
                    if(!wasHovered) {
                        updateState(ComponentState.HOVERED);
                        Assets.SFX_HOVER.play();
                        fireMouseHoverEvent();
                        wasHovered = true;
                    }
                }
            }
        } else {
            updateState(ComponentState.NORMAL);
            wasHovered = false;

            // Release mouse trigger if released out of bounds
            if(!input.isMousePressed(MouseEvent.BUTTON1) && mouseDown) {
                mouseDown = false;
            }
        }
    }

    /**
     * Informs all event trigger targets of a mouse click event
     */
    protected void fireMousePressEvent() {
        for (ButtonListener l : listeners) {
            l.buttonClicked(this);
        }
    }

    /**
     * Informs all event trigger targets of a mouse hover event
     */
    protected void fireMouseHoverEvent() {
        for (ButtonListener l : listeners) {
            l.buttonHovered(this);
        }
    }

    /**
     * Binds an event trigger target for this button,
     * the trigger target will be informed of the interactions between
     * the mouse and the component.
     *
     * @param listener Trigger target.
     */
    public void addListener(ButtonListener listener) {
        listeners.add(listener);
    }


    /**
     *
     * @return Current button text
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text New button text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return Current button alignment rule
     */
    public int getTextAlign() {
        return textAlign;
    }

    /**
     *
     * @param textAlign New alignment rule
     */
    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
    }

    /**
     * Applies a color scheme to the button.
     *
     * @param style Style constant, see above.
     */
    public void setStyle(int style) {
        //Applies some default styling options
        switch(style) {
            case TYPE_NORMAL:
                setBackgroundColor("secondary.standard");
                setForegroundColor("secondary.foreground");
                setAnimation(AnimationType.COMPONENT_MOUSE_HOVER, new BackgroundColorAnimation("secondary.lighter", 1000));
                setAnimation(AnimationType.COMPONENT_MOUSE_PRESS, new BackgroundColorAnimation("secondary.lighter", 1000));
                break;
            case TYPE_DEFAULT:
                setBackgroundColor("primary.standard");
                setForegroundColor("primary.button.foreground");
                setAnimation(AnimationType.COMPONENT_MOUSE_HOVER, new BackgroundColorAnimation("primary.lighter", 1000));
                setAnimation(AnimationType.COMPONENT_MOUSE_PRESS, new BackgroundColorAnimation("primary.lighter", 1000));
                break;
            case TYPE_LIGHT_BLUE:
                setBackgroundColor("toggle.base");
                setForegroundColor("toggle.foreground");
                setAnimation(AnimationType.COMPONENT_MOUSE_PRESS, new BackgroundColorAnimation("toggle.lighter", 1000));
                setAnimation(AnimationType.COMPONENT_MOUSE_PRESS, new BackgroundColorAnimation("toggle.lighter", 1000));
        }
    }
}
