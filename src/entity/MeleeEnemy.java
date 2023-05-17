package entity;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.PlayerConstants.MELEE_ATTACK_DELAY;
import static utils.Constants.PlayerConstants.MELEE_ATTACK_RANGE;
import static utils.Constants.PlayerConstants.MELEE_ATTACK_SPEED;
import static utils.Constants.Directions.*;
import static utils.Constants.AttackState.*;

import java.awt.geom.Rectangle2D;

import entity.base.Enemy;
import item.Item;
import item.derived.Sugar;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import utils.Helper;

public class MeleeEnemy extends Enemy {

	private double xspeed;
	private double yspeed;
	private int attackState;
	private int attackProgress;
	private int attackDirection;
	private Rectangle2D.Double attackBox;
	// private Image image;
	private Item lootItem;
	private Player player;

	public MeleeEnemy(double x, double y, Player player) {
		super(x, y, WIDTH, HEIGHT);
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		attackState = READY;
		attackProgress = 0;
		this.player = player;
		initHitbox(x, y, width, height);
		lootItem = new Sugar();
		attackDirection = LEFT;
		// image = new Image("file:res/Slime/stand_and_maybe_jump/slime2-1.png");
	}

	@Override
	public void draw(GraphicsContext gc) {
//		 Hitbox Rect
		gc.setFill(Color.RED);
		if (attackState != READY) {
			switch (attackDirection) {
			case LEFT:
				gc.fillRect(hitbox.x - attackProgress, hitbox.getCenterY() - ATTACK_BOX_HEIGHT / 2,
						attackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
				break;
			case RIGHT:
				gc.fillRect(hitbox.getCenterX(), hitbox.getCenterY() - ATTACK_BOX_HEIGHT / 2,
						attackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
				break;
			default:
				break;
			}
		}
		gc.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

		// draw HP
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.BOTTOM);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(currentHealth), hitbox.x, hitbox.y);
		gc.setFont(new Font(20));
//		 gc.drawImage(image, hitbox.x, hitbox.y, width, height);
	}

	private void jump() {
		yspeed = -MAX_Y_SPEED;
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

	private void updateAttackBox() {
		switch (attackDirection) {
		case LEFT:
			attackBox = new Rectangle2D.Double(hitbox.x - attackProgress, hitbox.getCenterY() - ATTACK_BOX_HEIGHT / 2,
					attackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
			break;
		case RIGHT:
			attackBox = new Rectangle2D.Double(hitbox.getCenterX(), hitbox.getCenterY() - ATTACK_BOX_HEIGHT / 2,
					attackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
			break;
		default:
			break;
		}
	}

	private boolean isAttackingWall() {
		if (attackDirection == LEFT
				&& !Helper.CanMoveHere(attackBox.x - attackProgress, attackBox.y, attackBox.width, attackBox.height))
			return true;
		if (attackDirection == RIGHT && !Helper.CanMoveHere(attackBox.x + hitbox.width / 2, attackBox.y,
				attackBox.width - hitbox.width / 2 + attackProgress, attackBox.height))
			return true;
		return false;
	}

	private void checkAttackHit() {
		if (attackBox.intersects(player.getHitbox()) && !Thread.interrupted()) {
			player.receiveDamage(MELEE_DAMAGE);
			attackState = MELEE_HIT;
		}
	}

	private void updateMeleeAttack() {
		if (attackCooldown != null && attackCooldown.isAlive())
			return;
		if (attackState == MELEE_IN_PROGRESS || attackState == MELEE_HIT) {
			if (attackProgress < MELEE_ATTACK_RANGE) {
				if (attackCooldown == null || !attackCooldown.isAlive())
					initAttackCooldown(MELEE_ATTACK_DELAY);
				attackProgress += MELEE_ATTACK_SPEED;
			}
			updateAttackBox();
			if (isAttackingWall()) {
				attackProgress -= MELEE_ATTACK_SPEED;
				attackState = MELEE_ON_COOLDOWN;
			}
			if (attackState != MELEE_HIT)
				checkAttackHit();
			attackCooldown.start();
			if (attackProgress >= MELEE_ATTACK_RANGE)
				attackState = MELEE_ON_COOLDOWN;
		} else if (attackState == MELEE_ON_COOLDOWN) {
			if (attackProgress > 0) {
				if (attackCooldown == null || !attackCooldown.isAlive())
					initAttackCooldown(MELEE_ATTACK_DELAY);
				attackProgress -= MELEE_ATTACK_SPEED;
				attackCooldown.start();
			} else {
				attackCooldown = null;
				attackProgress = 0;
				attackState = READY;
			}
		}
	}

	private void attack() {
		attackState = MELEE_IN_PROGRESS;
	}

	private void updateXSpeed() {
		if (isInSight(player)) {
			if (player.getHitbox().getMaxX() + MELEE_ATTACK_RANGE / 2 < hitbox.x && Helper
					.IsEntityOnFloor(new Rectangle2D.Double(hitbox.x - WIDTH, hitbox.y + 3 * HEIGHT, WIDTH, HEIGHT))) {
				xspeed = -BASE_X_SPEED;
			} else if (player.getHitbox().x > hitbox.getMaxX() + MELEE_ATTACK_RANGE / 2 && Helper
					.IsEntityOnFloor(new Rectangle2D.Double(hitbox.getMaxX(), hitbox.y + 3 * HEIGHT, WIDTH, HEIGHT))) {
				xspeed = BASE_X_SPEED;
			} else {
				xspeed = INITIAL_X_SPEED;
			}
		} else {
			xspeed = INITIAL_X_SPEED;
		}
	}

	private void updateAttackDirection() {
		double playerCenterX = player.getHitbox().getCenterX();
		double enemyCenterX = hitbox.getCenterX();
		if (playerCenterX < enemyCenterX)
			attackDirection = LEFT;
		else
			attackDirection = RIGHT;
	}

	@Override
	public void update() {
		updateXSpeed();
		updateAttackDirection();

		yspeed = Math.max(-MAX_Y_SPEED, Math.min(yspeed, MAX_Y_SPEED));

		move();
		if (attackState == MELEE_IN_PROGRESS || attackState == MELEE_HIT || attackState == MELEE_ON_COOLDOWN)
			updateMeleeAttack();
		if (canAttack(player) && attackState == READY)
			attack();

		if (currentHealth == 0) {
			isDestroy = true;
			if (attackCooldown != null)
				attackCooldown.interrupt();
		}
	}

	@Override
	public Item getLootItem() {
		return lootItem;
	}

}
