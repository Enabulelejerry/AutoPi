package codes.nibby.autopi.ui.animation;

import codes.nibby.autopi.ui.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>A container animation that allows for multiple, simultaneous animations.</p>
 *
 * <b>Created 10/06/16</b>
 *
 * @author Kevin Yang
 */
public class CompositeAnimation extends Animation {

    /** List of animations to be updated/rendered simultaneously */
    private List<Animation> animations = new ArrayList<>();

    /**
     * Constructs a composite animation as a collection of individual animations.
     * @param animations Animation varargs
     */
    public CompositeAnimation(Animation ... animations) {
        for(Animation a : animations) {
            add(a);
        }
    }

    /**
     * Add a new animation to the play list.
     * @param a Animation to be played.
     * @return The composite instance itself to allow chain invocations.
     */
    public CompositeAnimation add(Animation a) {
        animations.add(a);
        return this;
    }

    @Override
    public void updateAnimation(Component c) {
        for(Animation animation : animations) {
            animation.update();
        }
    }

    @Override
    protected void resetAnimation() {
        for(Animation animation : animations) {
            animation.reset();
        }
    }

    @Override
    public boolean isFinished() {
        for(Animation animation : animations) {
            if(!animation.isFinished())
                return false;
        }
        return true;
    }

    @Override
    public void onAdd(Component c) {
        super.onAdd(c);
        for(Animation animation : animations) {
            animation.onAdd(c);
        }
    }

    @Override
    public void play() {
        super.play();
        for(Animation animation : animations) {
            animation.play();
        }
    }
}
