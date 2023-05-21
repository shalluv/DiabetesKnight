package item.derived;

import static utils.Constants.InsulinConstants.POWER_AMOUNT;
import static utils.Constants.InsulinConstants.SUGAR_AMOUNT;

import entity.Player;
import interfaces.Consumable;
import item.Item;
import logic.GameLogic;
import utils.Loader;


/**
 * Insulin
 * Represents an insulin in the game
 * An insulin is a consumable item
 * @see item.Item
 * @see interfaces.Consumable
 */
public class Insulin extends Item implements Consumable {

	/**
	 * Constructor
	 */
	public Insulin() {
		super("Insulin", Loader.GetSpriteAtlas(Loader.INSULIN_ATLAS));
	}

	/**
	 * Player consumes the insulin and gains power and reduces sugar level
	 */
	@Override
	public void consume() {
		Player player = GameLogic.getPlayer();
		player.setCurrentPower(player.getCurrentPower() + POWER_AMOUNT);
		player.setSugarLevel(player.getSugarLevel() + SUGAR_AMOUNT);
		Loader.playSound(Loader.CONSUME_INSULIN_SOUND_ATLAS);
	}
}
