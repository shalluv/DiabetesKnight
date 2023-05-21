package drawing;

import application.Main;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import utils.Constants.Resolution;
import utils.Loader;

/**
 * GameScreen The screen of the game
 * 
 * @see javafx.scene.layout.Pane
 */
public class GameScreen extends Pane {

	/**
	 * The game canvas
	 * 
	 * @see drawing.GameCanvas
	 */
	private GameCanvas gameCanvas;
	/**
	 * The x position of the game screen
	 */
	private double x;
	/**
	 * The y position of the game screen
	 */
	private double y;
	/**
	 * The number of times the background should be repeated
	 */
	private int backgroundRepeatTimes;
	/**
	 * The image views of the background
	 * 
	 * @see javafx.scene.image.ImageView
	 */
	ImageView[][] imageViews;

	/**
	 * Constructor
	 */
	public GameScreen() {
		int mapWidth = Main.mapManager.getMapWidth();
		int mapHeight = Main.mapManager.getMapHeight();

		// Set the position of the game screen
		x = 0;
		y = Resolution.HEIGHT - mapHeight;
		setLayoutX(x);
		setLayoutY(y);

		// Initialize the game canvas
		gameCanvas = new GameCanvas(mapWidth, mapHeight);

		// initialize the background
		backgroundRepeatTimes = (int) Math.floor(mapWidth / Resolution.WIDTH) + 1;

		imageViews = new ImageView[5][backgroundRepeatTimes];
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < backgroundRepeatTimes; ++j) {
				if ((i == 0 || i == 3) && j > 0)
					continue;
				imageViews[i][j] = new ImageView(
						Loader.GetSpriteAtlas(Loader.BACKGROUND_ATLAS + String.format("%d.png", i + 1)));
				getChildren().add(imageViews[i][j]);
			}
		}
		getChildren().add(gameCanvas);
	}

	/**
	 * Draw the game screen
	 */
	public void drawComponent() {
		// Draw the background
		for (int i = 0; i < 5; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (imageViews[i][j] == null)
					continue;
				imageViews[i][j].setLayoutY(-y);
			}
		}
		imageViews[0][0].setLayoutX(-x);
		imageViews[0][0].setLayoutY(-y);
		for (int j = 0; j < backgroundRepeatTimes; ++j) {
			imageViews[1][j].setLayoutX(0.85 * -x + Resolution.WIDTH * j);
			imageViews[1][j].setLayoutY(1.05 * -y - 100);
		}
		for (int j = 0; j < backgroundRepeatTimes; ++j) {
			imageViews[2][j].setLayoutX(1.05 * -x - Resolution.WIDTH * j);
			imageViews[2][j].setLayoutY(1.15 * -y);
		}
		imageViews[3][0].setLayoutX(-x);
		imageViews[3][0].setLayoutY(1.1 * -y);
		for (int j = 0; j < backgroundRepeatTimes; ++j) {
			imageViews[4][j].setLayoutX(1.12 * -x - Resolution.WIDTH * j);
			imageViews[4][j].setLayoutY(0.85 * -y + 60);
		}

		// Draw the game canvas
		gameCanvas.drawComponent(x, y, this);
	}

	/**
	 * Get the game canvas
	 * 
	 * @return the game canvas
	 */
	public GameCanvas getCanvas() {
		return gameCanvas;
	}

	/**
	 * Set the x position of the game screen
	 * 
	 * @param x the x position
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Set the y position of the game screen
	 * 
	 * @param y the y position
	 */
	public void setY(double y) {
		this.y = y;
	}
}
