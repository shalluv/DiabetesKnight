package sharedObject;

import java.util.ArrayList;

public class RenderableHolder {

	private static final RenderableHolder instance = new RenderableHolder();
	public static RenderableHolder getInstance() {
		return instance;
	}

	private ArrayList<Renderable> entities;

	private RenderableHolder() {
		entities = new ArrayList<Renderable>();
	}

	public void add(Renderable entity) {
		entities.add(entity);
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
