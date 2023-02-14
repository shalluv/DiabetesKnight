package drawing;

import input.InputUtility;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;

public class GameScreen extends Canvas {

	public GameScreen(double width, double height) {
		super(width, height);
		this.setVisible(true);
		addListener();
	}

	public void addListener() {
		this.setOnKeyPressed((KeyEvent event) -> {
			InputUtility.setKeyPressed(event.getCode(), true);
		});

		this.setOnKeyReleased((KeyEvent event) -> {
			InputUtility.setKeyPressed(event.getCode(), false);
		});
	}

	public void drawComponent() {
		GraphicsContext gc = this.getGraphicsContext2D();
		// background color
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, getWidth(), getHeight());
		// draw entities
		for (Renderable entity : RenderableHolder.getInstance().getEntities()) {
			if (!entity.isDestroyed()) {
				entity.draw(gc);
			}
		}
	}
}
