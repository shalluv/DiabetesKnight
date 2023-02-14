package logic;

import java.util.ArrayList;

import entity.Player;
import entity.base.Entity;
import sharedObject.RenderableHolder;

public class GameLogic {

	private ArrayList<Entity> gameObjectContainer;

	private Player player;

	public GameLogic(String[] mapData) {
		this.gameObjectContainer = new ArrayList<>();
		Map map = new Map(mapData);
		addAllObject(map.getMap());

		player = new Player(50, 200);
		addNewObject(player);
	}

	protected void addNewObject(Entity entity) {
		gameObjectContainer.add(entity);
		RenderableHolder.getInstance().add(entity);
	}

	protected void addAllObject(ArrayList<Entity> entities) {
		for (Entity entity : entities) {
			addNewObject(entity);
		}
	}

	public void update() {
		player.update();
	}
}
