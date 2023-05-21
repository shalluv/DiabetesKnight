package item.derived;

import interfaces.Consumable;
import item.Item;
import logic.GameLogic;
import utils.Loader;

/**
 * HealthPotion
 * Represents a health potion in the game
 * A health potion is a consumable item
 * @see item.Item
 * @see interfaces.Consumable
 */
public class HealthPotion extends Item implements Consumable {

	/**
	 * The amount of health that the player will heal
	 */
	private int healthRegen;

	/**
	 * Constructor
	 */
	public HealthPotion() {
		super("Health Potion", Loader.GetSpriteAtlas(Loader.HEALTH_POTION_ATLAS));
		healthRegen = 50;
	}

	/**
	 * Player consumes the health potion and heals
	 */
	@Override
	public void consume() {
		GameLogic.getPlayer().heal(healthRegen);
	}

}
