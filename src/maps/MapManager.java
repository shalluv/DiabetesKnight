package maps;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import application.CSVParser;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Constants.BlockConstants;
import utils.Constants.Resolution;
import utils.Loader;

public class MapManager {

	private Image[] mapSprite;
	private Map mapOne;

	public MapManager() {
		importImages();
		mapOne = new Map(CSVParser.readCSV("res/csv/Level_1.csv"));
	}

	public void importImages() {
		mapSprite = new Image[5];
		for (int i = 1; i <= 4; ++i) {
			mapSprite[i] = Loader.GetSpriteAtlas(Loader.TILES_ATLAS + String.format("Tiles_%02d.png", i));
		}
	}

	public void draw(GraphicsContext gc, double screenX, double screenY) {
		int cnt = 0, sz = 0;
		int width = getCurrentMap().getMapData()[0].length;
		int height = getCurrentMap().getMapData().length;
		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				int index = Math.abs(getCurrentMap().getSpriteIndex(j, i));
				if (index == 0) {
					continue;
				}
				Rectangle2D.Double block = new Rectangle2D.Double(BlockConstants.SIZE * j, BlockConstants.SIZE * i,
						BlockConstants.SIZE, BlockConstants.SIZE);
				Rectangle2D.Double screen = new Rectangle2D.Double(screenX, screenY, Resolution.WIDTH,
						Resolution.HEIGHT);
				sz++;
				if (block.intersects(screen)) {
					gc.drawImage(mapSprite[index], BlockConstants.SIZE * j, BlockConstants.SIZE * i,
							BlockConstants.SIZE, BlockConstants.SIZE);
					++cnt;
				}
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
