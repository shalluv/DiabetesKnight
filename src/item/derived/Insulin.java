package item.derived;

import static utils.Constants.InsulinConstants.POWER_AMOUNT;
import static utils.Constants.InsulinConstants.SUGAR_AMOUNT;

import entity.Player;
import interfaces.Consumable;
import item.Item;
import logic.GameLogic;
import utils.Loader;

public class Insulin extends Item implements Consumable {

	public Insulin() {
		super("Insulin", Loader.GetSpriteAtlas(Loader.INSULIN_ATLAS));
	}

	@Override
	public void consume() {
		Player player = GameLogic.getPlayer();
		player.setCurrentPower(player.getCurrentPower() + POWER_AMOUNT);
		player.setSugarLevel(player.getSugarLevel() + SUGAR_AMOUNT);
	}
}
