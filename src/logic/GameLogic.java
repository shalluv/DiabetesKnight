package logic;

import java.util.ArrayList;

import application.CSVParser;
import application.Main;
import entity.Enemy;
import entity.Player;
import entity.base.Entity;
import sharedObject.RenderableHolder;

import static utils.Constants.PlayerConstants;
import static utils.Constants.EnemyConstants;
import static utils.Constants.Resolution;

public class GameLogic {

	private ArrayList<Entity> gameObjectContainer;

	private Player player;
	private Enemy enemy;

	public GameLogic() {
		this.gameObjectContainer = new ArrayList<>();

		int[][] mapData = CSVParser.readCSV("res/csv/Level_1.csv");
		Map map = new Map(mapData);
		addAllObject(map.getMap());
		player = new Player(PlayerConstants.ORIGIN_X, PlayerConstants.ORIGIN_Y);
		enemy = new Enemy(EnemyConstants.ORIGIN_X, EnemyConstants.ORIGIN_Y);
		addNewObject(player);
		addNewObject(enemy);
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
		enemy.update(player);
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
