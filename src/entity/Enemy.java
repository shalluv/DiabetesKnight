package entity;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.Directions.*;
import static utils.Constants.AttackState.*;

import java.awt.geom.Rectangle2D;

import entity.base.Entity;
import interfaces.Damageable;
import item.Item;
import item.derived.Sugar;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import utils.Helper;

public class Enemy extends Entity implements Damageable {

	private int maxHealth;
	private int currentHealth;
	private double xspeed;
	private double yspeed;
	private int attackState;
	private int attackProgress;
	private int attackDirection;
	private Rectangle2D.Double attackBox;
	private Thread attacking;
	// private Image image;
	private Player player;
	private Item lootItem;

	public Enemy(int x, int y, Player player) {
		super(x, y, WIDTH, HEIGHT);
		this.player = player;
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		maxHealth = 100;
		currentHealth = 100;
		attackState = READY;
		attackProgress = 0;
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
				gc.fillRect(hitbox.x - attackProgress, hitbox.y + (hitbox.height - ATTACK_BOX_HEIGHT) / 2,
						attackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
				break;
			case RIGHT:
				gc.fillRect(hitbox.getMaxX() - hitbox.width / 2, hitbox.y + (hitbox.height - ATTACK_BOX_HEIGHT) / 2,
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

	private boolean canAttack(Player player) {
		Rectangle2D.Double canAttackBox = new Rectangle2D.Double(hitbox.x - MELEE_ATTACK_RANGE, hitbox.y,
				WIDTH + 2 * MELEE_ATTACK_RANGE, HEIGHT);
		return canAttackBox.intersects(player.getHitbox()) && Helper.IsEntityOnFloor(hitbox);
	}

	private void updateAttackBox() {
		switch (attackDirection) {
		case LEFT:
			attackBox = new Rectangle2D.Double(hitbox.x - attackProgress,
					hitbox.y + (hitbox.height - ATTACK_BOX_HEIGHT) / 2, attackProgress + hitbox.width / 2,
					ATTACK_BOX_HEIGHT);
			break;
		case RIGHT:
			attackBox = new Rectangle2D.Double(hitbox.getMaxX() - hitbox.width / 2,
					hitbox.y + (hitbox.height - ATTACK_BOX_HEIGHT) / 2, attackProgress + hitbox.width / 2,
					ATTACK_BOX_HEIGHT);
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

	private boolean isAttackHit() {
		if (attackBox.intersects(player.getHitbox()) && !Thread.interrupted()) {
			player.receiveDamage(MELEE_DAMAGE);
			return true;
		}
		return false;
	}

	private void updateAttackProgress(int value) throws InterruptedException {
		Thread.sleep(MELEE_ATTACK_DELAY);
		attackProgress += value;
	}

	private void attackingLoop() {
		boolean hit = false;
		while (attackProgress <= MELEE_ATTACK_RANGE) {
			try {
				updateAttackProgress(MELEE_ATTACK_SPEED);
			} catch (InterruptedException e) {
				break;
			}
			updateAttackBox();
			if (isAttackingWall())
				break;
			if (!hit && isAttackHit())
				hit = true;
		}
	}

	private void afterAttackLoop() {
		while (attackProgress > 0) {
			try {
				updateAttackProgress(-MELEE_ATTACK_SPEED);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	private void initAttackingThread() {
		attacking = new Thread(() -> {
			attackState = IN_PROGRESS;
			attackingLoop();
			attackState = ON_COOLDOWN;
			afterAttackLoop();
			attackProgress = 0;
			attackState = READY;
		});
	}

	private void attack() {
		initAttackingThread();
		attacking.start();
	}

	private boolean isInSight(Player player) {
		Rectangle2D.Double enemySight = new Rectangle2D.Double(hitbox.x - SIGHT_SIZE, hitbox.y - SIGHT_SIZE,
				hitbox.width + 2 * SIGHT_SIZE, hitbox.height + 2 * SIGHT_SIZE);
		return enemySight.intersects(player.getHitbox());
	}

	private void updateXSpeed() {
		if (isInSight(player)) {
			if (player.getHitbox().getMaxX() + MELEE_ATTACK_RANGE / 2 < hitbox.x && Helper
					.IsEntityOnFloor(new Rectangle2D.Double(hitbox.x - WIDTH, hitbox.y + 3 * HEIGHT, WIDTH, HEIGHT))) {
				xspeed = -BASE_X_SPEED;
				if (attackState == READY)
					attackDirection = LEFT;
			} else if (player.getHitbox().x > hitbox.getMaxX() + MELEE_ATTACK_RANGE / 2 && Helper
					.IsEntityOnFloor(new Rectangle2D.Double(hitbox.getMaxX(), hitbox.y + 3 * HEIGHT, WIDTH, HEIGHT))) {
				xspeed = BASE_X_SPEED;
				if (attackState == READY)
					attackDirection = RIGHT;
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

	@Override
	public void receiveDamage(int damage) {
		setCurrentHealth(currentHealth - damage);
		System.out.println("Enemy is now " + currentHealth + " hp");
	}

	@Override
	public void update() {
		updateXSpeed();

		yspeed = Math.max(-MAX_Y_SPEED, Math.min(yspeed, MAX_Y_SPEED));

		move();
		if (canAttack(player) && attackState == READY)
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

	public Item getLootItem() {
		return lootItem;
	}

}
