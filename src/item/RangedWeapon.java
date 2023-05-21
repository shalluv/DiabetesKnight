package item;

import static utils.Constants.AttackState.READY;

import java.awt.geom.Rectangle2D;

import entity.Bullet;
import entity.base.Enemy;
import entity.base.Entity;
import javafx.scene.image.Image;
import utils.Constants.Weapon.BulletConstants;

/**
 * RangedWeapon
 * Represents a ranged weapon in the game
 * @see item.Weapon
 * @see item.derived.Gun
 */
public abstract class RangedWeapon extends Weapon {

	/**
	 * The x-coordinate of the target
	 */
	protected double targetX;
	/**
	 * The y-coordinate of the target 
	 */
	protected double targetY;
	/**
	 * The image of the weapon
	 * @see javafx.scene.image.Image
	 */
	protected Image image;

	/**
	 * Constructor
	 * @param name name of the weapon
	 * @param image image of the weapon
	 * @param XSpeedMultiplier X speed multiplier of the weapon
	 * @param YSpeedMultiplier Y speed multiplier of the weapon
	 */
	public RangedWeapon(String name, Image image, double XSpeedMultiplier, double YSpeedMultiplier) {
		super(name, image, XSpeedMultiplier, YSpeedMultiplier);
	}

	/**
	 * Update the attacking progress when the weapon's attacking state is in progress
	 * @param attacker the entity that is attacking
	 */
	protected void inProgressUpdate(Entity attacker) {
		Rectangle2D.Double hitbox = attacker.getHitbox();
		double bulletX = hitbox.getCenterX() - BulletConstants.WIDTH / 2;
		double bulletY = hitbox.getCenterY() - BulletConstants.HEIGHT / 2;
		if (attacker instanceof Enemy) {
			if (targetX > bulletX)
				bulletX = hitbox.getMaxX();
			else if (targetX < bulletX)
				bulletX = hitbox.x;
		}
		new Bullet(bulletX, bulletY, targetX, targetY, attacker);
	}

	/**
	 * Update the attacking progress when the weapon's attacking state is on cooldown
	 */
	protected void onCooldownUpdate() {
		cooldown = null;
		attackState = READY;
	}
}
