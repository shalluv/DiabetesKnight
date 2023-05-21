package entity;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.AttackState.ON_RELOAD;
import static utils.Constants.AttackState.READY;
import static utils.Constants.PlayerConstants.BASE_X_SPEED;
import static utils.Constants.PlayerConstants.HEIGHT;
import static utils.Constants.PlayerConstants.HIT_SUGAR_DECREASED_AMOUNT;
import static utils.Constants.PlayerConstants.HYPERGLYCEMIA_DAMAGE;
import static utils.Constants.PlayerConstants.HYPERGLYCEMIA_DELAY;
import static utils.Constants.PlayerConstants.HYPERGLYCEMIA_SUGAR_LEVEL;
import static utils.Constants.PlayerConstants.HYPOGLYCEMIA_SUGAR_LEVEL;
import static utils.Constants.PlayerConstants.HYPOGLYCEMIA_X_SPEED_MULTIPLIER;
import static utils.Constants.PlayerConstants.HYPOGLYCEMIA_Y_SPEED_MULTIPLIER;
import static utils.Constants.PlayerConstants.INITIAL_MAX_HEALTH;
import static utils.Constants.PlayerConstants.INITIAL_POWER;
import static utils.Constants.PlayerConstants.INITIAL_SUGAR_LEVEL;
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
import static utils.Constants.PlayerConstants.HealthState.HEALTHY;
import static utils.Constants.PlayerConstants.HealthState.HYPERGLYCEMIA;
import static utils.Constants.PlayerConstants.HealthState.HYPOGLYCEMIA;

import java.awt.geom.Rectangle2D;

import application.Main;
import entity.base.Entity;
import input.InputUtility;
import interfaces.Consumable;
import interfaces.Damageable;
import interfaces.Reloadable;
import item.Item;
import item.Weapon;
import item.derived.Gun;
import item.derived.Spear;
import item.derived.Sword;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import logic.GameLogic;
import utils.Helper;
import utils.Loader;

/**
 * Player class
 * Represents the player
 * 
 * @see Entity
 * @see Damageable
 */
public class Player extends Entity implements Damageable {

	/**
	 * The maximum health of the player
	 */
	private int maxHealth;
	/**
	 * The current health of the player
	 */
	private int currentHealth;
	/**
	 * The current power of the player
	 */
	private int currentPower;
	/**
	 * The current sugar level of the player
	 */
	private int sugarLevel;
	/**
	 * The x-axis speed of the player
	 */
	private double xspeed;
	/**
	 * The y-axis speed of the player
	 */
	private double yspeed;
	/**
	 * The current attack state of the player
	 */
	private int attackState;
	/**
	 * The inventory of the player
	 * @see item.Item
	 */
	private Item[] inventory;
	/**
	 * The current item hold by the player
	 * @see item.Item
	 */
	private Item currentItem;
	/**
	 * The current weapon hold by the player
	 * @see item.Weapon
	 */
	private Weapon currentWeapon;
	/**
	 * The current inventory focus of the player
	 */
	private int currentInventoryFocus;
	/**
	 * The frame of the animation of the player
	 */
	private int animationFrame;
	/**
	 * The state of the animation of the player
	 */
	private int animationState;
	/**
	 * The frame counter for the animation of the player
	 */
	private int frameCount;
	/**
	 * The animation of the player
	 * @see javafx.scene.image.Image
	 */
	private Image[] animation;
	/**
	 * The dust animation of the player when running
	 * @see javafx.scene.image.Image
	 */
	private Image dustAnimation;
	/**
	 * Whether the player is facing left
	 */
	private boolean isFacingLeft;
	/**
	 * The health state of the player
	 */
	private int healthState;
	/**
	 * The thread for the hyperglycemia effect
	 * @see java.lang.Thread
	 */
	private Thread onHyperglycemia;

	/**
	 * Constructor
	 * @param x x-coordinate of the player
	 * @param y y-coordinate of the player
	 */
	public Player(double x, double y) {
		super(x, y, WIDTH, HEIGHT);
		initHitbox(x, y, width, height);

		loadResources();

		// Initialize player stats
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		maxHealth = INITIAL_MAX_HEALTH;
		currentHealth = INITIAL_MAX_HEALTH;
		currentPower = INITIAL_POWER;
		sugarLevel = INITIAL_SUGAR_LEVEL;
		attackState = READY;
		healthState = HEALTHY;
		isFacingLeft = false;

		initOnHyperglycemia();
		
		// Initialize inventory
		inventory = new Item[INVENTORY_SIZE];
		addItem(new Sword());
		addItem(new Spear());
		addItem(new Gun());
		currentInventoryFocus = 0;
	}

	/**
	 * Load sprites of the player
	 */
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

	/**
	 * Draw the player
	 * @param gc GraphicsContext
	 * @param screen Rectangle2D.Double
	 * @see Entity#draw(GraphicsContext, Rectangle2D.Double)
	 */
	@Override
	public void draw(GraphicsContext gc, Rectangle2D.Double screen) {
		// Draw only if the player is on screen
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
			default:
				break;
			}
		}

		// Get the position to draw
		double drawX = hitbox.x + (isFacingLeft ? width : 0);
		double drawY = hitbox.y;
		double drawW = width * (isFacingLeft ? -1 : 1);
		double drawH = height;
		double offsetDust = 4 * (isFacingLeft ? 1 : -1);

		gc.drawImage(animation[animationState], animationFrame * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, drawX, drawY,
				drawW, drawH);

		// Draw the weapon
		if (currentWeapon != null) {
			currentWeapon.draw(gc, hitbox.x, hitbox.y, 32, 32, isFacingLeft);
		}

		// Draw dust when running
		if (animationState == RUNNING) {
			gc.drawImage(dustAnimation, animationFrame * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, drawX + offsetDust,
					drawY, drawW, drawH);
		}

	}

	/**
	 * Set the player's current health
	 * @param value the value to set, must be between 0 and maxHealth
	 */
	private void setCurrentHealth(int value) {
		if (value < 0) {
			currentHealth = 0;
		} else if (value > maxHealth) {
			currentHealth = maxHealth;
		} else {
			currentHealth = value;
		}
	}

	/**
	 * Heal the player
	 * @param value the value to heal, must be positive
	 */
	public void heal(int value) {
		if (value < 0)
			value = 0;
		setCurrentHealth(value + currentHealth);
	}

	/**
	 * Player receives damage
	 * @param damage the amount of damage to receive, must be positive
	 */
	@Override
	public void receiveDamage(int damage) {
		if (damage < 0)
			damage = 0;
		setCurrentHealth(currentHealth - damage);
		setSugarLevel(sugarLevel - HIT_SUGAR_DECREASED_AMOUNT);
	}

	/**
	 * Jump
	 */
	private void jump() {
		yspeed = -MAX_Y_SPEED;
	}

	/**
	 * Move the player
	 * @see utils.Helper#CanMoveHere(double, double, double, double)
	 * @see utils.Helper#GetEntityXPosNextToWall(Rectangle2D.Double, double)
	 * @see utils.Helper#GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Double, double)
	 */
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

	/**
	 * Add an item to the inventory
	 * @param item the item to add
	 * @return true if the item was added, false otherwise
	 * @see item.Item
	 */
	private boolean addItem(Item item) {
		for (int i = 0; i < INVENTORY_SIZE; ++i) {
			if (inventory[i] == null) {
				inventory[i] = item;
				return true;
			}
		}
		return false;
	}

	/**
	 * Pick up items on the ground
	 * @see entity.DroppedItem
	 * @see item.Item
	 */
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

	/**
	 * Use the current item
	 * @see interfaces.Consumable
	 */
	private void useItem() {
		if (currentItem == null)
			return;

		if (currentItem instanceof Consumable) {
			inventory[currentInventoryFocus] = null;

			((Consumable) currentItem).consume();
		}
	}

	/**
	 * Attack with the current weapon
	 * @see item.Weapon
	 * @see interfaces.Reloadable
	 */
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

	/**
	 * Update the player
	 * @see input.InputUtility
	 * @see #jump()
	 * @see #updateCurrentInventoryFocus()
	 * @see #attack()
	 * @see #useItem()
	 * @see #move()
	 * @see #pickUpItems()
	 * 
	 */
	@Override
	public void update() {
		// if player presses space and is on floor, jump
		if (InputUtility.getKeyPressed(KeyCode.SPACE) && Helper.IsEntityOnFloor(hitbox)) {
			jump();
		}

		// set xspeed according to key pressed
		if (InputUtility.getKeyPressed(KeyCode.A)) {
			xspeed = -BASE_X_SPEED;
		} else if (InputUtility.getKeyPressed(KeyCode.D)) {
			xspeed = BASE_X_SPEED;
		} else {
			xspeed = 0;
		}

		updateCurrentInventoryFocus();

		// update current item
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
		
		// update facing direction according to xspeed
		if (xspeed > 0) {
			isFacingLeft = false;
		} else if (xspeed < 0) {
			isFacingLeft = true;
		}
		
		if (InputUtility.isLeftDown()) {
			if (attackState == READY || attackState == ON_RELOAD) {
				attack();
			}

			// update facing direction according to mouse position when attacking
			if (InputUtility.getMouseX() > hitbox.x + width) {
				isFacingLeft = false;
			} else if (InputUtility.getMouseX() < hitbox.x) {
				isFacingLeft = true;
			}
		}

		// use item
		if (InputUtility.getKeyPressed(KeyCode.E)) {
			if (!isPlayerOnDoor())
				useItem();
		}

		// reload
		if (InputUtility.getKeyPressed(KeyCode.R) && attackState == READY && currentWeapon instanceof Reloadable) {
			attackState = ON_RELOAD;
			((Reloadable) currentWeapon).reload();
		}

		// use ultimate
		if (InputUtility.getKeyPressed(KeyCode.F) && attackState == READY && currentWeapon instanceof Weapon) {
			((Weapon) currentWeapon).useUlitmate();
		}

		pickUpItems();

		// update yspeed
		yspeed = Math.max(-MAX_Y_SPEED, Math.min(yspeed, MAX_Y_SPEED));
		if (currentItem instanceof Weapon) {
			xspeed *= ((Weapon) currentItem).getXSpeedMultiplier();
			yspeed *= ((Weapon) currentItem).getYSpeedMultiplier();
		}

		updateHealth();

		move();

		// if the player is out of the map
		if (hitbox.y + hitbox.height + 1 >= Main.mapManager.getMapHeight()) {
			currentHealth = 0;
		}

		// if the player is dead
		if (currentHealth <= 0) {
			if (currentWeapon != null) {
				((Weapon) currentWeapon).cancelAttack();
				((Weapon) currentWeapon).cancelUltimate();
			}
			Platform.exit();
		}
	}

	/**
	 * Get the z coordinate of the player
	 * @return 69
	 */
	@Override
	public int getZ() {
		return 69;
	}

	/**
	 * Get the current health of the player
	 * @return currentHealth
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * Get the max health of the player
	 * @return maxHealth
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * Get the current power of the player
	 * @return currentPower
	 */
	public int getCurrentPower() {
		return currentPower;
	}

	/**
	 * Set the current power of the player, must be non-negative
	 * @param power the power to set
	 */
	public void setCurrentPower(int power) {
		if (power < 0) {
			currentPower = 0;
		} else {
			currentPower = power;
		}
	}

	/**
	 * Get the sugar level of the player
	 * @return sugarLevel
	 */
	public int getSugarLevel() {
		return sugarLevel;
	}

	/**
	 * Set the sugar level of the player, must be non-negative
	 * @param sugarLevel the sugar level to set
	 */
	public void setSugarLevel(int sugarLevel) {
		if (sugarLevel < 0) {
			this.sugarLevel = 0;
		} else {
			this.sugarLevel = sugarLevel;
		}
	}

	/**
	 * Get player's inventory
	 * @return inventory
	 * @see item.Item
	 */
	public Item[] getInventory() {
		return inventory;
	}

	/**
	 * Get the current index of the inventory
	 * @return currentInventoryFocus
	 */
	public int getCurrentInventoryFocus() {
		return currentInventoryFocus;
	}

	/**
	 * Update the current index of the inventory
	 * according to the mouse scroll and number keys
	 * @see input.InputUtility
	 */
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

	/**
	 * Update the health state of the player
	 * whether the player is healthy, hyperglycemia or hypoglycemia
	 */
	private void updateHealthState() {
		if (sugarLevel > HYPERGLYCEMIA_SUGAR_LEVEL)
			healthState = HYPERGLYCEMIA;
		else if (sugarLevel < HYPOGLYCEMIA_SUGAR_LEVEL)
			healthState = HYPOGLYCEMIA;
		else
			healthState = HEALTHY;
	}

	/**
	 * When the player is hyperglycemia, the player will lose health
	 */
	private void initOnHyperglycemia() {
		if (onHyperglycemia != null && onHyperglycemia.isAlive())
			return;
		onHyperglycemia = new Thread(() -> {
			try {
				Thread.sleep(HYPERGLYCEMIA_DELAY);
				setCurrentHealth(currentHealth - HYPERGLYCEMIA_DAMAGE);
			} catch (InterruptedException e) {
				System.out.println("hyperglycemia interrupted");
			}
		});
		onHyperglycemia.start();
	}

	/**
	 * Clear the hyperglycemia thread
	 */
	public void clearHyperglycemiaThread() {
		if (onHyperglycemia != null)
			onHyperglycemia.interrupt();
	}

	/**
	 * Update the health of the player
	 * according to the health state
	 */
	private void updateHealth() {
		updateHealthState();
		switch (healthState) {
		case HYPERGLYCEMIA:
			initOnHyperglycemia();
			break;
		case HYPOGLYCEMIA:
			clearHyperglycemiaThread();
			xspeed *= HYPOGLYCEMIA_X_SPEED_MULTIPLIER;
			yspeed *= HYPOGLYCEMIA_Y_SPEED_MULTIPLIER;
			break;
		case HEALTHY:
			clearHyperglycemiaThread();
			break;
		default:
			break;
		}
	}

	/**
	 * Check if the player is on the door
	 * @return true if the player is on the door, false otherwise
	 * @see entity.Door
	 */
	private boolean isPlayerOnDoor() {
		for (Entity entity : GameLogic.getGameObjectContainer()) {
			if (entity instanceof Door && entity.getHitbox().intersects(hitbox)) {
				((Door) entity).interact();
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the player's current health
	 * @return currentHealth 
	 */
	@Override
	public int getHealth() {
		return currentHealth;
	}

	/**
	 * Get current weapon of the player
	 * @return currentWeapon
	 */
	public Weapon getCurrentWeapon() {
		return currentWeapon;
	}
}
