package ui;

import interfaces.Reloadable;
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
		int sugarLevel = GameLogic.getPlayer().getSugarLevel();

		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFill(Color.WHITE);
		gc.fillText("HP : " + Integer.toString(currentHealth) + " / " + Integer.toString(maxHealth),
				UI.GameOverlay.OFFSET_HP_X - layoutX, Resolution.HEIGHT - UI.GameOverlay.OFFSET_HP_Y - layoutY);
		gc.fillText("Power : " + Integer.toString(currentPower), UI.GameOverlay.OFFSET_POWER_X - layoutX,
				Resolution.HEIGHT - UI.GameOverlay.OFFSET_POWER_Y - layoutY);
		gc.fillText("Sugar Level : ", UI.GameOverlay.OFFSET_SUGAR_X - layoutX,
				Resolution.HEIGHT - UI.GameOverlay.OFFSET_SUGAR_Y - layoutY);
		if (sugarLevel > PlayerConstants.HYPERGLYCEMIA_SUGAR_LEVEL)
			gc.setFill(Color.RED);
		else if (sugarLevel < PlayerConstants.HYPOGLYCEMIA_SUGAR_LEVEL)
			gc.setFill(Color.YELLOW);
		gc.fillText(Integer.toString(sugarLevel),
				UI.GameOverlay.OFFSET_SUGAR_X - layoutX + UI.GameOverlay.OFFSET_SUGAR_AMOUNT_X,
				Resolution.HEIGHT - UI.GameOverlay.OFFSET_SUGAR_Y - layoutY);
		gc.setFill(Color.WHITE);

		Item[] inventory = GameLogic.getPlayer().getInventory();
		int currentInventoryFocus = GameLogic.getPlayer().getCurrentInventoryFocus();
		Item currentItem = inventory[currentInventoryFocus];
		if (currentItem != null) {
			gc.fillText(currentItem.getName(), Resolution.WIDTH / 2 - layoutX,
					Resolution.HEIGHT - UI.GameOverlay.CURRENT_ITEM_NAME_OFFSET_Y - layoutY);
		}

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
				gc.drawImage(inventory[i].getImage(),
						(Resolution.WIDTH / 2)
								+ ((i - Math.floor(PlayerConstants.INVENTORY_SIZE / 2)) * UI.GameOverlay.INVENTORY_GAP)
								- layoutX + UI.GameOverlay.INVENTORY_PADDING,
						Resolution.HEIGHT - UI.GameOverlay.OFFSET_INVENTORY_Y - layoutY
								+ UI.GameOverlay.INVENTORY_PADDING,
						UI.GameOverlay.ITEM_SIZE - UI.GameOverlay.INVENTORY_PADDING * 2,
						UI.GameOverlay.ITEM_SIZE - UI.GameOverlay.INVENTORY_PADDING * 2);
			}

			// Ammo
			gc.setFill(Color.WHITE);
			gc.save();
			if (inventory[i] instanceof Reloadable) {
				gc.setTextAlign(TextAlignment.CENTER);
				gc.setTextBaseline(VPos.CENTER);
				gc.fillText(
						Integer.toString(((Reloadable) inventory[i]).getAmmo()) + "/"
								+ Integer.toString(((Reloadable) inventory[i]).getMaxAmmo()),
						(Resolution.WIDTH / 2)
								+ ((i - Math.floor(PlayerConstants.INVENTORY_SIZE / 2)) * UI.GameOverlay.INVENTORY_GAP)
								- layoutX + UI.GameOverlay.ITEM_SIZE / 2,
						Resolution.HEIGHT - UI.GameOverlay.OFFSET_INVENTORY_Y - layoutY + UI.GameOverlay.ITEM_SIZE / 2);
			}
			gc.restore();
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
