package ui;

import application.Main;
import input.InputUtility;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import utils.Constants.GameState;
import utils.Constants.Resolution;
import utils.Constants.UI;

/**
 * MenuOverlay Handles the menu overlay
 */
public class MenuOverlay {
	/**
	 * private constructor to prevent instantiation
	 */
	private MenuOverlay() {
	}

	/**
	 * Update the menu overlay
	 */
	public static void update() {
		if (InputUtility.getKeyPressed(KeyCode.SPACE)) {
			Main.gameState = GameState.PLAYING;
		}
	}

	/**
	 * Draw the menu overlay
	 * 
	 * @param gc      the graphics context
	 * @param layoutX the layout x
	 * @param layoutY the layout y
	 */
	public static void draw(GraphicsContext gc, double layoutX, double layoutY) {
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFill(Color.WHITE);
		gc.fillText("Welcome...", Resolution.WIDTH / 2 - layoutX, Resolution.HEIGHT / 2 - layoutY - UI.FONT_SIZE * 1.5);
		gc.fillText("Press SPACE to start!", Resolution.WIDTH / 2 - layoutX, Resolution.HEIGHT / 2 - layoutY);

	}
}
