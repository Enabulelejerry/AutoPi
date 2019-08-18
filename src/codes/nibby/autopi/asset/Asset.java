package codes.nibby.autopi.asset;

/**
 * <p>An abstract asset used for deferred resource loading. Each
 * resource is given a unique resource key (to identify the resource
 * in registrar) and an object reference to the actual resource.</p>
 *
 * <b>Created 17/06/16.</b>
 *
 * @author Kevin Yang
 */
public abstract class Asset {

    /** Asset identifier (used in Assets registrar) */
    protected String id;

    /** Referred resource, to be loaded on start-up */
    protected Object target;

    /** Resource location */
    protected String resource;

    /**
     * Creates a deferrable asset
     * @param prefix Asset type prefix (to avoid ID clash)
     * @param id Unique identifier name
     * @param resource Resource path
     */
    public Asset(String prefix, String id, String resource) {
        this.id = prefix + ":" + id;
        this.resource = resource;

        Assets.register(this);
    }

    /**
     * This method will be implemented by concrete classes to load the actual asset.
     *
     */
    public abstract void load();

    /**
     *
     * @return The specific asset object.
     */
    public abstract Object getData();

    /**
     *
     * @return Unique asset identifier name.
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return Asset object.
     */
    public Object getTarget() {
        return target;
    }

    /**
     *
     * @return Resource location path.
     */
    public String getResource() {
        return resource;
    }

    /**
     *
     * @return Whether of this asset has been successfully loaded.
     */
    public boolean isLoaded() {
        return target != null;
    }
}
