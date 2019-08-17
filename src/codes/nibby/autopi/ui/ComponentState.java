package codes.nibby.autopi.ui;

import codes.nibby.autopi.ui.animation.AnimationType;

public enum ComponentState {

    NORMAL(AnimationType.RESTORE),
    HOVERED(AnimationType.COMPONENT_MOUSE_HOVER),
    PRESSED(AnimationType.COMPONENT_MOUSE_PRESS),
    DISABLED(null),
    HIDDEN(null)

    ;

    // Type of animation to be played on state entry
    private AnimationType animType;

    ComponentState(AnimationType entry) {
        animType = entry;
    }

    // Returns the type of animation to be played upon entering state
    public AnimationType getAnimationType() {
        return animType;
    }

}
