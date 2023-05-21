package entity.base;

import static utils.Constants.AttackState.READY;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;

import entity.Player;
import item.RangedWeapon;
import javafx.scene.image.Image;
import logic.GameLogic;
import utils.Helper;

/**
 * RangedEnemy Represents a ranged enemy in the game
 * 
 * @see Enemy
 * @see RangedWeapon
 */
public abstract class RangedEnemy extends Enemy {

	/**
	 * The x-axis speed of the enemy
	 */
	protected double xspeed;
	/**
	 * The y-axis speed of the enemy
	 */
	protected double yspeed;
	/**
	 * The state of the attack of the enemy
	 */
	protected int attackState;
	/**
	 * The frame of the animation of the enemy
	 */
	protected int animationFrame;
	/**
	 * The state of the animation of the enemy
	 */
	protected int animationState;
	/**
	 * The counter of the enemy's animation frame
	 */
	protected int frameCount;
	/**
	 * The animation of the enemy
	 * 
	 * @see javafx.scene.image.Image
	 */
	protected Image[] animation;
	/**
	 * Whether the enemy is facing left
	 */
	protected boolean isFacingLeft;
	/**
	 * The maximum y-axis speed of the enemy
	 */
	protected double maxYSpeed;
	/**
	 * The weight of the enemy
	 */
	protected double weight;
	/**
	 * The initial x-axis speed of the enemy
	 */
	protected double initialXSpeed;
	/**
	 * The base x-axis speed of the enemy
	 */
	protected double baseXSpeed;
	/**
	 * The state of animation of the enemy when it is on cooldown after attacking
	 */
	protected int animationAttackCooldown;
	/**
	 * The state of animation of the enemy when it is idle
	 */
	protected int animationIDLE;

	/**
	 * Constructor
	 * 
	 * @param x                the x position
	 * @param y                the y position
	 * @param width            the width
	 * @param height           the height
	 * @param sightSize        the sight size
	 * @param initialMaxHealth the initial max health
	 * @param weapon           the weapon
	 */
	public RangedEnemy(double x, double y, int width, int height, int sightSize, int initialMaxHealth,
			RangedWeapon weapon) {
		super(x, y, width, height, sightSize, initialMaxHealth, weapon);
		attackState = READY;
		initHitbox(x, y, width, height);
	}

	/**
	 * Loads the resources of the enemy
	 */
	protected abstract void loadResources();

	/**
	 * Checks if the enemy can attack
	 * 
	 * @return true if the enemy can attack, false otherwise
	 */
	protected abstract boolean canAttack();

	/**
	 * Updates the enemy
	 */
	@Override
	public void update() {
		if (GameLogic.getPlayer() != null) {
			updateXSpeed();
			if (GameLogic.getPlayer().getHitbox().getMinX() > hitbox.getMaxX()) {
				isFacingLeft = false;
			} else if (GameLogic.getPlayer().getHitbox().getMaxX() < hitbox.getMinX()) {
				isFacingLeft = true;
			}
		}

		xspeed *= weapon.getXSpeedMultiplier();
		yspeed = Math.max(-maxYSpeed, Math.min(yspeed, maxYSpeed));
		yspeed *= weapon.getYSpeedMultiplier();

		move();

		if (attackState != READY) {
			animationState = animationAttackCooldown;
			attackState = weapon.updateAttack(this);
		} else {
			animationState = animationIDLE;
		}

		if (GameLogic.getPlayer() != null && canAttack() && attackState == READY)
			attackState = weapon.attack(GameLogic.getPlayer().getHitbox().getCenterX(),
					GameLogic.getPlayer().getHitbox().getCenterY(), this);
		if (currentHealth <= 0) {
			isDestroy = true;
			weapon.cancelAttack();
		}
	}

	/**
	 * Moves the enemy
	 * 
	 * @see Helper#CanMoveHere(double, double, double, double)
	 * @see Helper#GetEntityXPosNextToWall(Hitbox, double)
	 * @see Helper#GetEntityYPosUnderRoofOrAboveFloor(Hitbox, double)
	 * @see #jump()
	 * @see #updateXSpeed()
	 */
	private void move() {
		if (Helper.CanMoveHere(hitbox.x + xspeed, hitbox.y, hitbox.width, hitbox.height)) {
			hitbox.x += xspeed;
		} else {
			hitbox.x = Helper.GetEntityXPosNextToWall(hitbox, xspeed);
			if (attackState == READY)
				jump();
		}
		if (Helper.CanMoveHere(hitbox.x, hitbox.y + yspeed, hitbox.width, hitbox.height)) {
			hitbox.y += yspeed;
			yspeed += weight;
		} else {
			hitbox.y = Helper.GetEntityYPosUnderRoofOrAboveFloor(hitbox, yspeed);
			if (yspeed < 0) {
				yspeed = 0;
				yspeed += weight;
			}
		}
	}

	/**
	 * Makes the enemy jump
	 */
	private void jump() {
		yspeed = -maxYSpeed;
	}

	/**
	 * Updates the x speed of the enemy
	 * 
	 * @see #canAttack()
	 * @see entity.base.Enemy#isInSight(Player)
	 * @see #moveToFalling(int)
	 */
	private void updateXSpeed() {
		if (canAttack()) {
			xspeed = initialXSpeed;
		} else if (isInSight(GameLogic.getPlayer())) {
			if (GameLogic.getPlayer().getHitbox().getCenterX() < hitbox.x && !moveToFalling(LEFT)) {
				xspeed = -baseXSpeed;
			} else if (GameLogic.getPlayer().getHitbox().getCenterX() > hitbox.getMaxX() && !moveToFalling(RIGHT)) {
				xspeed = baseXSpeed;
			} else {
				xspeed = initialXSpeed;
			}
		} else {
			xspeed = initialXSpeed;
		}
	}
}
