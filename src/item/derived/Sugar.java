package item.derived;

import static utils.Constants.SugarConstants.POWER_AMOUNT;
import static utils.Constants.SugarConstants.SUGAR_AMOUNT;

import entity.Player;
import interfaces.Consumable;
import item.Item;
import logic.GameLogic;
import utils.Loader;

/**
 * Sugar
 * Represents a sugar in the game
 * A sugar is a consumable item
 * @see item.Item
 * @see interfaces.Consumable
 */
public class Sugar extends Item implements Consumable {
	
	/**
	 * Constructor
	 */
	public Sugar() {
		super("Sugar", Loader.GetSpriteAtlas(Loader.SUGAR_ATLAS));
	}

	/**
	 * Player consumes the sugar and gains power and increases sugar level
	 */
	@Override
	public void consume() {
		Player player = GameLogic.getPlayer();
		player.setCurrentPower(player.getCurrentPower() + POWER_AMOUNT);
		player.setSugarLevel(player.getSugarLevel() + SUGAR_AMOUNT);
	}
}
