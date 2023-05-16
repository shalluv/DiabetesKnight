package entity;

import static utils.Constants.PlayerConstants.*;
import static utils.Constants.BulletConstants;

import java.awt.geom.Rectangle2D;

import application.Main;
import entity.base.Entity;
import input.InputUtility;
import interfaces.Damageable;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import utils.Helper;

public class Player extends Entity implements Damageable {

	private int maxHealth;
	private int currentHealth;
	private double xspeed;
	private double yspeed;
	private boolean attackLeft;
	private boolean isAttacking;
	private int meleeAttackProgress;
	private Thread attacking;
	private Image image;
	private Rectangle2D.Double meleeAttackBox;

	public Player(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		initHitbox(x, y, WIDTH, HEIGHT);
		image = new Image("file:res/Owlet_Monster/Owlet_Monster.png");
		maxHealth = 100;
		isAttacking = false;
		meleeAttackProgress = 0;
		currentHealth = 100;
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		if (isAttacking) {
			if (attackLeft) {
				gc.fillRect(hitbox.x - meleeAttackProgress, hitbox.y + height / 2 - 5, meleeAttackProgress + 20, 10);
			} else {
				gc.fillRect(hitbox.getMaxX(), hitbox.y + height / 2 - 5, meleeAttackProgress, 10);
			}
		}
		gc.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
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

	@Override
	public void receiveDamage(int damage) {
		if (damage < 0)
			damage = 0;
		setCurrentHealth(currentHealth - damage);
		System.out.println("player is now " + currentHealth + " hp");
	}

	public Rectangle2D.Double getHitbox() {
		return hitbox;
	}

	private void jump() {
		yspeed = -MAX_Y_SPEED;
	}

	private void move() {
		if (Helper.CanMoveHere(hitbox.x + xspeed, hitbox.y, hitbox.width, hitbox.height)) {
			hitbox.x += xspeed;
		} else {
			hitbox.x = Helper.GetEntityXPosNextToWall(hitbox, xspeed);
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

	private void updateMeleeAttackProgress(int value) throws InterruptedException {
		Thread.sleep(ATTACK_DELAY);
		meleeAttackProgress += value;
	}

	private void updateMeleeAttackBox() {
		if (attackLeft) {
			meleeAttackBox = new Rectangle2D.Double(hitbox.x - meleeAttackProgress, hitbox.y + height / 2 - 5,
					meleeAttackProgress + 20, 10);
		} else {
			meleeAttackBox = new Rectangle2D.Double(hitbox.getMaxX(), hitbox.y + height / 2 - 5, meleeAttackProgress,
					10);
		}
	}

	private boolean isMeleeAttackingWall() {
		if (attackLeft && !Helper.CanMoveHere(meleeAttackBox.x - meleeAttackProgress, meleeAttackBox.y,
				meleeAttackBox.width, meleeAttackBox.height))
			return true;
		if (!attackLeft && !Helper.CanMoveHere(meleeAttackBox.x, meleeAttackBox.y,
				meleeAttackBox.width + meleeAttackProgress, meleeAttackBox.height))
			return true;
		return false;
	}

	private boolean isMeleeAttackHit() {
		for (Entity entity : Main.gameLogic.getGameObjectContainer()) {
			if (!entity.isDestroyed() && entity instanceof Enemy) {
				Enemy enemy = (Enemy) entity;
				if (meleeAttackBox.intersects(enemy.getHitbox())) {
					enemy.receiveDamage(DAMAGE);
					return true;
				}
			}
		}
		return false;
	}

	private void meleeAttackingLoop() {
		boolean hit = false;
		while (meleeAttackProgress <= ATTACK_RANGE) {
			try {
				updateMeleeAttackProgress(ATTACK_SPEED);
			} catch (InterruptedException e) {
				break;
			}
			updateMeleeAttackBox();
			if (isMeleeAttackingWall())
				break;
			if (!hit && isMeleeAttackHit())
				hit = true;
		}
	}

	private void afterMeleeAttackLoop() {
		while (meleeAttackProgress > 0) {
			try {
				updateMeleeAttackProgress(-ATTACK_SPEED);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	private void initMeleeAttackingThread() {
		attacking = new Thread(() -> {
			meleeAttackingLoop();
			afterMeleeAttackLoop();
			meleeAttackProgress = 0;
			isAttacking = false;
		});
	}

	private void meleeAttack() {
		isAttacking = true;
		initMeleeAttackingThread();
		attacking.start();
	}

	private void initRangeAttackingThread() {
		attacking = new Thread(() -> {
			double bulletSpeed = BulletConstants.X_SPEED;
			double bulletX = hitbox.getMaxX();
			double bulletY = hitbox.y + (hitbox.height - BulletConstants.HEIGHT) / 2;
			if (attackLeft) {
				bulletSpeed = -bulletSpeed;
				bulletX = hitbox.x - BulletConstants.WIDTH;
			}
			new Bullet(bulletX, bulletY, bulletSpeed);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("range attacking thread interrupted");
			}
			isAttacking = false;
		});
	}

	private void shoot() {
		isAttacking = true;
		initRangeAttackingThread();
		attacking.start();
	}

	@Override
	public void update() {
		if (InputUtility.getKeyPressed(KeyCode.SPACE) && Helper.IsEntityOnFloor(hitbox)) {
			jump();
		}
		if (InputUtility.getKeyPressed(KeyCode.A)) {
			xspeed = -BASE_X_SPEED;
			attackLeft = true;
		} else if (InputUtility.getKeyPressed(KeyCode.D)) {
			xspeed = BASE_X_SPEED;
			attackLeft = false;
		} else {
			xspeed = 0;
		}

		if (InputUtility.isLeftDown() && Helper.IsEntityOnFloor(hitbox) && !isAttacking)
			meleeAttack();
		if (InputUtility.isRightDown() && !isAttacking)
			shoot();

		yspeed = Math.max(-MAX_Y_SPEED, Math.min(yspeed, MAX_Y_SPEED));

		move();

		// if the player is dead
		if (currentHealth == 0) {
			if (attacking != null)
				attacking.interrupt();
			Platform.exit();
		}
	}

	@Override
	public int getZ() {
		return 69;
	}
}
