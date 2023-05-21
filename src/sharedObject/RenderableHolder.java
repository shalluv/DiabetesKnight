package sharedObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * RenderableHolder
 * Holds all renderable objects
 * @see sharedObject.Renderable
 */
public class RenderableHolder {

	/**
	 * The instance of RenderableHolder
	 * Things need to be rendered are stored in this class
	 * @see sharedObject.Renderable
	 */
	private static final RenderableHolder instance = new RenderableHolder();
	/**
	 * The comparator for sorting the renderable objects
	 * @see java.util.Comparator
	 * @see sharedObject.Renderable
	 */
	private Comparator<Renderable> comparator;
	/**
	 * The list of renderable objects
	 * @see sharedObject.Renderable
	 * @see java.util.ArrayList
	 */
	private ArrayList<Renderable> entities;

	/**
	 * Get the instance of RenderableHolder
	 * @return the instance of RenderableHolder
	 */
	public static RenderableHolder getInstance() {
		return instance;
	}

	/**
	 * Constructor
	 */
	private RenderableHolder() {
		entities = new ArrayList<Renderable>();
		comparator = (Renderable o1, Renderable o2) -> {
			if (o1.getZ() > o2.getZ())
				return 1;
			return -1;
		};
	}

	/**
	 * Add a renderable object to the list
	 * @param entity the renderable object to be added
	 */
	public void add(Renderable entity) {
		entities.add(entity);
		Collections.sort(entities, comparator);
	}

	/**
	 * Get the list of renderable objects
	 * @return the list of renderable objects
	 */
	public ArrayList<Renderable> getEntities() {
		return entities;
	}

	/**
	 * Clear all renderable objects
	 */
	public void clearAll() {
		entities.removeAll(entities);
	}

	/**
	 * Update the list of renderable objects
	 */
	public void update() {
		for (int i = entities.size() - 1; i >= 0; --i) {
			if (entities.get(i).isDestroyed()) {
				entities.remove(i);
			}
		}
	}

}
