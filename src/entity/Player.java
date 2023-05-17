package entity;

import static utils.Constants.PlayerConstants.*;
import static utils.Constants.Directions.*;
import static utils.Constants.AttackState.*;

import java.awt.MouseInfo;
import java.awt.geom.Rectangle2D;

import application.Main;
import entity.base.Entity;
import input.InputUtility;
import interfaces.Consumable;
import interfaces.Damageable;
import item.Item;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import utils.Constants.BulletConstants;
import utils.Constants.Resolution;
import utils.Helper;

public class Player extends Entity implements Damageable {

	private int maxHealth;
	private int currentHealth;
	private int maxPower;
	private int currentPower;
	private double xspeed;
	private double yspeed;
	private int attackDirection;
	private int attackState;
	private int meleeAttackProgress;
	private Thread attacking;
	// private Image image;
	private Rectangle2D.Double meleeAttackBox;
	private Item[] inventory;
	private int currentInventoryFocus;

	public Player(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		initHitbox(x, y, width, height);
		// image = new Image("file:res/Owlet_Monster/Owlet_Monster.png");
		maxHealth = 100;
		currentHealth = 100;
		maxPower = 100;
		currentPower = 0;
		inventory = new Item[INVENTORY_SIZE];
		currentInventoryFocus = 0;
		attackState = READY;
		meleeAttackProgress = 0;
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		if (attackState != READY && meleeAttackProgress != 0) {
			switch (attackDirection) {
			case LEFT:
				gc.fillRect(hitbox.x - meleeAttackProgress, hitbox.y + (hitbox.height - ATTACK_BOX_HEIGHT) / 2,
						meleeAttackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
				break;
			case RIGHT:
				gc.fillRect(hitbox.getMaxX() - hitbox.width / 2, hitbox.y + (hitbox.height - ATTACK_BOX_HEIGHT) / 2,
						meleeAttackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
				break;
			default:
				break;
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
		Thread.sleep(MELEE_ATTACK_DELAY);
		meleeAttackProgress += value;
	}

	private void updateMeleeAttackBox() {
		switch (attackDirection) {
		case LEFT:
			meleeAttackBox = new Rectangle2D.Double(hitbox.x - meleeAttackProgress,
					hitbox.y + (hitbox.height - ATTACK_BOX_HEIGHT) / 2, meleeAttackProgress + hitbox.width / 2,
					ATTACK_BOX_HEIGHT);
			break;
		case RIGHT:
			meleeAttackBox = new Rectangle2D.Double(hitbox.getMaxX() - hitbox.width / 2,
					hitbox.y + (hitbox.height - ATTACK_BOX_HEIGHT) / 2, meleeAttackProgress + hitbox.width / 2,
					ATTACK_BOX_HEIGHT);
			break;
		default:
			break;
		}
	}

	private boolean isMeleeAttackingWall() {
		if (attackDirection == LEFT && !Helper.CanMoveHere(meleeAttackBox.x - meleeAttackProgress, meleeAttackBox.y,
				meleeAttackBox.width, meleeAttackBox.height))
			return true;
		if (attackDirection == RIGHT && !Helper.CanMoveHere(meleeAttackBox.x + hitbox.width / 2, meleeAttackBox.y,
				meleeAttackBox.width + meleeAttackProgress - hitbox.width / 2, meleeAttackBox.height))
			return true;
		return false;
	}

	private boolean isMeleeAttackHit() {
		for (Entity entity : Main.gameLogic.getGameObjectContainer()) {
			if (!entity.isDestroyed() && entity instanceof Enemy) {
				Enemy enemy = (Enemy) entity;
				if (meleeAttackBox.intersects(enemy.getHitbox()) && !Thread.interrupted()) {
					enemy.receiveDamage(MELEE_DAMAGE);
					return true;
				}
			}
		}
		return false;
	}

	private void meleeAttackingLoop() {
		boolean hit = false;
		while (meleeAttackProgress <= MELEE_ATTACK_RANGE) {
			try {
				updateMeleeAttackProgress(MELEE_ATTACK_SPEED);
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
				updateMeleeAttackProgress(-MELEE_ATTACK_SPEED);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	private void initMeleeAttackingThread() {
		attacking = new Thread(() -> {
			attackState = IN_PROGRESS;
			meleeAttackingLoop();
			attackState = ON_COOLDOWN;
			afterMeleeAttackLoop();
			meleeAttackProgress = 0;
			attackState = READY;
		});
	}

	private void meleeAttack() {
		initMeleeAttackingThread();
		attacking.start();
	}

	private void initRangeAttackingThread() {
		attacking = new Thread(() -> {
			attackState = IN_PROGRESS;
			double bulletSpeed = BulletConstants.X_SPEED;
			double bulletX = hitbox.getMaxX();
			double bulletY = hitbox.y + (hitbox.height - BulletConstants.HEIGHT) / 2;
			if (attackDirection == LEFT) {
				bulletSpeed = -bulletSpeed;
				bulletX = hitbox.x - BulletConstants.WIDTH;
			}
			new Bullet(bulletX, bulletY, bulletSpeed);
			attackState = ON_COOLDOWN;
			try {
				Thread.sleep(RANGE_ATTACK_DELAY);
			} catch (InterruptedException e) {
				System.out.println("range attacking thread interrupted");
			}
			attackState = READY;
		});
	}

	private void shoot() {
		initRangeAttackingThread();
		attacking.start();
	}

	private boolean addItem(Item item) {
		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			if (inventory[i] == null) {
				inventory[i] = item;
				return true;
			}
		}
		return false;
	}

	private void pickUpItems() {
		for (Entity entity : Main.gameLogic.getGameObjectContainer()) {
			if (!entity.isDestroyed() && entity instanceof DroppedItem) {
				DroppedItem item = (DroppedItem) entity;
				if (hitbox.intersects(item.getHitbox())) {
					if (addItem(item.getItem())) {
						item.setDestroy(true);
					}
				}
			}
		}
	}

	private void useItem() {
		Item currentItem = inventory[currentInventoryFocus];
		if (currentItem == null)
			return;

		if (currentItem instanceof Consumable) {
			inventory[currentInventoryFocus] = null;

			((Consumable) currentItem).consume();
		}
	}

	private void updateAttackDirection() {
		if (InputUtility.getMouseX() >= hitbox.x + hitbox.width / 2)
			attackDirection = RIGHT;
		else
			attackDirection = LEFT;
	}

	@Override
	public void update() {
		if (InputUtility.getKeyPressed(KeyCode.SPACE) && Helper.IsEntityOnFloor(hitbox)) {
			jump();
		}
		if (InputUtility.getKeyPressed(KeyCode.A)) {
			xspeed = -BASE_X_SPEED;
		} else if (InputUtility.getKeyPressed(KeyCode.D)) {
			xspeed = BASE_X_SPEED;
		} else {
			xspeed = 0;
		}

		updateCurrentInventoryFocus();

		if (InputUtility.isLeftDown() && Helper.IsEntityOnFloor(hitbox) && attackState == READY) {
			updateAttackDirection();
			meleeAttack();
		}
		if (InputUtility.isRightDown() && attackState == READY)
			shoot();
		if (InputUtility.getKeyPressed(KeyCode.E)) {
			useItem();
		}
		pickUpItems();

		yspeed = Math.max(-MAX_Y_SPEED, Math.min(yspeed, MAX_Y_SPEED));

		move();

		if (hitbox.y + hitbox.height >= Resolution.HEIGHT) {
			currentHealth = 0;
		}

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

	public int getCurrentHealth() {
		return currentHealth;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getMaxPower() {
		return maxPower;
	}

	public int getCurrentPower() {
		return currentPower;
	}

	public void setCurrentPower(int power) {
		if (power > maxPower) {
			currentPower = maxPower;
		} else if (power < 0) {
			currentPower = 0;
		} else {
			currentPower = power;
		}
	}

	public Item[] getInventory() {
		return inventory;
	}

	public int getCurrentInventoryFocus() {
		return currentInventoryFocus;
	}

	public void updateCurrentInventoryFocus() {
		if (InputUtility.getKeyPressed(KeyCode.DIGIT0)) {
			currentInventoryFocus = 9;
		} else if (InputUtility.getKeyPressed(KeyCode.DIGIT1)) {
			currentInventoryFocus = 0;
		} else if (InputUtility.getKeyPressed(KeyCode.DIGIT2)) {
			currentInventoryFocus = 1;
		} else if (InputUtility.getKeyPressed(KeyCode.DIGIT3)) {
			currentInventoryFocus = 2;
		} else if (InputUtility.getKeyPressed(KeyCode.DIGIT4)) {
			currentInventoryFocus = 3;
		} else if (InputUtility.getKeyPressed(KeyCode.DIGIT5)) {
			currentInventoryFocus = 4;
		} else if (InputUtility.getKeyPressed(KeyCode.DIGIT6)) {
			currentInventoryFocus = 5;
		} else if (InputUtility.getKeyPressed(KeyCode.DIGIT7)) {
			currentInventoryFocus = 6;
		} else if (InputUtility.getKeyPressed(KeyCode.DIGIT8)) {
			currentInventoryFocus = 7;
		} else if (InputUtility.getKeyPressed(KeyCode.DIGIT9)) {
			currentInventoryFocus = 8;
		}
	}
}
