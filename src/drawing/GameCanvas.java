package drawing;

import application.Main;
import input.InputUtility;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;
import utils.Constants.Resolution;
import utils.Loader;

public class GameCanvas extends Canvas {

	private Image background;

	public GameCanvas(double width, double height) {
		super(width, height);
		setVisible(true);
		addListener();
		background = Loader.GetSpriteAtlas(Loader.BACKGROUND_ATLAS);
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
		gc.drawImage(background, -layoutX, -layoutY, Resolution.WIDTH, Resolution.HEIGHT);
		Main.mapManager.draw(gc);
		// draw entities
		for (Renderable entity : RenderableHolder.getInstance().getEntities()) {
			if (!entity.isDestroyed()) {
				entity.draw(gc);
			}
		}
	}
}
