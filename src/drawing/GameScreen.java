package drawing;

import application.Main;
import javafx.scene.layout.Pane;
import utils.Constants.Resolution;

public class GameScreen extends Pane {

	private GameCanvas gameCanvas;
	private double x;
	private double y;

	public GameScreen() {
		int mapWidth = Main.mapManager.getMapWidth();
		int mapHeight = Main.mapManager.getMapHeight();

		x = 0;
		y = Resolution.HEIGHT - mapHeight;
		setLayoutY(y);

		gameCanvas = new GameCanvas(mapWidth, mapHeight);
		getChildren().add(gameCanvas);
	}

	public void drawComponent() {
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
