package item.derived;

import entity.Player;
import interfaces.Consumable;
import item.Item;
import javafx.scene.paint.Color;
import logic.GameLogic;

public class Sugar extends Item implements Consumable {

	public Sugar() {
		super("Sugar", 10, Color.YELLOW);
	}

	@Override
	public void consume() {
		Player player = GameLogic.player;
		player.setCurrentPower(player.getCurrentPower() + power);
	}
}
