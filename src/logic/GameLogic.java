package logic;

import java.util.ArrayList;

import application.Main;
import entity.DroppedItem;
import entity.Enemy;
import entity.Player;
import entity.base.Entity;
import input.InputUtility;
import javafx.scene.input.KeyCode;
import sharedObject.RenderableHolder;
import utils.Constants.DroppedItemConstants;
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
			else {
				if (entity instanceof Enemy) {
					addNewObject(new DroppedItem(entity.getHitbox().x, entity.getHitbox().y, DroppedItemConstants.WIDTH,
							DroppedItemConstants.HEIGHT, ((Enemy) entity).getLootItem()));
				}
				gameObjectContainer.remove(i);
			}
		}
		updateScreen();
	}

	private void updateScreen() {
		int mapWidth = Main.mapManager.getMapWidth();
		int mapHeight = Main.mapManager.getMapHeight();
		double x = player.getHitbox().x + player.getHitbox().width / 2;
		double y = player.getHitbox().y + player.getHitbox().height / 2;
		if (x >= Resolution.WIDTH / 2 && x + Resolution.WIDTH / 2 < mapWidth) {
			Main.gameScreen.setX(-(x - Resolution.WIDTH / 2));
		}
		if (y >= Resolution.HEIGHT / 2 && y + Resolution.HEIGHT / 2 < mapHeight) {
			Main.gameScreen.setY(-(y - Resolution.HEIGHT / 2));
		}
	}
}
