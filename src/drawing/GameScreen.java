package drawing;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import utils.Constants.Resolution;

public class GameScreen extends StackPane {

	private GameCanvas gameCanvas;
	private int x;
	private int y;

	public GameScreen() {
		int mapWidth = Main.mapManager.getMapWidth();
		int mapHeight = Main.mapManager.getMapHeight();
		x = 0;
		y = Resolution.HEIGHT - mapHeight;
		setAlignment(Pos.TOP_LEFT);
		setLayoutY(y);
		gameCanvas = new GameCanvas(mapWidth, mapHeight);
		getChildren().add(gameCanvas);
	}

	public void drawComponent() {
		gameCanvas.drawComponent(x, y);
		setLayoutX(x);
		setLayoutY(y);
	}

	public GameCanvas getCanvas() {
		return gameCanvas;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
