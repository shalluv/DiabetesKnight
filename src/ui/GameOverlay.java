package ui;

import item.Item;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import logic.GameLogic;
import utils.Constants.BlockConstants;
import utils.Constants.PlayerConstants;
import utils.Constants.Resolution;
import utils.Constants.UI;

public class GameOverlay {

	public void update() {
	}

	public static void draw(GraphicsContext gc, double layoutX, double layoutY) {
		int currentHealth = GameLogic.player.getCurrentHealth();
		int maxHealth = GameLogic.player.getMaxHealth();
		int currentPower = GameLogic.player.getCurrentPower();
		int maxPower = GameLogic.player.getMaxPower();

		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFill(Color.WHITE);
		gc.setFont(new Font(20));
		gc.fillText("HP : " + Integer.toString(currentHealth) + " / " + Integer.toString(maxHealth),
				UI.GameOverlay.OFFSET_HP_X - layoutX, Resolution.HEIGHT - UI.GameOverlay.OFFSET_HP_Y - layoutY);
		gc.fillText("Power : " + Integer.toString(currentPower) + " / " + Integer.toString(maxPower),
				UI.GameOverlay.OFFSET_HP_X - layoutX, Resolution.HEIGHT - UI.GameOverlay.OFFSET_HP_Y - layoutY + 30);

		Item[] inventory = GameLogic.player.getInventory();
		int currentInventoryFocus = GameLogic.player.getCurrentInventoryFocus();
		for (int i = 0; i < PlayerConstants.INVENTORY_SIZE; ++i) {
			// Rect Stroke
			if (currentInventoryFocus == i) {
				gc.setStroke(Color.LIME);
			} else {
				gc.setStroke(Color.WHITE);
			}
			gc.strokeRect((Resolution.WIDTH / 2) + ((i - 4) * 50) - layoutX,
					Resolution.HEIGHT - UI.GameOverlay.OFFSET_HP_Y - layoutY, BlockConstants.SIZE, BlockConstants.SIZE);

			if (inventory[i] != null) {
				gc.setFill(inventory[i].getColor());
				gc.fillRect((Resolution.WIDTH / 2) + ((i - 4) * 50) - layoutX + 2,
						Resolution.HEIGHT - UI.GameOverlay.OFFSET_HP_Y - layoutY + 2, 36, 36);
			}

			// Numbering
			gc.setFill(Color.WHITE);
			gc.setTextAlign(TextAlignment.RIGHT);
			gc.setTextBaseline(VPos.BOTTOM);
			gc.fillText(Integer.toString(i + 1), (Resolution.WIDTH / 2) + ((i - 4) * 50) - layoutX + 40,
					Resolution.HEIGHT - UI.GameOverlay.OFFSET_HP_Y - layoutY);

		}
	}
}
