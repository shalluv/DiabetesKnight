package drawing;

import java.awt.geom.Rectangle2D;

import application.Main;
import input.InputUtility;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Font;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;
import ui.GameOverlay;
import ui.MenuOverlay;
import ui.PauseOverlay;
import utils.Constants.GameState;
import utils.Constants.Resolution;
import utils.Constants.UI;
import utils.Loader;

public class GameCanvas extends Canvas {

	private Image[] backgroundImage = new Image[5];

	public GameCanvas(double width, double height) {
		super(width, height);
		setVisible(true);
		addListener();
		for (int i = 1; i <= 5; ++i) {
			backgroundImage[i - 1] = Loader.GetSpriteAtlas(Loader.BACKGROUND_ATLAS + String.format("%d.png", i));
		}
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
		setOnScroll((ScrollEvent event) -> {
			InputUtility.setScrollDeltaY(event.getDeltaY());
		});
	}

	public void drawBackground(GraphicsContext gc, double layoutX, double layoutY) {
		for (int i = 0; i < 5; ++i)
			gc.drawImage(backgroundImage[i], layoutX, layoutY, Resolution.WIDTH, Resolution.HEIGHT);
	}

	public void drawComponent(double layoutX, double layoutY, GameScreen gameScreen) {
		GraphicsContext gc = getGraphicsContext2D();
		Rectangle2D.Double screen = new Rectangle2D.Double(-layoutX, -layoutY, Resolution.WIDTH, Resolution.HEIGHT);
		gc.setFont(new Font(UI.FONT_SIZE));
		switch (Main.gameState) {
		case GameState.MENU:
			gameScreen.setLayoutX(0);
			gameScreen.setLayoutY(0);

			drawBackground(gc, 0, 0);

			MenuOverlay.draw(gc);
			break;
		case GameState.PLAYING:
			gameScreen.setLayoutX(layoutX);
			gameScreen.setLayoutY(layoutY);

			drawBackground(gc, -layoutX, -layoutY);

			Main.mapManager.draw(gc, -layoutX, -layoutY);
			// draw entities
			for (Renderable entity : RenderableHolder.getInstance().getEntities()) {
				if (!entity.isDestroyed()) {
					entity.draw(gc, screen);
				}
			}
			GameOverlay.draw(gc, layoutX, layoutY);
			break;
		case GameState.PAUSE:

			drawBackground(gc, -layoutX, -layoutY);

			Main.mapManager.draw(gc, -layoutX, -layoutY);
			// draw entities
			for (Renderable entity : RenderableHolder.getInstance().getEntities()) {
				if (!entity.isDestroyed()) {
					entity.draw(gc, screen);
				}
			}

			PauseOverlay.draw(gc, layoutX, layoutY);
			break;
		default:
			break;
		}
	}
}
