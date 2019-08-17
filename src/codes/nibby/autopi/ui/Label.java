package codes.nibby.autopi.ui;

import codes.nibby.autopi.Main;
import codes.nibby.autopi.ui.Component;

import java.awt.*;

/**
 * A text-only component that has inherent animation properties.
 *
 * Author: Kevin Yang
 * Creation Date: 13/06/16.
 */
public class Label extends Component {

    private String[] text;
    private Font font;

    public Label(int x, int y, String text, Color color, Font font) {
        super(x, y, 0, 0);

        setForegroundColor(color, true);
        this.text = text.split("\n");
        this.font = font;

        Graphics2D g = Main.getApp().getDrawGraphics();
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        setWidth(metrics.stringWidth(this.text[0]));
        setHeight(metrics.getHeight());
        setDrawDimensions(getWidth(), getHeight());
    }

    @Override
    public void render(Graphics2D g) {
        g.setFont(font);
        g.setColor(getForegroundColor());
        FontMetrics metrics = g.getFontMetrics();
        for(int i = 0; i < text.length; i++) {
            g.drawString(text[i], getDrawX() + getOffsetX(), getDrawY() + getOffsetY() + (metrics.getHeight() + 2) * i);
        }
    }

    @Override
    public void update(InputHandler input) {
        super.update(input);
    }

    public void centerText() {
        Graphics2D g = Main.getApp().getDrawGraphics();
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int width = metrics.stringWidth(text[0]);
        setPosition(getX() - width / 2, getY(), true);
    }
}
