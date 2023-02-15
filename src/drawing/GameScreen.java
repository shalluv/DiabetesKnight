package drawing;

import application.MapData;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public class GameScreen extends StackPane {
	
	private GameCanvas gameCanvas;
	private double x = 0;
	private double y = 960-MapData.height;
	
	public GameScreen() {
		this.setAlignment(Pos.TOP_LEFT);
		this.setLayoutY(y);
		this.setCanvas(new GameCanvas(MapData.width, MapData.height));
		this.getChildren().add(gameCanvas);
	}
	
	public void setCanvas(GameCanvas gameCanvas) {
		this.gameCanvas=gameCanvas;
	}
	
	public GameCanvas getCanvas() {
		return gameCanvas;
	}
	
	public void drawComponent() {
		this.getCanvas().drawComponent(x, y);
		this.setLayoutX(x);
		this.setLayoutY(y);
	}
	
	public void updateX(double x) {
		this.x = x;
	}
	
	public void updateY(double y) {
		this.y = y;
	}
}
