package drawing;

import application.MapData;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public class GameScreen extends StackPane {

	private GameCanvas gameCanvas;
	private int x = 0;
	private int y = 960 - MapData.height;

	public GameScreen() {
		setAlignment(Pos.TOP_LEFT);
		setLayoutY(y);
		setCanvas(new GameCanvas(MapData.width, MapData.height));
		getChildren().add(gameCanvas);
	}

	public GameCanvas getCanvas() {
		return gameCanvas;
	}

	public void drawComponent() {
		getCanvas().drawComponent(x, y);
		setLayoutX(x);
		setLayoutY(y);
	}

	public void setCanvas(GameCanvas gameCanvas) {
		this.gameCanvas = gameCanvas;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
