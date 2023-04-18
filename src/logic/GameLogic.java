package logic;

import java.util.ArrayList;

import application.CSVParser;
import application.Main;
import entity.Player;
import entity.base.Entity;
import sharedObject.RenderableHolder;
import static utils.Constants.Player.*;
import static utils.Constants.Resolution;

public class GameLogic {

	private ArrayList<Entity> gameObjectContainer;

	private Player player;

	public GameLogic() {
		this.gameObjectContainer = new ArrayList<>();

		int[][] mapData = CSVParser.readCSV("res/csv/Level_1.csv");
		Map map = new Map(mapData);
		addAllObject(map.getMap());

		player = new Player(ORIGIN_X, ORIGIN_Y);
		addNewObject(player);
	}

	protected void addAllObject(ArrayList<Entity> entities) {
		for (Entity entity : entities) {
			addNewObject(entity);
		}
	}

	protected void addNewObject(Entity entity) {
		gameObjectContainer.add(entity);
		RenderableHolder.getInstance().add(entity);
	}

	public void update() {
		player.update();
		updateScreen();
	}

	private void updateScreen() {
		if (player.getX() > Resolution.WIDTH / 2 && player.getX() + Resolution.WIDTH / 2 < Map.getWidth()) {
			Main.gameScreen.setX(-(player.getX() - Resolution.WIDTH / 2));
		}
		if (player.getY() > Resolution.HEIGHT / 2 && player.getY() + Resolution.HEIGHT / 2 < Map.getHeight()) {
			Main.gameScreen.setY(-(player.getY() - Resolution.HEIGHT / 2));
		}
	}
}
