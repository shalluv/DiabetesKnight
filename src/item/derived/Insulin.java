package item.derived;

import entity.Player;
import interfaces.Consumable;
import item.Item;
import logic.GameLogic;
import utils.Loader;

public class Insulin extends Item implements Consumable {

	private int power;

	public Insulin() {
		super("Insulin", Loader.GetSpriteAtlas(Loader.INSULIN_ATLAS));
		power = 30;
	}

	@Override
	public void consume() {
		Player player = GameLogic.getPlayer();
		player.setCurrentPower(player.getCurrentPower() - power);
	}
}
