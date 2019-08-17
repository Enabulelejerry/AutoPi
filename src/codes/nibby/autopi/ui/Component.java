package codes.nibby.autopi.ui;

import codes.nibby.autopi.ui.animation.Animation;
import codes.nibby.autopi.ui.animation.AnimationType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Component {

    private int x, y, width, height;
    private int dx, dy, dWidth, dHeight;

    private int offsetX, offsetY;
    private Color backgroundColor, foregroundColor;

    private Map<AnimationType, Animation> animations = new HashMap<>();
    private Animation currentAnim;
    private AnimationType lastPlayedAnimType;

    protected ComponentState state;
    protected boolean visible = true;
}
