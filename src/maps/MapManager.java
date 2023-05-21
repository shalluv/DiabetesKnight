package maps;

import static utils.Constants.Maps.TILES_AMOUNT;

import java.awt.geom.Rectangle2D;

import application.CSVParser;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import logic.GameLogic;
import utils.Constants.BlockConstants;
import utils.Constants.Resolution;
import utils.Loader;


/**
 * MapManager
 * Manages the map in the game
 * @see maps.Map
 */
public class MapManager {

	/**
	 * The sprite of the map
	 * @see javafx.scene.image.Image
	 */
	private Image[] mapSprite;
	/**
	 * The current map
	 * @see maps.Map
	 */
	private Map map;
	/**
	 * The current level
	 */
	private int level;

	/**
	 * Constructor
	 */
	public MapManager() {
		importImages();
		level = 1;
		map = new Map(CSVParser.readCSV("res/csv/Level_1.csv"));
	}

	/**
	 * Import all tiles images
	 */
	public void importImages() {
		mapSprite = new Image[TILES_AMOUNT + 1];
		for (int i = 1; i <= TILES_AMOUNT; ++i) {
			mapSprite[i] = Loader.GetSpriteAtlas(Loader.TILES_ATLAS + String.format("Tiles_%02d.png", i));
		}
	}

	/**
	 * Draw the map
	 * @param gc The graphics context
	 * @param screenX The x position of the screen
	 * @param screenY The y position of the screen
	 */
	public void draw(GraphicsContext gc, double screenX, double screenY) {
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
				if (block.intersects(screen))
					gc.drawImage(mapSprite[index], BlockConstants.SIZE * j, BlockConstants.SIZE * i,
							BlockConstants.SIZE, BlockConstants.SIZE);
			}
		}
	}

	/**
	 * Get the current map
	 * @return The current map
	 */
	public Map getCurrentMap() {
		return map;
	}

	/**
	 * Get the current map width
	 * @return The current map width
	 */
	public int getMapWidth() {
		return map.getMapData()[0].length * BlockConstants.SIZE;
	}

	/**
	 * Get the current map height
	 * @return The current map height
	 */
	public int getMapHeight() {
		return map.getMapData().length * BlockConstants.SIZE;
	}

	/**
	 * Go to the next map
	 */
	public void nextLevel() {
		level += 1;
		map = new Map(CSVParser.readCSV("res/csv/Level_" + level + ".csv"));
		GameLogic.addNewObject(GameLogic.getPlayer());
	}
}
