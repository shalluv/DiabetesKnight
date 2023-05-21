package item;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

import entity.Player;
import entity.base.Enemy;
import entity.base.Entity;
import interfaces.Damageable;
import javafx.scene.image.Image;
import logic.GameLogic;
import utils.Helper;

/**
 * MeleeWeapon
 * Represents a melee weapon in the game
 * @see item.Weapon
 * @see item.derived.Sword
 * @see item.derived.Spear
 */
public abstract class MeleeWeapon extends Weapon {
	/**
	 * The direction of the attack of the weapon
	 */
	protected int attackDirection;
	/**
	 * The progress of the attack of the weapon
	 */
	protected int attackProgress;
	/**
	 * The attack range of the weapon
	 */
	protected int attackRange;
	/**
	 * The damage of the weapon
	 */
	protected int damage;
	/**
	 * The attack box of the weapon
	 * @see java.awt.Shape
	 */
	protected Shape attackBox;
	/**
	 * Whether the weapon has hit an entity
	 */
	protected boolean hit;
	/**
	 * Whether the weapon can hit multiple targets
	 */
	protected boolean canMultipleHit;
	/**
	 * The list of entities that the weapon has hit
	 */
	protected ArrayList<Entity> alreadyHit;

	/**
	 * Constructor
	 * @param name name of the weapon
	 * @param image image of the weapon
	 * @param attackRange attack range of the weapon
	 * @param damage damage of the weapon
	 * @param XSpeedMultiplier X speed multiplier of the weapon
	 * @param YSpeedMultiplier Y speed multiplier of the weapon
	 * @param canMultipleHit can multiple hit or not
	 */

	public MeleeWeapon(String name, Image image, int attackRange, int damage, double XSpeedMultiplier,
			double YSpeedMultiplier, boolean canMultipleHit) {
		super(name, image, XSpeedMultiplier, YSpeedMultiplier);
		this.attackRange = attackRange;
		this.damage = damage;
		this.canMultipleHit = canMultipleHit;
	}

	/**
	 * Update the attacking progress
	 * @param attacker the entity that is attacking
	 * @return the attacking progress
	 */
	protected abstract int updateProgress(Entity attacker);

	/**
	 * Update the attack box of the weapon
	 * @param attacker the entity that is attacking
	 */

	protected abstract void updateAttackBox(Entity attacker);

	/**
	 * Get the attack range of the weapon
	 * @return the attack range of the weapon
	 */
	public int getAttackRange() {
		return attackRange;
	}

	/**
	 * Update the attacking state of the weapon
	 * @param attacker the entity that is attacking
	 * @return the attacking state of the weapon
	 */
	@Override
	public int updateAttack(Entity attacker) {
		if (!hit)
			checkAttackHit(attacker);
		if (cooldown != null && cooldown.isAlive()) // cooldown then don't update progress
			return attackState;
		return updateProgress(attacker);
	}

	/**
	 * Attack the target
	 * @param targetX the x coordinate of the target
	 * @param targetY the y coordinate of the target
	 * @param attacker the entity that is attacking
	 * @return the attacking state of the weapon
	 */
	@Override
	public int attack(double targetX, double targetY, Entity attacker) {
		this.hit = false;
		this.alreadyHit = new ArrayList<Entity>();
		this.attackState = IN_PROGRESS;
		updateAttackDirection(targetX, attacker);
		updateAttackBox(attacker);
		return attackState;
	}

	/**
	 * Check if the attacker and the entity are enemies
	 * @param entity the entity that is being attacked
	 * @param attacker the entity that is attacking
	 * @return true if the player is attacking an enemy or the enemy is attacking the player, false otherwise
	 */
	protected boolean isEnemy(Entity entity, Entity attacker) {
		if ((entity instanceof Enemy && attacker instanceof Player)
				|| (entity instanceof Player && attacker instanceof Enemy))
			return true;
		return false;
	}

	/**
	 * Check if the attacker is attacking a wall
	 * @return true if the attacker is attacking a wall, false otherwise
	 */
	protected boolean isAttackingWall() {
		Rectangle2D.Double rectangleAttackBox = (Double) attackBox.getBounds2D();
		if (attackDirection == LEFT
				&& !Helper.CanMoveHere(rectangleAttackBox.getMinX(), rectangleAttackBox.getMaxY(), 1, 1))
			return true;
		if (attackDirection == RIGHT
				&& !Helper.CanMoveHere(rectangleAttackBox.getMaxX(), rectangleAttackBox.getMaxY(), 1, 1))
			return true;
		return false;
	}

	/**
	 * Check if the attacker hit an entity
	 * @param attacker the entity that is attacking
	 */
	protected void checkAttackHit(Entity attacker) {
		if (attackBox == null)
			return;
		if (attacker instanceof Damageable && ((Damageable) attacker).getHealth() < 0)
			return;
		for (Entity entity : GameLogic.getGameObjectContainer()) {
			if (!entity.isDestroyed() && entity instanceof Damageable && isEnemy(entity, attacker)) {
				if (attackBox.intersects(entity.getHitbox()) && !alreadyHit.contains(entity)) {
					((Damageable) entity).receiveDamage(damage);
					alreadyHit.add(entity);
					if (!canMultipleHit) {
						hit = true;
						return;
					}
				}
			}
		}
	}

	/**
	 * Update the attack direction of the weapon
	 * @param targetX the x coordinate of the target
	 * @param attacker the entity that is attacking
	 */
	protected void updateAttackDirection(double targetX, Entity attacker) {
		if (targetX >= attacker.getHitbox().getCenterX())
			attackDirection = RIGHT;
		else
			attackDirection = LEFT;
	}

}
