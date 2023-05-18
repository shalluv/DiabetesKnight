package ui;

import item.Item;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
		int currentHealth = GameLogic.getPlayer().getCurrentHealth();
		int maxHealth = GameLogic.getPlayer().getMaxHealth();
		int currentPower = GameLogic.getPlayer().getCurrentPower();
		int maxPower = GameLogic.getPlayer().getMaxPower();

		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFill(Color.WHITE);
		gc.fillText("HP : " + Integer.toString(currentHealth) + " / " + Integer.toString(maxHealth),
				UI.GameOverlay.OFFSET_HP_X - layoutX, Resolution.HEIGHT - UI.GameOverlay.OFFSET_HP_Y - layoutY);
		gc.fillText("Power : " + Integer.toString(currentPower) + " / " + Integer.toString(maxPower),
				UI.GameOverlay.OFFSET_POWER_X - layoutX, Resolution.HEIGHT - UI.GameOverlay.OFFSET_POWER_Y - layoutY);

		Item[] inventory = GameLogic.getPlayer().getInventory();
		int currentInventoryFocus = GameLogic.getPlayer().getCurrentInventoryFocus();
		for (int i = 0; i < PlayerConstants.INVENTORY_SIZE; ++i) {
			// Rect Stroke
			if (currentInventoryFocus == i) {
				gc.setStroke(Color.LIME);
			} else {
				gc.setStroke(Color.WHITE);
			}
			gc.strokeRect((Resolution.WIDTH / 2)
					+ ((i - Math.floor(PlayerConstants.INVENTORY_SIZE / 2)) * UI.GameOverlay.INVENTORY_GAP) - layoutX,
					Resolution.HEIGHT - UI.GameOverlay.OFFSET_INVENTORY_Y - layoutY, UI.GameOverlay.ITEM_SIZE,
					UI.GameOverlay.ITEM_SIZE);

			// Items
			if (inventory[i] != null) {
				gc.setFill(inventory[i].getColor());
				gc.fillRect(
						(Resolution.WIDTH / 2)
								+ ((i - Math.floor(PlayerConstants.INVENTORY_SIZE / 2)) * UI.GameOverlay.INVENTORY_GAP)
								- layoutX + UI.GameOverlay.INVENTORY_PADDING,
						Resolution.HEIGHT - UI.GameOverlay.OFFSET_INVENTORY_Y - layoutY
								+ UI.GameOverlay.INVENTORY_PADDING,
						UI.GameOverlay.ITEM_SIZE - UI.GameOverlay.INVENTORY_PADDING * 2,
						UI.GameOverlay.ITEM_SIZE - UI.GameOverlay.INVENTORY_PADDING * 2);
			}

			// Numbering
			gc.setFill(Color.WHITE);
			gc.setTextAlign(TextAlignment.RIGHT);
			gc.setTextBaseline(VPos.BOTTOM);
			gc.fillText(Integer.toString(i + 1),
					(Resolution.WIDTH / 2)
							+ ((i - Math.floor(PlayerConstants.INVENTORY_SIZE / 2)) * UI.GameOverlay.INVENTORY_GAP)
							- layoutX + BlockConstants.SIZE,
					Resolution.HEIGHT - UI.GameOverlay.OFFSET_HP_Y - layoutY);

		}
	}
}
