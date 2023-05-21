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

/**
 * Enemy
 * Represents an enemy in the game
 */
public abstract class Enemy extends Entity implements Damageable {

	/**
	 * The maximum health of the enemy
	 */
	protected int maxHealth;
	/**
	 * The current health of the enemy
	 */
	protected int currentHealth;
	/**
	 * The size of the sight of the enemy
	 */
	protected int sightSize;
	/**
	 * The item that the enemy will drop when killed
	 * @see item.Item
	 */
	protected Item lootItem;
	/**
	 * The weapon of the enemy
	 * @see item.Weapon
	 */
	protected Weapon weapon;

	/**
	 * Constructor
	 * @param x x coordinate of the enemy
	 * @param y y coordinate of the enemy
	 * @param width width of the enemy
	 * @param height height of the enemy
	 * @param sightSize size of the sight of the enemy
	 * @param maxHealth maximum health of the enemy
	 * @param weapon weapon of the enemy
	 */
	public Enemy(double x, double y, int width, int height, int sightSize, int maxHealth, Weapon weapon) {
		super(x, y, width, height);
		this.maxHealth = maxHealth;
		this.weapon = weapon;
		this.currentHealth = maxHealth;
		this.sightSize = sightSize;
		this.lootItem = new Random().nextInt(5) != 1 ? new Sugar() : new Insulin();
	}

	/**
	 * Get the z coordinate of the enemy
	 * @return 2
	 */
	@Override
	public int getZ() {
		return 2;
	}

	/**
	 * Set the current health of the enemy
	 * @param value the value to be set, cannot be less than 0 or more than maxHealth
	 */
	protected void setCurrentHealth(int value) {
		if (value < 0) {
			currentHealth = 0;
		} else if (value > maxHealth) {
			currentHealth = maxHealth;
		} else {
			currentHealth = value;
		}
	}

	/**
	 * Check if the enemy is in sight of the player
	 * @param player the player
	 * @return true if the enemy is in sight of the player, false otherwise
	 * @see Player
	 */
	protected boolean isInSight(Player player) {
		Rectangle2D.Double enemySight = new Rectangle2D.Double(hitbox.getCenterX() - sightSize,
				hitbox.getCenterY() - sightSize, 2 * sightSize, 2 * sightSize);
		return enemySight.intersects(player.getHitbox());
	}

	/**
	 * Receive damage
	 * @param damage the damage to be received
	 */
	@Override
	public void receiveDamage(int damage) {
		setCurrentHealth(currentHealth - damage);
	}

	/**
	 * Get the loot item of the enemy
	 * @return the loot item of the enemy
	 * @see Item
	 */
	public Item getLootItem() {
		return lootItem;
	}

	/**
	 * Get the current health of the enemy
	 * @return the current health of the enemy
	 */
	public int getHealth() {
		return currentHealth;
	}

	/**
	 * Get the weapon of the enemy
	 * @return the weapon of the enemy
	 * @see Weapon
	 */
	public Weapon getWeapon() {
		return weapon;
	}

	/**
	 * Check if the enemy will fall if it moves in the given direction
	 * @param direction the direction to be checked
	 * @return true if the enemy will fall, false otherwise
	 */
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
