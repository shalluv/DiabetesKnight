package entity;

import static utils.Constants.PlayerConstants.ATTACK_BOX_HEIGHT;
import static utils.Constants.PlayerConstants.BASE_X_SPEED;
import static utils.Constants.PlayerConstants.HEIGHT;
import static utils.Constants.PlayerConstants.INITIAL_MAX_HEALTH;
import static utils.Constants.PlayerConstants.INITIAL_MAX_POWER;
import static utils.Constants.PlayerConstants.INITIAL_X_SPEED;
import static utils.Constants.PlayerConstants.INITIAL_Y_SPEED;
import static utils.Constants.PlayerConstants.INVENTORY_SIZE;
import static utils.Constants.PlayerConstants.MAX_Y_SPEED;
import static utils.Constants.PlayerConstants.MELEE_ATTACK_DELAY;
import static utils.Constants.PlayerConstants.MELEE_ATTACK_RANGE;
import static utils.Constants.PlayerConstants.MELEE_ATTACK_SPEED;
import static utils.Constants.PlayerConstants.MELEE_DAMAGE;
import static utils.Constants.PlayerConstants.RANGE_ATTACK_DELAY;
import static utils.Constants.PlayerConstants.WEIGHT;
import static utils.Constants.PlayerConstants.WIDTH;
import static utils.Constants.PlayerConstants.Animations.IDLE;
import static utils.Constants.PlayerConstants.Animations.RUNNING;
import static utils.Constants.PlayerConstants.Animations.JUMPING;
import static utils.Constants.PlayerConstants.Animations.IDLE_FRAMES_COUNT;
import static utils.Constants.PlayerConstants.Animations.RUNNING_FRAMES_COUNT;
import static utils.Constants.PlayerConstants.Animations.ANIMATION_SPEED;

import java.awt.geom.Rectangle2D;

import application.Main;
import entity.base.Entity;
import input.InputUtility;
import interfaces.Consumable;
import interfaces.Damageable;
import item.Item;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import utils.Constants.BulletConstants;
import utils.Constants.Resolution;
import utils.Helper;
import utils.Loader;

public class Player extends Entity implements Damageable {

	private int maxHealth;
	private int currentHealth;
	private int maxPower;
	private int currentPower;
	private double xspeed;
	private double yspeed;
	private boolean attackLeft;
	private boolean isAttacking;
	private int meleeAttackProgress;
	private Thread attacking;
	private Rectangle2D.Double meleeAttackBox;
	private Item[] inventory;
	private int currentInventoryFocus;
	private int animationFrame;
	private int animationState;
	private int frameCount;
	private Image[] animation;
	private Image dustAnimation;

	public Player(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		initHitbox(x, y, width, height);
		loadResources();
		maxHealth = INITIAL_MAX_HEALTH;
		currentHealth = INITIAL_MAX_HEALTH;

		maxPower = INITIAL_MAX_POWER;
		currentPower = 0;

		isAttacking = false;
		meleeAttackProgress = 0;

		inventory = new Item[INVENTORY_SIZE];
		currentInventoryFocus = 0;
	}

	private void loadResources() {
		animation = new Image[3];
		animationFrame = 0;
		frameCount = 0;
		animationState = IDLE;
		animation[0] = Loader.GetSpriteAtlas(Loader.PLAYER_IDLE_ATLAS);
		animation[1] = Loader.GetSpriteAtlas(Loader.PLAYER_RUN_ATLAS);
		animation[2] = Loader.GetSpriteAtlas(Loader.PLAYER_JUMP_ATLAS);
		dustAnimation = Loader.GetSpriteAtlas(Loader.DUST_ATLAS);
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		if (isAttacking) {
			if (attackLeft) {
				gc.fillRect(hitbox.x - meleeAttackProgress, hitbox.y + (hitbox.height - ATTACK_BOX_HEIGHT) / 2,
						meleeAttackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
			} else {
				gc.fillRect(hitbox.getMaxX(), hitbox.y + (hitbox.height - ATTACK_BOX_HEIGHT) / 2, meleeAttackProgress,
						ATTACK_BOX_HEIGHT);
			}
		}
//		gc.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

		frameCount++;
		if (!Helper.IsEntityOnFloor(hitbox)) {
			if (animationState != JUMPING)
				animationFrame = 0;
			animationState = JUMPING;
		} else if (Math.abs(xspeed) > 0) {
			if (animationState != RUNNING)
				animationFrame = 0;
			animationState = RUNNING;
		} else {
			if (animationState != IDLE)
				animationFrame = 0;
			animationState = IDLE;
		}
		if (frameCount > ANIMATION_SPEED) {
			frameCount -= ANIMATION_SPEED;
			animationFrame++;
			switch (animationState) {
			case IDLE:
				animationFrame %= IDLE_FRAMES_COUNT;
				break;
			case RUNNING:
				animationFrame %= RUNNING_FRAMES_COUNT;
				break;
			case JUMPING:
				if (yspeed > 0) {
					animationFrame = Math.min(6, animationFrame);
				} else {
					animationFrame = Math.min(3, animationFrame);
				}
			default:
				break;
			}
		}
		gc.drawImage(animation[animationState], animationFrame * 32, 0, 32, 32, hitbox.x + 2, hitbox.y, width, height);
		if (animationState == 1) {
			gc.drawImage(dustAnimation, animationFrame * 32, 0, 32, 32, hitbox.x - 4, hitbox.y, width, height);
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
		if (attackLeft) {
			meleeAttackBox = new Rectangle2D.Double(hitbox.x - meleeAttackProgress,
					hitbox.y + (hitbox.height - ATTACK_BOX_HEIGHT) / 2, meleeAttackProgress + hitbox.width / 2,
					ATTACK_BOX_HEIGHT);
		} else {
			meleeAttackBox = new Rectangle2D.Double(hitbox.getMaxX(),
					hitbox.y + (hitbox.height - ATTACK_BOX_HEIGHT) / 2, meleeAttackProgress, ATTACK_BOX_HEIGHT);
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
				Thread.sleep(RANGE_ATTACK_DELAY);
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

		updateCurrentInventoryFocus();

		if (InputUtility.isLeftDown() && Helper.IsEntityOnFloor(hitbox) && !isAttacking)
			meleeAttack();
		if (InputUtility.isRightDown() && !isAttacking)
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
		if (currentHealth <= 0) {
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
