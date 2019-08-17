package codes.nibby.autopi.ui.animation;

import codes.nibby.autopi.ui.Component;

public abstract class Animation {

    private Component component;
    private boolean playing = false;
    private long startTime = -1;

    public void update() {
        if (startTime == -1) {
            reset();
        }

        updateAnimation(component);
    }

    public void reset() {
        startTime = System.currentTimeMillis();
        resetAnimation();
    }

    public void play() {
        reset();
        playing = true;
    }

    public abstract void updateAnimation(Component c);

    protected abstract void resetAnimation();

    public abstract boolean isFinished();

    public void onAdd(Component c) {
        this.component = c;
    }

    public Component getComponent() {
        return component;
    }

    public long getStartTime() {
        return startTime;
    }
}
