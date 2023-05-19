package item.derived;

import interfaces.Consumable;
import item.Item;
import logic.GameLogic;
import utils.Loader;

public class HealthPotion extends Item implements Consumable {

	private int healthRegen = 50;

	public HealthPotion() {
		super("Health Potion", Loader.GetSpriteAtlas(Loader.HEALTH_POTION_ATLAS));
		healthRegen = 50;
	}

	@Override
	public void consume() {
		GameLogic.getPlayer().heal(healthRegen);
	}

}
