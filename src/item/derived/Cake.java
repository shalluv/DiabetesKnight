package item.derived;

import interfaces.Consumable;
import item.Item;
import javafx.scene.paint.Color;
import logic.GameLogic;

public class Cake extends Item implements Consumable {

	private int power;

	public Cake() {
		super("Cake", Color.GREEN);
		this.power = 50;
	}

	@Override
	public void consume() {
		GameLogic.getPlayer().heal(power);
	}

	@Override
	public int getPower() {
		return power;
	}

}
