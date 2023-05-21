package maps;

import entity.Door;
import entity.DroppedItem;
import entity.Gunner;
import entity.Spearman;
import entity.Swordman;
import item.derived.Gun;
import item.derived.HealthPotion;
import item.derived.Spear;
import logic.GameLogic;
import utils.Constants.BlockConstants;
import utils.Constants.DroppedItemConstants;

/**
 * Map
 * Represents a map in the game
 */
public class Map {

	/**
	 * The code for player spawn
	 */
	public static final int PLAYER_SPAWN_CODE = 3000;
	/**
	 * The code for swordman spawn
	 */
	public static final int SWORDMEN_SPAWN_CODE = 3001;
	/**
	 * The code for spearman spawn
	 */
	public static final int SPEARMEN_SPAWN_CODE = 3002;
	/**
	 * The code for gunner spawn
	 */
	public static final int GUNNER_SPAWN_CODE = 3003;
	/**
	 * The code for health potion spawn
	 */
	public static final int HEALTH_POTION_SPAWN_CODE = 3004;
	/**
	 * The code for door spawn
	 */
	public static final int DOOR_SPAWN_CODE = 3005;
	/**
	 * The code for spear spawn
	 */
	public static final int SPEAR_SPAWN_CODE = 3006;
	/**
	 * The code for gun spawn
	 */
	public static final int GUN_SPAWN_CODE = 3007;

	/**
	 * The map data
	 */
	private int[][] mapData;

	/**
	 * Constructor
	 * @param mapData the map data
	 */
	public Map(int[][] mapData) {
		for (int i = 0; i < mapData.length; ++i) {
			for (int j = 0; j < mapData[i].length; ++j) {
				switch (mapData[i][j]) {
				case PLAYER_SPAWN_CODE:
					mapData[i][j] = 0;
					if (GameLogic.getPlayer() == null)
						GameLogic.spawnPlayer(j * BlockConstants.SIZE, i * BlockConstants.SIZE);
					else
						GameLogic.tpPlayer(j * BlockConstants.SIZE, i * BlockConstants.SIZE);
					break;
				case SWORDMEN_SPAWN_CODE:
					mapData[i][j] = 0;
					GameLogic.addNewObject(new Swordman(j * BlockConstants.SIZE, i * BlockConstants.SIZE));
					break;
				case SPEARMEN_SPAWN_CODE:
					mapData[i][j] = 0;
					GameLogic.addNewObject(new Spearman(j * BlockConstants.SIZE, i * BlockConstants.SIZE));
					break;
				case GUNNER_SPAWN_CODE:
					mapData[i][j] = 0;
					GameLogic.addNewObject(new Gunner(j * BlockConstants.SIZE, i * BlockConstants.SIZE));
					break;
				case HEALTH_POTION_SPAWN_CODE:
					mapData[i][j] = 0;
					GameLogic.addNewObject(new DroppedItem(j * BlockConstants.SIZE, i * BlockConstants.SIZE,
							DroppedItemConstants.SIZE, DroppedItemConstants.SIZE, new HealthPotion()));
					break;
				case DOOR_SPAWN_CODE:
					mapData[i][j] = 0;
					GameLogic.addNewObject(new Door(j * BlockConstants.SIZE, i * BlockConstants.SIZE));
					break;
				case SPEAR_SPAWN_CODE:
					mapData[i][j] = 0;
					GameLogic.addNewObject(new DroppedItem(j * BlockConstants.SIZE, i * BlockConstants.SIZE,
							DroppedItemConstants.SIZE, DroppedItemConstants.SIZE, new Spear()));
					break;
				case GUN_SPAWN_CODE:
					mapData[i][j] = 0;
					GameLogic.addNewObject(new DroppedItem(j * BlockConstants.SIZE, i * BlockConstants.SIZE,
							DroppedItemConstants.SIZE, DroppedItemConstants.SIZE, new Gun()));
					break;
				default:
					break;
				}
			}
		}
		this.mapData = mapData;

	}

	/**
	 * Get the sprite index of the block at (x, y)
	 * @param x the x coordinate of the block
	 * @param y the y coordinate of the block
	 * @return the sprite index of the block at (x, y)
	 */
	public int getSpriteIndex(int x, int y) {
		return mapData[y][x];
	}

	/**
	 * Get the map data
	 * @return the map data
	 */
	public int[][] getMapData() {
		return mapData;
	}

}
