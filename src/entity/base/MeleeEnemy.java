package entity.base;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.AttackState.READY;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;

import java.awt.geom.Rectangle2D;

import item.MeleeWeapon;
import javafx.scene.image.Image;
import logic.GameLogic;
import utils.Helper;

/**
 * MeleeEnemy Represents a melee enemy in the game
 * 
 * @see Enemy
 * @see MeleeWeapon
 */
public abstract class MeleeEnemy extends Enemy {

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
	 * Constructor
	 * 
	 * @param x                x coordinate of the enemy
	 * @param y                y coordinate of the enemy
	 * @param width            width of the enemy
	 * @param height           height of the enemy
	 * @param sightSize        sight size of the enemy
	 * @param initialMaxHealth initial max health of the enemy
	 * @param weapon           the weapon of the enemy
	 */
	public MeleeEnemy(double x, double y, int width, int height, int sightSize, int initialMaxHealth,
			MeleeWeapon weapon) {
		super(x, y, width, height, sightSize, initialMaxHealth, weapon);
		attackState = READY;
		initHitbox(x, y, this.width, this.height);
	}

	/**
	 * Loads the resources of the enemy
	 */
	protected abstract void loadResources();

	/**
	 * Jumps
	 */
	private void jump() {
		yspeed = -maxYSpeed;
	}

	/**
	 * Moves the enemy
	 * 
	 * @see utils.Helper#CanMoveHere(double, double, double, double)
	 * @see utils.Helper#GetEntityXPosNextToWall(Rectangle2D.Double, double)
	 * @see utils.Helper#GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Double,
	 *      double)
	 * @see jump()
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
	 * Checks if the enemy can attack
	 * 
	 * @return true if the enemy can attack
	 */
	private boolean canAttack() {
		Rectangle2D.Double canAttackBox = new Rectangle2D.Double(hitbox.x - ((MeleeWeapon) weapon).getAttackRange(),
				hitbox.y, width + 2 * ((MeleeWeapon) weapon).getAttackRange(), height);
		return canAttackBox.intersects(GameLogic.getPlayer().getHitbox()) && Helper.IsEntityOnFloor(hitbox);
	}

	/**
	 * Updates the x speed of the enemy
	 */
	private void updateXSpeed() {
		if (isInSight(GameLogic.getPlayer())) {
			if (GameLogic.getPlayer().getHitbox().getMaxX() < hitbox.getCenterX() && !moveToFalling(LEFT)) {
				xspeed = -baseXSpeed;
			} else if (GameLogic.getPlayer().getHitbox().x > hitbox.getCenterX() && !moveToFalling(RIGHT)) {
				xspeed = baseXSpeed;
			} else {
				xspeed = initialXSpeed;
			}
		} else {
			xspeed = initialXSpeed;
		}
	}

	/**
	 * Updates the enemy
	 */
	@Override
	public void update() {
		if (GameLogic.getPlayer() != null) {
			updateXSpeed();
		}
		xspeed *= weapon.getXSpeedMultiplier();
		yspeed = Math.max(-maxYSpeed, Math.min(yspeed, maxYSpeed));
		yspeed *= weapon.getYSpeedMultiplier();
		if (attackState != IN_PROGRESS)
			move();

		if (attackState != READY) {
			attackState = weapon.updateAttack(this);
		}
		if (xspeed > 0) {
			isFacingLeft = false;
		} else if (xspeed < 0) {
			isFacingLeft = true;
		}
		if (GameLogic.getPlayer() != null && canAttack() && attackState == READY) {
			attackState = weapon.attack(GameLogic.getPlayer().getHitbox().getCenterX(),
					GameLogic.getPlayer().getHitbox().getCenterY(), this);
			if (GameLogic.getPlayer().getHitbox().getMaxX() < hitbox.getMinX()) {
				isFacingLeft = true;
			} else if (GameLogic.getPlayer().getHitbox().getMinX() > hitbox.getMaxX()) {
				isFacingLeft = false;
			}
		}

		// if current health is 0 or below, destroy the enemy
		if (currentHealth <= 0) {
			isDestroy = true;
			weapon.cancelAttack();
		}
	}

}
