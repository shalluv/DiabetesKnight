package entity.base;

import static utils.Constants.AttackState.READY;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;

import item.RangedWeapon;
import javafx.scene.image.Image;
import logic.GameLogic;
import utils.Helper;

public abstract class RangedEnemy extends Enemy {

	protected double xspeed;
	protected double yspeed;
	protected int attackState;
	protected RangedWeapon weapon;
	protected int animationFrame;
	protected int animationState;
	protected int frameCount;
	protected Image[] animation;
	protected boolean isFacingLeft;
	protected double maxYSpeed;
	protected double weight;
	protected double initialXSpeed;
	protected double baseXSpeed;
	protected int animationAttackCooldown;
	protected int animationIDLE;

	public RangedEnemy(double x, double y, int width, int height, int sightSize, int initialMaxHealth) {
		super(x, y, width, height, sightSize, initialMaxHealth);
		attackState = READY;
		initHitbox(x, y, width, height);
	}

	protected abstract void loadResources();

	protected abstract boolean canAttack();

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
		xspeed *= weapon.getSpeedMultiplier();
		yspeed = Math.max(-maxYSpeed, Math.min(yspeed, maxYSpeed));
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
			if (attackState != READY)
				weapon.cancelAttack();
		}
	}

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

	private void jump() {
		yspeed = -maxYSpeed;
	}

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
