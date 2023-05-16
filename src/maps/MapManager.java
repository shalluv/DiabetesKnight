package maps;

import application.CSVParser;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Constants.BlockConstants;
import utils.Loader;

public class MapManager {

	private Image[] mapSprite;
	private Map mapOne;

	public MapManager() {
		importImages();
		mapOne = new Map(CSVParser.readCSV("res/csv/Level_1.csv"));
	}

	public void importImages() {
		mapSprite = new Image[97];
		for (int i = 1; i <= 96; ++i) {
			mapSprite[i] = Loader.GetSpriteAtlas(Loader.TILES_ATLAS + String.format("Tile_%02d.png", i));
		}
	}

	public void draw(GraphicsContext gc) {
		int width = getCurrentMap().getMapData()[0].length;
		int height = getCurrentMap().getMapData().length;
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				int index = Math.abs(getCurrentMap().getSpriteIndex(j, i));
				if (index == 0) {
					continue;
				}
				gc.drawImage(mapSprite[index], BlockConstants.SIZE * j, BlockConstants.SIZE * i, BlockConstants.SIZE,
						BlockConstants.SIZE);
			}
		}
	}

	public Map getCurrentMap() {
		return mapOne;
	}

	public int getMapWidth() {
		return mapOne.getMapData()[0].length * BlockConstants.SIZE;
	}

	public int getMapHeight() {
		return mapOne.getMapData().length * BlockConstants.SIZE;
	}
}
