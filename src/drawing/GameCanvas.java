package drawing;

import input.InputUtility;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;

public class GameCanvas extends Canvas {

	private Image background;

	public GameCanvas(double width, double height) {
		super(width, height);
		setVisible(true);
		addListener();
		background = new Image("file:res/2_Background/Day/Background.png");
	}

	public void addListener() {
		setOnKeyPressed((KeyEvent event) -> {
			InputUtility.setKeyPressed(event.getCode(), true);
		});

		setOnKeyReleased((KeyEvent event) -> {
			InputUtility.setKeyPressed(event.getCode(), false);
		});
	}

	public void drawComponent(double layoutX, double layoutY) {
		GraphicsContext gc = getGraphicsContext2D();
		gc.drawImage(background, -layoutX, -layoutY, 1280, 960);
		// draw entities
		for (Renderable entity : RenderableHolder.getInstance().getEntities()) {
			if (!entity.isDestroyed()) {
				entity.draw(gc);
			}
		}
	}
}
