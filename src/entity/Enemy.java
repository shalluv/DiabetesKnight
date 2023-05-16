package entity;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.PlayerConstants.WEIGHT;

import java.awt.geom.Rectangle2D;

import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.Helper;

public class Enemy extends Entity {

	private int maxHealth;
	private int currentHealth;
	private double xspeed;
	private double yspeed;
	private boolean isAttacking;
	private int attackProgress;
	private boolean attackLeft;
	private Image image;

	public Enemy(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		maxHealth = 100;
		currentHealth = 100;
		isAttacking = false;
		attackProgress = 0;
		initHitbox(x, y, width, height);
		image = new Image("file:res/Slime/stand_and_maybe_jump/slime2-1.png");
	}

	@Override
	public void draw(GraphicsContext gc) {
//		 Hitbox Rect
		gc.setFill(Color.RED);
		if (isAttacking) {
			if (attackLeft) {
				gc.fillRect(hitbox.x - attackProgress, hitbox.y + height / 2 - 5, attackProgress, 10);
			} else {
				gc.fillRect(hitbox.getMaxX(), hitbox.y + height / 2 - 5, attackProgress, 10);
			}
		}
		gc.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		// gc.drawImage(image, hitbox.x, hitbox.y, width, height);
	}

	private void jump() {
		yspeed = -MAX_Y_SPEED;
	}

	private void move() {
		if (Helper.CanMoveHere(hitbox.x + xspeed, hitbox.y, hitbox.width, hitbox.height)) {
			hitbox.x += xspeed;
		} else {
			hitbox.x = Helper.GetEntityXPosNextToWall(hitbox, xspeed);
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

	private boolean canAttack(Player player) {
		Rectangle2D.Double canAttackBox = new Rectangle2D.Double(hitbox.x - ATTACK_RANGE, hitbox.y,
				WIDTH + 2 * ATTACK_RANGE, HEIGHT);
		return canAttackBox.intersects(player.getHitbox()) && Helper.IsEntityOnFloor(hitbox);
	}

	private void updateAttackDirection(Player player) {
		if (hitbox.x >= player.getHitbox().x) {
			attackLeft = true;
		} else {
			attackLeft = false;
		}
	}

	private boolean isAttackHit(Player player) {
		Rectangle2D.Double attackBox;
		if (attackLeft) {
			attackBox = new Rectangle2D.Double(hitbox.x - attackProgress, hitbox.y + height / 2 - 5,
					attackProgress + 20, 10);
		} else {
			attackBox = new Rectangle2D.Double(hitbox.getMaxX(), hitbox.y + height / 2 - 5, attackProgress, 10);
		}
		if (attackBox.intersects(player.getHitbox())) {
			player.receiveDamage(DAMAGE);
			return true;
		}
		return false;
	}

	private void updateAttackProgress(int value) {
		try {
			Thread.sleep(ATTACK_DELAY);
			attackProgress += value;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void attack(Player player) {
		isAttacking = true;
		Thread attacking = new Thread(() -> {
			updateAttackDirection(player);
			while (attackProgress <= ATTACK_RANGE) {
				updateAttackProgress(ATTACK_SPEED);
				if (isAttackHit(player))
					break;
			}
			while (attackProgress > 0) {
				updateAttackProgress(-ATTACK_SPEED);
			}
			attackProgress = 0;
			isAttacking = false;
		});
		attacking.start();
	}

	private boolean isInSight(Player player) {
		Rectangle2D.Double enemySight = new Rectangle2D.Double(hitbox.x - SIGHT_SIZE, hitbox.y - SIGHT_SIZE,
				width + 2 * SIGHT_SIZE, height - OFFSET_HITBOX_Y + 2 * SIGHT_SIZE);
		return enemySight.intersects(player.getHitbox());
	}

	private void updateXSpeed(Player player) {
		if (isInSight(player)) {
			if (player.getHitbox().x < hitbox.x && Helper.IsEntityOnFloor(
					new Rectangle2D.Double(hitbox.getMinX() - WIDTH, hitbox.y + 5 * WIDTH, WIDTH, HEIGHT))) {
				xspeed = -BASE_X_SPEED;
			} else if (player.getHitbox().x > hitbox.x && Helper
					.IsEntityOnFloor(new Rectangle2D.Double(hitbox.getMaxX(), hitbox.y + 5 * WIDTH, WIDTH, HEIGHT))) {
				xspeed = BASE_X_SPEED;
			} else {
				xspeed = INITIAL_X_SPEED;
			}
		} else {
			xspeed = INITIAL_X_SPEED;
		}
	}

	private void setCurrentHealth(int value) {
		if (value < 0) {
			currentHealth = 0;
		} else if (value > maxHealth) {
			currentHealth = maxHealth;
		} else {
			currentHealth = value;
		}
	}

	public void receiveDamage(int damage) {
		setCurrentHealth(currentHealth - damage);
	}

	public void update(Player player) {
		if (isAttacking)
			return;
		updateXSpeed(player);

		yspeed = Math.max(-MAX_Y_SPEED, Math.min(yspeed, MAX_Y_SPEED));

		move();
		if (canAttack(player)) {
			attack(player);
		}
	}

	@Override
	public int getZ() {
		return 1;
	}

}
