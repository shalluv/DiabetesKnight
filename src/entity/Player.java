package entity;

import static utils.Constants.PlayerConstants.*;

import java.awt.geom.Rectangle2D;

import entity.base.Entity;
import input.InputUtility;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;
import utils.Helper;

public class Player extends Entity {

	private int maxHealth;
	private int currentHealth;
	private double xspeed;
	private double yspeed;
	private boolean attackLeft;
	private boolean isAttacking;
	private int attackProgress;
	private Thread attacking;
	private Image image;

	public Player(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		initHitbox(x, y, WIDTH, HEIGHT);
		image = new Image("file:res/Owlet_Monster/Owlet_Monster.png");
		maxHealth = 100;
		isAttacking = false;
		attackProgress = 0;
		currentHealth = 100;
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		if (isAttacking) {
			if (attackLeft) {
				gc.fillRect(hitbox.x - attackProgress, hitbox.y + height / 2 - 5, attackProgress + 20, 10);
			} else {
				gc.fillRect(hitbox.getMaxX(), hitbox.y + height / 2 - 5, attackProgress, 10);
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

	private void updateAttackProgress(int value) throws InterruptedException {
		Thread.sleep(ATTACK_DELAY);
		attackProgress += value;
	}

	private boolean isAttackHit() {
		Rectangle2D.Double attackBox;
		if (attackLeft) {
			attackBox = new Rectangle2D.Double(hitbox.x - attackProgress, hitbox.y + height / 2 - 5,
					attackProgress + 20, 10);
		} else {
			attackBox = new Rectangle2D.Double(hitbox.getMaxX(), hitbox.y + height / 2 - 5, attackProgress, 10);
		}
		for (Renderable entity : RenderableHolder.getInstance().getEntities()) {
			if (!entity.isDestroyed() && entity instanceof Enemy) {
				Enemy enemy = (Enemy) entity;
				if (attackBox.intersects(enemy.getHitbox())) {
					enemy.receiveDamage(DAMAGE);
					return true;
				}
			}
		}
		return false;
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
			attack();

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
