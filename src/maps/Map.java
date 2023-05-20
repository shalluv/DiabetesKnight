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

public class Map {

	public static final int PLAYER_SPAWN_CODE = 3000;
	public static final int SWORDMEN_SPAWN_CODE = 3001;
	public static final int SPEARMEN_SPAWN_CODE = 3002;
	public static final int GUNNER_SPAWN_CODE = 3003;
	public static final int HEALTH_POTION_SPAWN_CODE = 3004;
	public static final int DOOR_SPAWN_CODE = 3005;
	public static final int SPEAR_SPAWN_CODE = 3006;
	public static final int GUN_SPAWN_CODE = 3007;

	private int[][] mapData;

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

	public int getSpriteIndex(int x, int y) {
		return mapData[y][x];
	}

	public int[][] getMapData() {
		return mapData;
	}

}
