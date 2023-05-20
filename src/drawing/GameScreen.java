package drawing;

import application.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import utils.Constants.Resolution;
import utils.Loader;

public class GameScreen extends Pane {

	private GameCanvas gameCanvas;
	private double x;
	private double y;
	ImageView[] imageViews;

	public GameScreen() {
		int mapWidth = Main.mapManager.getMapWidth();
		int mapHeight = Main.mapManager.getMapHeight();

		x = 0;
		y = Resolution.HEIGHT - mapHeight;
		setLayoutX(x);
		setLayoutY(y);

		gameCanvas = new GameCanvas(mapWidth, mapHeight);
		imageViews = new ImageView[5];
		for (int i = 0; i < 5; ++i) {
			imageViews[i] = new ImageView(
					Loader.GetSpriteAtlas(Loader.BACKGROUND_ATLAS + String.format("%d.png", i + 1)));
			getChildren().add(imageViews[i]);
		}
		getChildren().add(gameCanvas);
	}

	public void drawComponent() {
		for (int i = 0; i < 5; ++i) {
			imageViews[i].setLayoutX(-x);
			imageViews[i].setLayoutY(-y);
		}
		gameCanvas.drawComponent(x, y, this);
	}

	public GameCanvas getCanvas() {
		return gameCanvas;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
}
