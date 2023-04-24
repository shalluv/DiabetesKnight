package sharedObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RenderableHolder {

	private static final RenderableHolder instance = new RenderableHolder();

	public static RenderableHolder getInstance() {
		return instance;
	}

	private Comparator<Renderable> comparator;
	private ArrayList<Renderable> entities;

	private RenderableHolder() {
		entities = new ArrayList<Renderable>();
		comparator = (Renderable o1, Renderable o2) -> {
			if (o1.getZ() > o2.getZ())
				return 1;
			return -1;
		};
	}

	public void add(Renderable entity) {
		entities.add(entity);
		Collections.sort(entities, comparator);
	}

	public ArrayList<Renderable> getEntities() {
		return entities;
	}

	public void update() {
		for (int i = entities.size() - 1; i >= 0; --i) {
			if (entities.get(i).isDestroyed()) {
				entities.remove(i);
			}
		}
	}
}
