package logic;

import java.util.ArrayList;

import entity.Block;
import entity.base.Entity;

public class Map {

	private ArrayList<Entity> map;

	public Map(int[][] mapData) {
		this.map = new ArrayList<Entity>();
		for (int i = 0; i < mapData.length; ++i) {
			for (int j = 0; j < mapData[0].length; ++j) {
				if (mapData[i][j] == 0) {
					continue;
				}
				if (mapData[i][j] > 0) {
					map.add(new Block(j * 40, i * 40, String.format("file:res/1_Tiles/Tile_%02d.png", mapData[i][j]),
							true));
				} else {
					map.add(new Block(j * 40, i * 40, String.format("file:res/1_Tiles/Tile_%02d.png", -mapData[i][j]),
							false));
				}

			}
		}
	}

	public ArrayList<Entity> getMap() {
		return map;
	}
}
