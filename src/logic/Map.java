package logic;

import java.util.ArrayList;

import entity.Block;
import entity.base.Entity;

public class Map {

	private static int width;
	private static int height;

	private ArrayList<Entity> map;

	public Map(int[][] mapData) {
		width = mapData[0].length * 40;
		height = mapData.length * 40;

		this.setMap(new ArrayList<Entity>());
		for (int i = 0; i < mapData.length; ++i) {
			for (int j = 0; j < mapData[0].length; ++j) {
				if (mapData[i][j] == 0) {
					continue;
				}
				if (mapData[i][j] > 0) {
					getMap().add(new Block(j * 40, i * 40, String.format("file:res/1_Tiles/Tile_%02d.png", mapData[i][j]),
							true));
				} else {
					getMap().add(new Block(j * 40, i * 40, String.format("file:res/1_Tiles/Tile_%02d.png", -mapData[i][j]),
							false));
				}
			}
		}
	}

	public static int getHeight() {
		return height;
	}

	public static int getWidth() {
		return width;
	}

	public ArrayList<Entity> getMap() {
		return map;
	}
}
