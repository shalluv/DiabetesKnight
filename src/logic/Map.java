package logic;

import java.util.ArrayList;

public class Map {

	private ArrayList<Entity> map;

	public Map(String[] mapData) {
		this.map = new ArrayList<Entity>();
		for (int i = 0; i < mapData.length; ++i) {
			String line = mapData[i];
			for (int j = 0; j < line.length(); ++j) {
				switch (line.charAt(j)) {
				case '0':
					break;
				case '1':
					map.add(new Entity(j*40, i*40));
					break;
				default:
					System.out.println("Error parsing at position x = " + j + " y = " + i
							+ ".\nUnknown Object with Symbol " + line.charAt(j));
					break;
				}
			}
		}
	}

	public ArrayList<Entity> getMap() {
		return map;
	}
}
