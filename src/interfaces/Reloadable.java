package interfaces;

/**
 * Reloadable
 * Represents an object that can be reloaded
 */
public interface Reloadable {
	/**
	 * Reload the object
	 */
	public void reload();

	/**
	 * Get the ammo of the object
	 * @return the ammo of the object
	 */
	public int getAmmo();

	/**
	 * Cancel the reload of the object
	 */
	public void cancelReload();

	/**
	 * Get the maximum ammo of the object
	 * @return the maximum ammo of the object
	 */
	public int getMaxAmmo();
}
