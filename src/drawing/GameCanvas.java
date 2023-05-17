package drawing;

import application.Main;
import input.InputUtility;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;
import ui.GameOverlay;
import ui.Menu;
import ui.Pause;
import utils.Constants.GameState;
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
		setOnMousePressed((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY)
				InputUtility.mouseLeftDown();
			else if (event.getButton() == MouseButton.SECONDARY)
				InputUtility.mouseRightDown();
		});
		setOnMouseReleased((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY)
				InputUtility.mouseLeftUp();
			else if (event.getButton() == MouseButton.SECONDARY)
				InputUtility.mouseRightUp();
		});
	}

	public void drawComponent(double layoutX, double layoutY, GameScreen gameScreen) {
		GraphicsContext gc = getGraphicsContext2D();

		switch (Main.gameState) {
		case GameState.MENU:
			gameScreen.setLayoutX(0);
			gameScreen.setLayoutY(0);

			Menu.draw(gc);
			break;
		case GameState.PLAYING:
			gameScreen.setLayoutX(layoutX);
			gameScreen.setLayoutY(layoutY);

			gc.drawImage(background, -layoutX, -layoutY, Resolution.WIDTH, Resolution.HEIGHT);
			Main.mapManager.draw(gc);
			// draw entities
			for (Renderable entity : RenderableHolder.getInstance().getEntities()) {
				if (!entity.isDestroyed()) {
					entity.draw(gc);
				}
			}
			GameOverlay.draw(gc, layoutX, layoutY);
			break;
		case GameState.PAUSE:
			gc.drawImage(background, -layoutX, -layoutY, Resolution.WIDTH, Resolution.HEIGHT);
			Main.mapManager.draw(gc);
			// draw entities
			for (Renderable entity : RenderableHolder.getInstance().getEntities()) {
				if (!entity.isDestroyed()) {
					entity.draw(gc);
				}
			}

			Pause.draw(gc, layoutX, layoutY);
			break;
		default:
			break;
		}
	}
}
