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
import utils.Constants.GameState;
import utils.Constants.Resolution;

/**
 * GameLogic
 * Handles the game logic
 */
public class GameLogic {

	/**
	 * The game object container
	 * entities in the game are stored in this array list
	 * @see entity.base.Entity
	 * @see java.util.ArrayList
	 */
	private static ArrayList<Entity> gameObjectContainer = new ArrayList<Entity>();
	/**
	 * The player
	 * @see entity.Player
	 */
	private static Player player;
	/**
	 * Whether the game is changing level
	 */
	private boolean isChangingLevel;

	/**
	 * Constructor
	 */
	public GameLogic() {
		isChangingLevel = false;
	}

	/**
	 * Add all objects in the array list to the game
	 * @param entities the array list of entities to be added
	 * @see entity.base.Entity
	 */
	public static void addAllObject(ArrayList<Entity> entities) {
		for (Entity entity : entities) {
			addNewObject(entity);
		}
	}

	/**
	 * Add a new object to the game
	 * @param entity the entity to be added
	 * @see entity.base.Entity
	 */
	public static void addNewObject(Entity entity) {
		gameObjectContainer.add(entity);
		RenderableHolder.getInstance().add(entity);
	}

	/**
	 * Get the game object container
	 * @return the game object container
	 */
	public static ArrayList<Entity> getGameObjectContainer() {
		return gameObjectContainer;
	}

	/**
	 * Update the game logic
	 */
	public void update() {
		if (Main.gameState == GameState.CHANGING_LEVEL) {
			if (isChangingLevel)
				return;
			isChangingLevel = true;
			gameObjectContainer.removeAll(gameObjectContainer);
			RenderableHolder.getInstance().clearAll();
			Main.mapManager.nextLevel();
			Main.gameState = GameState.PLAYING;
			isChangingLevel = false;
			return;
		}
		if (InputUtility.getKeyPressed(KeyCode.ESCAPE)) {
			Main.gameState = GameState.PAUSE;
			return;
		}
		// Update the input
		InputUtility.update();

		// Update the entities
		RenderableHolder.getInstance().update();
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

		// Update the screen position
		updateScreen();
	}

	/**
	 * Update the screen position to follow the player
	 * @see utils.Constants.Resolution
	 * @see entity.Player
	 */
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
		} else if (x < Resolution.WIDTH / 2) {
			Main.gameScreen.setX(0);
		}
		if (y >= Resolution.HEIGHT / 2 && y + Resolution.HEIGHT / 2 < mapHeight) {
			Main.gameScreen.setY(-(y - Resolution.HEIGHT / 2));
		} else if (y < Resolution.HEIGHT / 2) {
			Main.gameScreen.setY(0);
		}
	}

	/**
	 * Spawn the player at the given position
	 * @param x the x position
	 * @param y the y position
	 * @see entity.Player
	 */
	public static void spawnPlayer(int x, int y) {
		player = new Player(x, y);
		addNewObject(player);
	}

	/**
	 * Teleport the player to the given position
	 * @param x the x position
	 * @param y the y position
	 * @see entity.Player
	 */
	public static void tpPlayer(double x, double y) {
		player.getHitbox().x = x;
		player.getHitbox().y = y;
	}

	/**
	 * Get the player
	 * @return the player
	 * @see entity.Player
	 */
	public static Player getPlayer() {
		return player;
	}
}
