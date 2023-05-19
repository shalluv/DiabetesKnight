package entity;

import static utils.Constants.AttackState.READY;
import static utils.Constants.MeleeEnemyConstants.HEIGHT;
import static utils.Constants.MeleeEnemyConstants.WIDTH;
import static utils.Constants.RangedEnemyConstants.*;
import static utils.Constants.Directions.*;

import java.awt.geom.Rectangle2D;

import entity.base.Enemy;
import item.derived.Gun;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import logic.GameLogic;
import utils.Helper;

public class RangedEnemy extends Enemy {

	private double xspeed;
	private double yspeed;
	private int attackState;
	private Gun gun;

	public RangedEnemy(double x, double y) {
		super(x, y, WIDTH, HEIGHT, SIGHT_SIZE, INITIAL_MAX_HEALTH);
		attackState = READY;
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		initHitbox(x, y, width, height);
		gun = new Gun();
	}

	@Override
	public void draw(GraphicsContext gc, Rectangle2D.Double screen) {
		if (!hitbox.intersects(screen))
			return;
		gc.setFill(Color.RED);
		gc.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.BOTTOM);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(currentHealth), hitbox.x, hitbox.y);
	}

	@Override
	public void update() {
		if (GameLogic.getPlayer() != null) {
			updateXSpeed();
		}
		yspeed = Math.max(-MAX_Y_SPEED, Math.min(yspeed, MAX_Y_SPEED));
		move();
		if (attackState != READY)
			attackState = gun.updateAttack(this);
		if (GameLogic.getPlayer() != null && canAttack() && attackState == READY)
			attackState = gun.attack(GameLogic.getPlayer().getHitbox().getCenterX(),
					GameLogic.getPlayer().getHitbox().getCenterY(), this);
		if (currentHealth <= 0) {
			isDestroy = true;
			if (attackState != READY)
				gun.cancelAttack();
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
			yspeed += WEIGHT;
		} else {
			hitbox.y = Helper.GetEntityYPosUnderRoofOrAboveFloor(hitbox, yspeed);
			if (yspeed < 0) {
				yspeed = 0;
				yspeed += WEIGHT;
			}
		}
	}

	private void jump() {
		yspeed = -MAX_Y_SPEED;
	}

	private void updateXSpeed() {
		if (canAttack()) {
			xspeed = INITIAL_X_SPEED;
		} else if (isInSight(GameLogic.getPlayer())) {
			if (GameLogic.getPlayer().getHitbox().getCenterX() < hitbox.x && !moveToFalling(LEFT)) {
				xspeed = -BASE_X_SPEED;
			} else if (GameLogic.getPlayer().getHitbox().getCenterX() > hitbox.getMaxX() && !moveToFalling(RIGHT)) {
				xspeed = BASE_X_SPEED;
			} else {
				xspeed = INITIAL_X_SPEED;
			}
		} else {
			xspeed = INITIAL_X_SPEED;
		}
	}

	private boolean canAttack() {
		Rectangle2D.Double canAttackBox = new Rectangle2D.Double(hitbox.getCenterX() - ATTACK_RANGE,
				hitbox.getCenterY() - ATTACK_RANGE, 2 * ATTACK_RANGE, 2 * ATTACK_RANGE);
		if (canAttackBox.intersects(GameLogic.getPlayer().getHitbox()) && Helper.IsEntityOnFloor(hitbox)) {
			return Bullet.canBulletHit(GameLogic.getPlayer().getHitbox(), hitbox, canAttackBox);
		}
		return false;
	}
}
