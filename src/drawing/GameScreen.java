package drawing;

import input.InputUtility;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;

public class GameScreen extends Canvas {
	
	private Image background;

	public GameScreen(double width, double height) {
		super(width, height);
		this.setVisible(true);
		addListener();
		background = new Image("file:res/2_Background/Day/Background.png");
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
		gc.drawImage(background, 0, 0, getWidth(), getHeight());
		// draw entities
		for (Renderable entity : RenderableHolder.getInstance().getEntities()) {
			if (!entity.isDestroyed()) {
				entity.draw(gc);
			}
		}
	}
}
