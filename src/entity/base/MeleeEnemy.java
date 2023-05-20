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

public abstract class MeleeEnemy extends Enemy {

	protected double xspeed;
	protected double yspeed;
	protected int attackState;
	protected int animationFrame;
	protected int animationState;
	protected int frameCount;
	protected Image[] animation;
	protected boolean isFacingLeft;
	protected double maxYSpeed;
	protected double weight;
	protected double initialXSpeed;
	protected double baseXSpeed;

	public MeleeEnemy(double x, double y, int width, int height, int sightSize, int initialMaxHealth,
			MeleeWeapon weapon) {
		super(x, y, width, height, sightSize, initialMaxHealth, weapon);
		attackState = READY;
		initHitbox(x, y, this.width, this.height);
	}

	protected abstract void loadResources();

	private void jump() {
		yspeed = -maxYSpeed;
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

	private boolean canAttack() {
		Rectangle2D.Double canAttackBox = new Rectangle2D.Double(hitbox.x - ((MeleeWeapon) weapon).getAttackRange(),
				hitbox.y, width + 2 * ((MeleeWeapon) weapon).getAttackRange(), height);
		return canAttackBox.intersects(GameLogic.getPlayer().getHitbox()) && Helper.IsEntityOnFloor(hitbox);
	}

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

		if (currentHealth <= 0) {
			isDestroy = true;
			weapon.cancelAttack();
		}
	}

}
