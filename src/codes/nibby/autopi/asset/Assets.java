package codes.nibby.autopi.asset;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * <p>
 *     The functional registrar of all assets used in the program.
 *     Actual loading of assets is done in the loading menu (see `LoadingScreen.java`).
 * </p>
 *
 * <b>Created 17/06/16.</b>
 *
 * <p>
 *     Asset loading done in the same thread causes startup latency.
 *     Larger assets (such as background music) will take significant
 *     processing power. By utilizing deferred resource loading, the
 *     resource reference can be declared prior to resource loading,
 *     ensuring that the program launches responsively and in a more
 *     user-friendly manner by showing a loading screen.
 * </p>
 *
 * @author Kevin Yang
 */
public class Assets {

    /** Dictionary-equivalent registrar for all program resources */
    private static final Map<String, Asset> REGISTRAR = new HashMap<>();

    /** Indexing all deferred resource to be loaded during start-up */
    private static final Queue<Asset> LOAD_QUEUE = new ArrayDeque<>();

    // Declared assets
    public static final Sound SFX_HOVER = new Sound("ui_hover", "/sounds/hover.wav");
    public static final Sound SFX_CLICK = new Sound("ui_click", "/sounds/click.wav");

    /**
     * Puts an asset into the registrar and (if unloaded) indexed to the load queue.
     * This method is invoked automatically in the `Asset` class.
     *
     * @param asset The asset to be registered.
     * @return Success indicator flag.
     */
    public static boolean register(Asset asset) {
        // Reject duplicated assets
        if (REGISTRAR.containsKey(asset.getId()))
            return false;
        else {
            //Register new asset
            REGISTRAR.put(asset.getId(), asset);
            if(!asset.isLoaded())
                LOAD_QUEUE.add(asset);

            return true;
        }
    }

    /**
     *
     * @return The asset load queue.
     */
    public static Queue<Asset> getLoadQueue() {
        return LOAD_QUEUE;
    }
}
