package logic;

import java.util.ArrayList;

import application.CSVParser;
import application.Main;
import entity.Player;
import entity.base.Entity;
import sharedObject.RenderableHolder;

public class GameLogic {

	private ArrayList<Entity> gameObjectContainer;

	private Player player;

	public GameLogic() {
		this.gameObjectContainer = new ArrayList<>();

		int[][] mapData = CSVParser.readCSV("res/csv/Level_1.csv");
		Map map = new Map(mapData);
		addAllObject(map.getMap());

		player = new Player(50, 600);
		addNewObject(player);
	}

	protected void addAllObject(ArrayList<Entity> entities) {
		for (Entity entity : entities) {
			addNewObject(entity);
		}
	}

	protected void addNewObject(Entity entity) {
		getGameObjectContainer().add(entity);
		RenderableHolder.getInstance().add(entity);
	}

	public void update() {
		player.update();
		updateScreen();
	}

	private void updateScreen() {
		if (player.getX() > 640 && player.getX() + 640 < Map.getWidth()) {
			Main.gameScreen.setX(-(player.getX() - 640));
		}
		if (player.getY() > 480 && player.getY() + 480 < Map.getHeight()) {
			Main.gameScreen.setY(-(player.getY() - 480));
		}
	}
}
