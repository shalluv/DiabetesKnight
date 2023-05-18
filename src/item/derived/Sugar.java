package item.derived;

import entity.Player;
import interfaces.Consumable;
import item.Item;
import javafx.scene.paint.Color;
import logic.GameLogic;

public class Sugar extends Item implements Consumable {

	private int power;

	public Sugar(int power) {
		super("Sugar", Color.YELLOW);
		this.power = power;
	}

	@Override
	public int getPower() {
		return power;
	}

	@Override
	public void consume() {
		Player player = GameLogic.getPlayer();
		player.setCurrentPower(player.getCurrentPower() + power);
	}
}
