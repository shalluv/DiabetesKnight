package entity;

import static utils.Constants.EnemyConstants.AFTER_ATTACK_DELAY;
import static utils.Constants.EnemyConstants.ATTACK_DELAY;
import static utils.Constants.EnemyConstants.ATTACK_RANGE;
import static utils.Constants.EnemyConstants.BASE_X_SPEED;
import static utils.Constants.EnemyConstants.DAMAGE;
import static utils.Constants.EnemyConstants.HEIGHT;
import static utils.Constants.EnemyConstants.INITIAL_X_SPEED;
import static utils.Constants.EnemyConstants.INITIAL_Y_SPEED;
import static utils.Constants.EnemyConstants.MAX_Y_SPEED;
import static utils.Constants.EnemyConstants.OFFSET_HITBOX_Y;
import static utils.Constants.EnemyConstants.SIGHT_SIZE;
import static utils.Constants.EnemyConstants.WEIGHT;
import static utils.Constants.EnemyConstants.WIDTH;

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
	private Image image;

	public Enemy(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		maxHealth = 100;
		currentHealth = 100;
		isAttacking = false;
		initHitbox(x, y + OFFSET_HITBOX_Y, width, height - OFFSET_HITBOX_Y);
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
		if (Helper.CanMoveHere(hitbox.x + xspeed, hitbox.y, hitbox.width, hitbox.height)) {
			hitbox.x += xspeed;
		} else {
			hitbox.x = Helper.GetEntityXPosNextToWall(hitbox, xspeed);
			jump();
		}
		// gravity
		if (Helper.CanMoveHere(hitbox.x, hitbox.y + yspeed, hitbox.width, hitbox.height)) {
			hitbox.y += yspeed;
			yspeed += WEIGHT;
		} else {
			hitbox.y = Helper.GetEntityYPosUnderRoofOrAboveFloor(hitbox, yspeed);
		}
	}

	private boolean canAttack(Player player) {
		Rectangle2D.Double attackBox = new Rectangle2D.Double(hitbox.x - ATTACK_RANGE, hitbox.y,
				WIDTH + 2 * ATTACK_RANGE, HEIGHT - OFFSET_HITBOX_Y);
		return attackBox.intersects(player.getHitbox()) && Helper.IsEntityOnFloor(hitbox);
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
		Rectangle2D.Double enemySight = new Rectangle2D.Double(hitbox.x - SIGHT_SIZE, hitbox.y - SIGHT_SIZE,
				width + 2 * SIGHT_SIZE, height - OFFSET_HITBOX_Y + 2 * SIGHT_SIZE);
		return enemySight.intersects(player.getHitbox());
	}

	private void updateXSpeed(Player player) {
		if (isInSight(player)) {
			if (player.getHitbox().x < hitbox.x
					&& Helper.CanMoveHere(hitbox.x - 1, hitbox.y, hitbox.width, hitbox.height)) {
				xspeed = -BASE_X_SPEED;
			} else if (player.getHitbox().x > hitbox.x
					&& Helper.CanMoveHere(hitbox.x + 1, hitbox.y, hitbox.width, hitbox.height)) {
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
		if (yspeed < -MAX_Y_SPEED) {
			yspeed = -MAX_Y_SPEED;
		} else if (yspeed > MAX_Y_SPEED) {
			yspeed = MAX_Y_SPEED;
		}
		move();
		if (canAttack(player)) {
			attack(player);
		}
		hitbox.x = (int) x;
		hitbox.y = (int) (y + OFFSET_HITBOX_Y);
	}

	@Override
	public int getZ() {
		return 1;
	}

}
