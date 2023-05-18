package ui;

import application.Main;
import input.InputUtility;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import utils.Constants.GameState;
import utils.Constants.Resolution;
import utils.Loader;

public class PauseOverlay {

	private static Image[] background = new Image[5];

	public static void loadResources() {
		for (int i = 5; i <= 5; ++i) {
			background[i - 1] = Loader.GetSpriteAtlas(String.format("res/2_Background/Night/%d.png", i));
		}
	}

	public static void update() {
		if (InputUtility.getKeyPressed(KeyCode.SPACE)) {
			Main.gameState = GameState.PLAYING;
		}
	}

	public static void draw(GraphicsContext gc, double layoutX, double layoutY) {
		for (int i = 4; i < 5; ++i) {
			gc.drawImage(background[i], -layoutX, -layoutY, Resolution.WIDTH, Resolution.HEIGHT);
		}

		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFill(Color.RED);
		gc.fillText("Press SPACE to continue...", Resolution.WIDTH / 2 - layoutX, Resolution.HEIGHT / 2 - layoutY);

	}
}