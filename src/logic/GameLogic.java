package logic;

import java.util.ArrayList;

import application.Main;
import entity.DroppedItem;
import entity.Player;
import entity.base.Enemy;
import entity.base.Entity;
import input.InputUtility;
import javafx.scene.input.KeyCode;
import sharedObject.RenderableHolder;
import utils.Constants.DroppedItemConstants;
import utils.Constants.Resolution;

public class GameLogic {

	private static ArrayList<Entity> gameObjectContainer = new ArrayList<Entity>();
	private static Player player;

	public GameLogic() {
	}

	public static void addAllObject(ArrayList<Entity> entities) {
		for (Entity entity : entities) {
			addNewObject(entity);
		}
	}

	public static void addNewObject(Entity entity) {
		gameObjectContainer.add(entity);
		RenderableHolder.getInstance().add(entity);
	}

	public static ArrayList<Entity> getGameObjectContainer() {
		return gameObjectContainer;
	}

	public void update() {
		if (InputUtility.getKeyPressed(KeyCode.ESCAPE)) {
			Main.gameState = 2;
			return;
		}
		RenderableHolder.getInstance().update();
		InputUtility.update();

		for (int i = gameObjectContainer.size() - 1; i >= 0; --i) {
			Entity entity = gameObjectContainer.get(i);
			if (!entity.isDestroyed())
				entity.update();
			else {
				if (entity instanceof Enemy) {
					addNewObject(new DroppedItem(entity.getHitbox().x, entity.getHitbox().y, DroppedItemConstants.SIZE,
							DroppedItemConstants.SIZE, ((Enemy) entity).getLootItem()));
				}
				gameObjectContainer.remove(i);
			}
		}
		updateScreen();
	}

	private void updateScreen() {
		int mapWidth = Main.mapManager.getMapWidth();
		int mapHeight = Main.mapManager.getMapHeight();

		double x, y;
		if (player != null) {
			x = player.getHitbox().x + player.getHitbox().width / 2;
			y = player.getHitbox().y + player.getHitbox().height / 2;
		} else {
			x = 0;
			y = 0;
		}

		if (x >= Resolution.WIDTH / 2 && x + Resolution.WIDTH / 2 < mapWidth) {
			Main.gameScreen.setX(-(x - Resolution.WIDTH / 2));
		}
		if (y >= Resolution.HEIGHT / 2 && y + Resolution.HEIGHT / 2 < mapHeight) {
			Main.gameScreen.setY(-(y - Resolution.HEIGHT / 2));
		}
	}

	public static void spawnPlayer(int x, int y) {
		player = new Player(x, y);
		addNewObject(player);
	}

	public static Player getPlayer() {
		return player;
	}
}
