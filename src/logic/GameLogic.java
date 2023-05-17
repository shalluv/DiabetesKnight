package logic;

import java.util.ArrayList;

import application.Main;
import entity.Enemy;
import entity.Player;
import entity.base.Entity;
import input.InputUtility;
import javafx.scene.input.KeyCode;
import sharedObject.RenderableHolder;
import utils.Constants.EnemyConstants;
import utils.Constants.PlayerConstants;
import utils.Constants.Resolution;

public class GameLogic {

	private ArrayList<Entity> gameObjectContainer;

	public static Player player;
	private Enemy enemy;

	public GameLogic() {
		this.gameObjectContainer = new ArrayList<>();

		player = new Player(PlayerConstants.INITIAL_X, PlayerConstants.INITIAL_Y);
		enemy = new Enemy(EnemyConstants.INITIAL_X, EnemyConstants.INITIAL_Y, player);
		addNewObject(player);
		addNewObject(enemy);
	}

	public void addAllObject(ArrayList<Entity> entities) {
		for (Entity entity : entities) {
			addNewObject(entity);
		}
	}

	public void addNewObject(Entity entity) {
		gameObjectContainer.add(entity);
		RenderableHolder.getInstance().add(entity);
	}

	public ArrayList<Entity> getGameObjectContainer() {
		return gameObjectContainer;
	}

	public void update() {
		if (InputUtility.getKeyPressed(KeyCode.ESCAPE)) {
			Main.gameState = 2;
			return;
		}
		RenderableHolder.getInstance().update();

		for (int i = gameObjectContainer.size() - 1; i >= 0; --i) {
			Entity entity = gameObjectContainer.get(i);
			if (!entity.isDestroyed())
				entity.update();
			else
				gameObjectContainer.remove(i);
		}
		updateScreen();
	}

	private void updateScreen() {
		int mapWidth = Main.mapManager.getMapWidth();
		int mapHeight = Main.mapManager.getMapHeight();
		if (player.getHitbox().x > Resolution.WIDTH / 2 && player.getHitbox().x + Resolution.WIDTH / 2 < mapWidth) {
			Main.gameScreen.setX(-((int) player.getHitbox().x - Resolution.WIDTH / 2));
		}
		if (player.getHitbox().y > Resolution.HEIGHT / 2 && player.getHitbox().y + Resolution.HEIGHT / 2 < mapHeight) {
			Main.gameScreen.setY(-((int) player.getHitbox().y - Resolution.HEIGHT / 2));
		}
	}
}
