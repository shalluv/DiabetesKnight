package entity;

import java.awt.Rectangle;

import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.*;

public class Enemy extends Entity {

	private int maxHealth;
	private int currentHealth;
	private int xspeed;
	private int yspeed;
	private boolean isAttacking;
	private Rectangle hitbox;
	private Image image;

	public Enemy(int x, int y) {
		super(x, y);
		xspeed = ORIGIN_X_SPEED;
		yspeed = ORIGIN_Y_SPEED;
		maxHealth = 100;
		currentHealth = 100;
		isAttacking = false;
		hitbox = new Rectangle(x, y + OFFSET_HITBOX_Y, WIDTH, HEIGHT - OFFSET_HITBOX_Y);
		image = new Image("file:res/Slime/stand_and_maybe_jump/slime2-1.png");
	}

	@Override
	public void draw(GraphicsContext gc) {
//		 Hitbox Rect
		if (isAttacking)
			gc.setFill(Color.RED);
		else
			gc.setFill(Color.GREEN);
		gc.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

		gc.drawImage(image, x, y, WIDTH, HEIGHT);
	}

	private void jump() {
		yspeed = -MAX_Y_SPEED;
	}

	private void move() {
		hitbox.x += xspeed;
		Block collidingBlock = findCollidingBlock();
		if (collidingBlock != null) {
			getRidOfCollisionX(collidingBlock);
			jump();
		}
		// gravity
		yspeed += WEIGHT;
		hitbox.y += yspeed;
		collidingBlock = findCollidingBlock();
		if (collidingBlock != null) {
			getRidOfCollisionY(collidingBlock);
		}
		setX(x + xspeed);
		setY(y + yspeed);
	}

	private boolean isOnPlatform() {
		int x = hitbox.x;
		int y = hitbox.y + hitbox.height;
		Rectangle checkerRect = new Rectangle(x, y, hitbox.width, 1);
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid()
					&& ((Block) block).getHitbox().intersects(checkerRect)) {
				return true;
			}
		}
		return false;
	}

	private boolean canMoveLeft() {
		int x = hitbox.x - BlockConstants.WIDTH;
		int y = hitbox.y + hitbox.height;
		Rectangle checkerRect = new Rectangle(x, y, BlockConstants.WIDTH, BlockConstants.HEIGHT * 10);
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid()
					&& ((Block) block).getHitbox().intersects(checkerRect)) {
				return true;
			}
		}
		return false;
	}

	private boolean canMoveRight() {
		int x = hitbox.x + hitbox.width;
		int y = hitbox.y + hitbox.height;
		Rectangle checkerRect = new Rectangle(x, y, BlockConstants.WIDTH, BlockConstants.HEIGHT * 10);
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid()
					&& ((Block) block).getHitbox().intersects(checkerRect)) {
				return true;
			}
		}
		return false;
	}

	private Block findCollidingBlock() {
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid() && ((Block) block).getHitbox().intersects(hitbox)) {
				return (Block) block;
			}
		}
		return null;
	}

	private void getRidOfCollisionX(Block collidingBlock) {
		hitbox.x -= xspeed;
		while (!collidingBlock.getHitbox().intersects(hitbox)) {
			hitbox.x += Math.signum(xspeed);
		}
		hitbox.x -= Math.signum(xspeed);
		xspeed = 0;
		setX(hitbox.x);
	}

	private void getRidOfCollisionY(Block collidingBlock) {
		hitbox.y -= yspeed;
		while (!collidingBlock.getHitbox().intersects(hitbox)) {
			hitbox.y += Math.signum(yspeed);
		}
		hitbox.y -= Math.signum(yspeed);
		yspeed = 0;
	}

	private boolean canAttack(Player player) {
		Rectangle attackBox = new Rectangle(hitbox.x - ATTACK_RANGE, hitbox.y, WIDTH + 2 * ATTACK_RANGE,
				HEIGHT - OFFSET_HITBOX_Y);
		if (attackBox.intersects(player.getHitbox()) && isOnPlatform())
			return true;
		return false;
	}

	private void attack(Player player) {
		isAttacking = true;
		Thread attacking = new Thread(() -> {
			try {
				Thread.sleep(ATTACK_DELAY);
				if (canAttack(player))
					player.receiveDamage(DAMAGE);
				Thread.sleep(AFTER_ATTACK_DELAY);
				isAttacking = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		attacking.start();
	}

	private boolean isInSight(Player player) {
		Rectangle enemySight = new Rectangle(hitbox.x - SIGHT_SIZE, hitbox.y - SIGHT_SIZE, WIDTH + 2 * SIGHT_SIZE,
				HEIGHT - OFFSET_HITBOX_Y + 2 * SIGHT_SIZE);
		if (enemySight.intersects(player.getHitbox()))
			return true;
		return false;
	}

	private void updateXSpeed(Player player) {
		if (isInSight(player)) {
			if (player.getHitbox().x < hitbox.x && canMoveLeft()) {
				xspeed = -BASE_X_SPEED;
			} else if (player.getHitbox().x > hitbox.x && canMoveRight()) {
				xspeed = BASE_X_SPEED;
			} else {
				xspeed = ORIGIN_X_SPEED;
			}
		} else {
			xspeed = ORIGIN_X_SPEED;
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
		if (yspeed < -MAX_Y_SPEED) {
			yspeed = -MAX_Y_SPEED;
		} else if (yspeed > MAX_Y_SPEED) {
			yspeed = MAX_Y_SPEED;
		}
		move();
		if (canAttack(player)) {
			attack(player);
		}
		hitbox.x = x;
		hitbox.y = y + OFFSET_HITBOX_Y;
	}

	@Override
	public int getZ() {
		return 1;
	}

}
