package maps;

public class Map {

	private int[][] mapData;

	public Map(int[][] mapData) {
		this.mapData = mapData;
	}

	public int getSpriteIndex(int x, int y) {
		return mapData[y][x];
	}

	public int[][] getMapData() {
		return mapData;
	}

}
