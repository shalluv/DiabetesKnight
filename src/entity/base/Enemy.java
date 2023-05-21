package entity.base;

import static utils.Constants.Directions.LEFT;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import entity.Player;
import interfaces.Damageable;
import item.Item;
import item.Weapon;
import item.derived.Insulin;
import item.derived.Sugar;
import utils.Helper;
import utils.Loader;

public abstract class Enemy extends Entity implements Damageable {

	protected int maxHealth;
	protected int currentHealth;
	protected int sightSize;
	protected Item lootItem;
	protected Weapon weapon;

	public Enemy(double x, double y, int width, int height, int sightSize, int maxHealth, Weapon weapon) {
		super(x, y, width, height);
		this.maxHealth = maxHealth;
		this.weapon = weapon;
		this.currentHealth = maxHealth;
		this.sightSize = sightSize;
		this.lootItem = new Random().nextInt(5) != 1 ? new Sugar() : new Insulin();
	}

	@Override
	public int getZ() {
		return 2;
	}

	protected void setCurrentHealth(int value) {
		if (value < 0) {
			currentHealth = 0;
		} else if (value > maxHealth) {
			currentHealth = maxHealth;
		} else {
			currentHealth = value;
		}
	}

	protected boolean isInSight(Player player) {
		Rectangle2D.Double enemySight = new Rectangle2D.Double(hitbox.getCenterX() - sightSize,
				hitbox.getCenterY() - sightSize, 2 * sightSize, 2 * sightSize);
		return enemySight.intersects(player.getHitbox());
	}

	@Override
	public void receiveDamage(int damage) {
		setCurrentHealth(currentHealth - damage);
		if (currentHealth > 0)
			Loader.playSound(Loader.HURT_SOUND_ATLAS);
		else
			Loader.playSound(Loader.DIE_SOUND_ATLAS);
		// System.out.println("Enemy is now " + currentHealth + " hp");
	}

	public Item getLootItem() {
		return lootItem;
	}

	public int getHealth() {
		return currentHealth;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	protected boolean moveToFalling(int direction) {
		for (int i = 0; i < 6; ++i) {
			Rectangle2D.Double checkBox;
			if (direction == LEFT)
				checkBox = new Rectangle2D.Double(hitbox.x - hitbox.width, hitbox.y + i * hitbox.height, hitbox.width,
						hitbox.height);
			else
				checkBox = new Rectangle2D.Double(hitbox.getMaxX(), hitbox.y + i * hitbox.height, hitbox.width,
						hitbox.height);
			if (Helper.IsEntityOnFloor(checkBox))
				return false;
		}
		return true;
	}
}
