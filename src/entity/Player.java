package entity;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.AttackState.ON_RELOAD;
import static utils.Constants.AttackState.READY;
import static utils.Constants.PlayerConstants.BASE_X_SPEED;
import static utils.Constants.PlayerConstants.HEIGHT;
import static utils.Constants.PlayerConstants.INITIAL_MAX_HEALTH;
import static utils.Constants.PlayerConstants.INITIAL_MAX_POWER;
import static utils.Constants.PlayerConstants.INITIAL_X_SPEED;
import static utils.Constants.PlayerConstants.INITIAL_Y_SPEED;
import static utils.Constants.PlayerConstants.INVENTORY_SIZE;
import static utils.Constants.PlayerConstants.MAX_Y_SPEED;
import static utils.Constants.PlayerConstants.WEIGHT;
import static utils.Constants.PlayerConstants.WIDTH;
import static utils.Constants.PlayerConstants.Animations.ANIMATION_SPEED;
import static utils.Constants.PlayerConstants.Animations.ANIMATION_STATE_COUNT;
import static utils.Constants.PlayerConstants.Animations.IDLE;
import static utils.Constants.PlayerConstants.Animations.IDLE_FRAMES_COUNT;
import static utils.Constants.PlayerConstants.Animations.JUMPING;
import static utils.Constants.PlayerConstants.Animations.RUNNING;
import static utils.Constants.PlayerConstants.Animations.RUNNING_FRAMES_COUNT;
import static utils.Constants.PlayerConstants.Animations.SPRITE_SIZE;

import java.awt.geom.Rectangle2D;

import application.Main;
import entity.base.Entity;
import input.InputUtility;
import interfaces.Consumable;
import interfaces.Damageable;
import interfaces.Reloadable;
import item.Item;
import item.Weapon;
import item.derived.Sword;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import logic.GameLogic;
import utils.Helper;
import utils.Loader;

public class Player extends Entity implements Damageable {

	private int maxHealth;
	private int currentHealth;
	private int maxPower;
	private int currentPower;
	private double xspeed;
	private double yspeed;
	private int attackState;
	private Item[] inventory;
	private Item currentItem;
	private Weapon currentWeapon;
	private int currentInventoryFocus;
	private int animationFrame;
	private int animationState;
	private int frameCount;
	private Image[] animation;
	private Image dustAnimation;
	private boolean isFacingLeft;

	public Player(double x, double y) {
		super(x, y, WIDTH, HEIGHT);
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		initHitbox(x, y, width, height);
		loadResources();
		maxHealth = INITIAL_MAX_HEALTH;
		currentHealth = INITIAL_MAX_HEALTH;

		maxPower = INITIAL_MAX_POWER;
		currentPower = 0;
		attackState = READY;

		inventory = new Item[INVENTORY_SIZE];
		addItem(new Sword());
		currentInventoryFocus = 0;

		isFacingLeft = false;
	}

	private void loadResources() {
		animation = new Image[ANIMATION_STATE_COUNT + 1];
		animationFrame = 0;
		frameCount = 0;
		animationState = IDLE;
		animation[0] = Loader.GetSpriteAtlas(Loader.PLAYER_IDLE_ATLAS);
		animation[1] = Loader.GetSpriteAtlas(Loader.PLAYER_RUN_ATLAS);
		animation[2] = Loader.GetSpriteAtlas(Loader.PLAYER_JUMP_ATLAS);
		dustAnimation = Loader.GetSpriteAtlas(Loader.DUST_ATLAS);
	}

	@Override
	public void draw(GraphicsContext gc, Rectangle2D.Double screen) {
		if (!hitbox.intersects(screen))
			return;

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
			case JUMPING:
				animationFrame %= RUNNING_FRAMES_COUNT;
				break;
//				if (yspeed > 0) {
//					animationFrame = Math.min(6, animationFrame);
//				} else {
//					animationFrame = Math.min(3, animationFrame);
//				}
			default:
				break;
			}
		}

		double drawX = hitbox.x + (isFacingLeft ? width : 0);
		double drawY = hitbox.y;
		double drawW = width * (isFacingLeft ? -1 : 1);
		double drawH = height;
		double offsetDust = 4 * (isFacingLeft ? 1 : -1);

		gc.drawImage(animation[animationState], animationFrame * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, drawX, drawY,
				drawW, drawH);
		gc.setFill(Color.BLACK);
		if (currentWeapon != null) {
			currentWeapon.draw(gc, hitbox.x, hitbox.y, 32, 32, isFacingLeft);
		}
		if (animationState == RUNNING) {
			gc.drawImage(dustAnimation, animationFrame * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, drawX + offsetDust,
					drawY, drawW, drawH);
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

	public void heal(int value) {
		if (value < 0)
			value = 0;
		setCurrentHealth(value + currentHealth);
	}

	@Override
	public void receiveDamage(int damage) {
		if (damage < 0)
			damage = 0;
		setCurrentHealth(currentHealth - damage);
		// System.out.println("player is now " + currentHealth + " hp");
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
		for (Entity entity : GameLogic.getGameObjectContainer()) {
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
		if (currentItem == null)
			return;

		if (currentItem instanceof Consumable) {
			inventory[currentInventoryFocus] = null;

			((Consumable) currentItem).consume();
		}
	}

	private void attack() {
		if (currentItem == null)
			return;

		if (currentItem instanceof Weapon) {
			currentWeapon = ((Weapon) currentItem);
			if (attackState == ON_RELOAD && currentItem instanceof Reloadable
					&& ((Reloadable) currentItem).getAmmo() > 0) {
				((Reloadable) currentWeapon).cancelReload();
				attackState = currentWeapon.attack(InputUtility.getMouseX(), InputUtility.getMouseY(), this);
			} else if (attackState == READY)
				attackState = currentWeapon.attack(InputUtility.getMouseX(), InputUtility.getMouseY(), this);
		}
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
		currentItem = inventory[currentInventoryFocus];
		if (attackState != READY && currentWeapon != null) {
			if (attackState == ON_RELOAD && !(currentItem instanceof Reloadable)
					&& currentWeapon instanceof Reloadable) {
				attackState = READY;
				((Reloadable) currentWeapon).cancelReload();
			} else
				attackState = currentWeapon.updateAttack(this);
		}
		if (attackState == READY && currentItem == null)
			currentWeapon = null;
		if (xspeed > 0) {
			isFacingLeft = false;
		} else if (xspeed < 0) {
			isFacingLeft = true;
		}
		if (InputUtility.isLeftDown()) {
			if (attackState == READY || attackState == ON_RELOAD) {
				attack();
			}
			if (InputUtility.getMouseX() > hitbox.x + width) {
				isFacingLeft = false;
			} else if (InputUtility.getMouseX() < hitbox.x) {
				isFacingLeft = true;
			}
		}
		if (InputUtility.getKeyPressed(KeyCode.E)) {
			if (!isPlayerOnDoor())
				useItem();
		}
		if (InputUtility.getKeyPressed(KeyCode.R) && attackState == READY && currentWeapon instanceof Reloadable) {
			attackState = ON_RELOAD;
			((Reloadable) currentWeapon).reload();
		}
		pickUpItems();

		yspeed = Math.max(-MAX_Y_SPEED, Math.min(yspeed, MAX_Y_SPEED));
		if (currentItem instanceof Weapon)
			xspeed *= ((Weapon) currentItem).getSpeedMultiplier();

		move();

		if (hitbox.y + hitbox.height + 1 >= Main.mapManager.getMapHeight()) {
			currentHealth = 0;
		}

		// if the player is dead
		if (currentHealth <= 0) {
			if (attackState != READY && currentWeapon != null) {
				((Weapon) currentWeapon).cancelAttack();
			}
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
		if (attackState == IN_PROGRESS)
			return;
		if (InputUtility.getScrollDeltaY() != 0) {
			if (InputUtility.getScrollDeltaY() > 0)
				currentInventoryFocus = (currentInventoryFocus + 9) % 10;
			else if (InputUtility.getScrollDeltaY() < 0)
				currentInventoryFocus = (currentInventoryFocus + 11) % 10;
			InputUtility.setScrollDeltaY(0);
		}
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

		if (inventory[currentInventoryFocus] instanceof Weapon) {
			currentWeapon = (Weapon) inventory[currentInventoryFocus];
		}
	}

	private boolean isPlayerOnDoor() {
		for (Entity entity : GameLogic.getGameObjectContainer()) {
			if (entity instanceof Door && entity.getHitbox().intersects(hitbox)) {
				((Door) entity).interact();
				return true;
			}
		}
		return false;
	}

	@Override
	public int getHealth() {
		return currentHealth;
	}
}
