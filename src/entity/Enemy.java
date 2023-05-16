package entity;

import static utils.Constants.EnemyConstants.*;

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
	private Thread attacking;
	private Image image;
	private Player player;

	public Enemy(int x, int y, Player player) {
		super(x, y, WIDTH, HEIGHT);
		this.player = player;
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
				gc.fillRect(hitbox.x - attackProgress, hitbox.y + height / 2 - 5, attackProgress + 20, 10);
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
			if (!isAttacking)
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

	private boolean isAttackHit() {
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

	private void updateAttackProgress(int value) throws InterruptedException {
		Thread.sleep(ATTACK_DELAY);
		attackProgress += value;
	}

	private void initAttackingThread() {
		attacking = new Thread(() -> {
			boolean hit = false;
			while (attackProgress <= ATTACK_RANGE) {
				try {
					updateAttackProgress(ATTACK_SPEED);
				} catch (InterruptedException e) {
					break;
				}
				if (!hit && isAttackHit())
					hit = true;
			}
			while (attackProgress > 0) {
				try {
					updateAttackProgress(-ATTACK_SPEED);
				} catch (InterruptedException e) {
					break;
				}
			}
			attackProgress = 0;
			isAttacking = false;
		});
	}

	private void attack() {
		isAttacking = true;
		initAttackingThread();
		attacking.start();
	}

	private boolean isInSight(Player player) {
		Rectangle2D.Double enemySight = new Rectangle2D.Double(hitbox.x - SIGHT_SIZE, hitbox.y - SIGHT_SIZE,
				width + 2 * SIGHT_SIZE, height - OFFSET_HITBOX_Y + 2 * SIGHT_SIZE);
		return enemySight.intersects(player.getHitbox());
	}

	private void updateXSpeed() {
		if (isInSight(player)) {
			if (player.getHitbox().x < hitbox.x && Helper.IsEntityOnFloor(
					new Rectangle2D.Double(hitbox.getMinX() - WIDTH, hitbox.y + 5 * WIDTH, WIDTH, HEIGHT))) {
				xspeed = -BASE_X_SPEED;
				attackLeft = true;
			} else if (player.getHitbox().x > hitbox.x && Helper
					.IsEntityOnFloor(new Rectangle2D.Double(hitbox.getMaxX(), hitbox.y + 5 * WIDTH, WIDTH, HEIGHT))) {
				xspeed = BASE_X_SPEED;
				attackLeft = false;
			} else {
				xspeed = INITIAL_X_SPEED;
				attackLeft = true;
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
		System.out.println("Enemy is now " + currentHealth + " hp");
	}

	public void update() {
		updateXSpeed();

		yspeed = Math.max(-MAX_Y_SPEED, Math.min(yspeed, MAX_Y_SPEED));

		move();
		if (canAttack(player) && !isAttacking)
			attack();

		if (currentHealth == 0) {
			isDestroy = true;
			if (attacking != null)
				attacking.interrupt();
		}
	}

	@Override
	public int getZ() {
		return 1;
	}

}
