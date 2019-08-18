package codes.nibby.autopi.asset;

import java.applet.Applet;
import java.applet.AudioClip;

/**
 * <p>Representation of sound effect resources used in the program.
 * Playback is done on a separate thread.</p>
 *
 * <b>Created 17/06/16.</b>
 *
 * @author Kevin Yang
 */
public class Sound extends Asset {

    public Sound(String id, String resource) {
        super("sfx", id, resource);
    }

    @Override
    public void load() {
        target = Applet.newAudioClip(Sound.class.getResource(resource));
    }

    public void play() {
        new Thread(() -> {
            ((AudioClip) target).play();
        }).start();
    }

    @Override
    public Object getData() {
        return target;
    }
}
