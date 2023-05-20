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

public class PauseOverlay {

	public static void update() {
		if (InputUtility.getKeyPressed(KeyCode.SPACE)) {
			Main.gameState = GameState.PLAYING;
		}
	}

	public static void draw(GraphicsContext gc, double layoutX, double layoutY) {
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFill(Color.WHITE);
		gc.fillText("Press SPACE to continue...", Resolution.WIDTH / 2 - layoutX, Resolution.HEIGHT / 2 - layoutY);

	}
}
