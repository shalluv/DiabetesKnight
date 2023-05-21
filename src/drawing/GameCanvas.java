package drawing;

import java.awt.geom.Rectangle2D;
import java.io.File;

import application.Main;
import input.InputUtility;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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

/**
 * GameCanvas The canvas of the game
 * 
 * @see javafx.scene.canvas.Canvas
 */
public class GameCanvas extends Canvas {

	/**
	 * The background music player
	 * 
	 * @see javafx.scene.media.MediaPlayer
	 */
	private MediaPlayer backgroundMusicPlayer;

	/**
	 * Constructor
	 * 
	 * @param width  the width of the canvas
	 * @param height the height of the canvas
	 */
	public GameCanvas(double width, double height) {
		super(width, height);
		setVisible(true);
		addListener();
		File file = new File(Loader.BACKGROUND_MUSIC_ATLAS);
		Media backgroundMusic = new Media(file.toURI().toString());
		backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
		backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	}

	/**
	 * Add listener to the canvas
	 */
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

	/**
	 * Draw the canvas
	 * 
	 * @param layoutX    the layout x
	 * @param layoutY    the layout y
	 * @param gameScreen the game screen
	 */
	public void drawComponent(double layoutX, double layoutY, GameScreen gameScreen) {
		GraphicsContext gc = getGraphicsContext2D();
		// clear the canvas
		gc.clearRect(-layoutX, -layoutY, Resolution.WIDTH, Resolution.HEIGHT);

		gc.setFont(new Font(UI.FONT_SIZE));
		Rectangle2D.Double screen = new Rectangle2D.Double(-layoutX, -layoutY, Resolution.WIDTH, Resolution.HEIGHT);

		switch (Main.gameState) {
		case GameState.MENU:
			MenuOverlay.draw(gc, layoutX, layoutY);
			break;

		case GameState.PLAYING:
			if (backgroundMusicPlayer.getStatus() != MediaPlayer.Status.PLAYING)
				backgroundMusicPlayer.play();
			gameScreen.setLayoutX(layoutX);
			gameScreen.setLayoutY(layoutY);

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
			backgroundMusicPlayer.pause();
			PauseOverlay.draw(gc, layoutX, layoutY);
			break;

		default:
			break;
		}
	}
}
