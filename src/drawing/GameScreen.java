package drawing;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import logic.Map;

public class GameScreen extends StackPane {

	private GameCanvas gameCanvas;
	private int x = 0;
	private int y = 960 - Map.getHeight();

	public GameScreen() {
		setAlignment(Pos.TOP_LEFT);
		setLayoutY(y);
		setCanvas(new GameCanvas(Map.getWidth(), Map.getHeight()));
		getChildren().add(getGameCanvas());
	}

	public void drawComponent() {
		getCanvas().drawComponent(x, y);
		setLayoutX(x);
		setLayoutY(y);
	}

	public GameCanvas getCanvas() {
		return getGameCanvas();
	}

	public GameCanvas getGameCanvas() {
		return gameCanvas;
	}

	public void setCanvas(GameCanvas gameCanvas) {
		this.setGameCanvas(gameCanvas);
	}

	public void setGameCanvas(GameCanvas gameCanvas) {
		this.gameCanvas = gameCanvas;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
