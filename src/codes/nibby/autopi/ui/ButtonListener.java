package codes.nibby.autopi.ui;

/**
 * <p>Event listener interface for all Button components.</p>
 *
 * <b>Created 10/06/16.</b>
 *
 * @author Kevin Yang
 */
public interface ButtonListener {

    /**
     * Invoked method when a button is clicked.
     *
     * @param b Button that has been clicked.
     */
    void buttonClicked(Button b);

    /**
     * Invoked method when a button is hovered.
     *
     * @param b Button that has been hovered.
     */
    void buttonHovered(Button b);

}
