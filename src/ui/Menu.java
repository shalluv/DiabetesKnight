package ui;

import application.Main;
import input.InputUtility;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import utils.Constants.GameState;
import utils.Constants.Resolution;
import utils.Loader;

public class Menu {

	private Image[] background = new Image[5];

	public Menu() {
		for (int i = 1; i <= 5; ++i) {
			background[i - 1] = Loader.GetSpriteAtlas(String.format("res/2_Background/Night/%d.png", i));
		}
	}

	public void update() {
		if (InputUtility.getKeyPressed(KeyCode.SPACE)) {
			Main.gameState = GameState.MENU;
		}
	}

	public void draw(GraphicsContext gc) {
		for (int i = 0; i < 5; ++i) {
			gc.drawImage(background[i], 0, 0, Resolution.WIDTH, Resolution.HEIGHT);
		}

		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFill(Color.RED);
		gc.fillText("Press SPACE to continue...", Resolution.WIDTH / 2, Resolution.HEIGHT / 2);
		gc.setFont(new Font(20));

	}
}
