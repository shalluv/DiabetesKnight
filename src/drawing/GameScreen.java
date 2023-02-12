package drawing;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;

public class GameScreen extends Canvas {
	
	public GameScreen(double width, double height) {
		super(width, height);
		this.setVisible(true);
	}
	
	public void addListener() {
		
	}
	
	public void drawComponent() {
		GraphicsContext gc = this.getGraphicsContext2D();
		//background color
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, getWidth(), getHeight());
		//draw entities
		for(Renderable entity : RenderableHolder.getInstance().getEntities()) {
			if (!entity.isDestroyed()) {
				entity.draw(gc);
			}
		}
	}
}
