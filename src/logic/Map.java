package logic;

import static utils.Constants.BlockConstants.HEIGHT;
import static utils.Constants.BlockConstants.WIDTH;

import java.util.ArrayList;

import entity.Block;
import entity.base.Entity;

public class Map {

	private static int width;
	private static int height;
	private ArrayList<Entity> map;

	public Map(int[][] mapData) {
		width = mapData[0].length * WIDTH;
		height = mapData.length * HEIGHT;

		this.map = new ArrayList<Entity>();
		for (int i = 0; i < mapData.length; ++i) {
			for (int j = 0; j < mapData[0].length; ++j) {
				if (mapData[i][j] == 0) {
					continue;
				}
				if (mapData[i][j] > 0) {
					map.add(new Block(j * WIDTH, i * HEIGHT,
							String.format("file:res/1_Tiles/Tile_%02d.png", mapData[i][j]), true));
				} else {
					map.add(new Block(j * WIDTH, i * HEIGHT,
							String.format("file:res/1_Tiles/Tile_%02d.png", -mapData[i][j]), false));
				}
			}
		}
	}

	public ArrayList<Entity> getMap() {
		return map;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}
}
