package item.derived;

import entity.Player;
import interfaces.Consumable;
import item.Item;
import logic.GameLogic;
import utils.Loader;

public class Sugar extends Item implements Consumable {

	private int power;

	public Sugar() {
		super("Sugar", Loader.GetSpriteAtlas(Loader.SUGAR_ATLAS));
		power = 10;
	}

	@Override
	public void consume() {
		Player player = GameLogic.getPlayer();
		player.setCurrentPower(player.getCurrentPower() + power);
	}
}
