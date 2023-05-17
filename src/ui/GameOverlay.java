package ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.GameLogic;
import utils.Constants.Resolution;
import utils.Constants.UI;

public class GameOverlay {

	public void update() {

	}

	public static void draw(GraphicsContext gc, double layoutX, double layoutY) {
		int currentHealth = GameLogic.player.getCurrentHealth();

		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFill(Color.WHITE);
		gc.fillText("Player HP : " + Integer.toString(currentHealth), UI.GameOverlay.OFFSET_HP_X - layoutX,
				Resolution.HEIGHT - UI.GameOverlay.OFFSET_HP_Y - layoutY);
		gc.setFont(new Font(20));

	}
}
