package maps;

import entity.MeleeEnemy;
import entity.RangedEnemy;
import logic.GameLogic;
import utils.Constants.BlockConstants;

public class Map {

	public static final int PLAYER_SPAWN_CODE = 3000;
	public static final int MELEE_ENEMY_SPAWN_CODE = 3001;
	public static final int RANGED_ENEMY_SPAWN_CODE = 3002;

	private int[][] mapData;

	public Map(int[][] mapData) {
		System.out.println(mapData.length + " " + mapData[0].length);
		for (int i = 0; i < mapData.length; ++i) {
			for (int j = 0; j < mapData[i].length; ++j) {
				switch (mapData[i][j]) {
				case PLAYER_SPAWN_CODE:
					mapData[i][j] = 0;
					GameLogic.spawnPlayer(j * BlockConstants.SIZE, i * BlockConstants.SIZE);
					break;
				case MELEE_ENEMY_SPAWN_CODE:
					mapData[i][j] = 0;
					GameLogic.addNewObject(new MeleeEnemy(j * BlockConstants.SIZE, i * BlockConstants.SIZE));
					break;
				case RANGED_ENEMY_SPAWN_CODE:
					mapData[i][j] = 0;
					GameLogic.addNewObject(new RangedEnemy(j * BlockConstants.SIZE, i * BlockConstants.SIZE));
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
