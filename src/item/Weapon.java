package item;

import static utils.Constants.AttackState.READY;

import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Weapon Represents a weapon in the game A weapon is an item that can be used
 * to attack
 * 
 * @see item.Item
 */
public abstract class Weapon extends Item {

	/**
	 * The state of the attack of the weapon
	 */
	protected int attackState;
	/**
	 * The cooldown thread of the weapon
	 * 
	 * @see java.lang.Thread
	 */
	protected Thread cooldown;
	/**
	 * The ultimate thread of the weapon
	 * 
	 * @see java.lang.Thread
	 */
	protected Thread onUltimate;
	/**
	 * Whether the weapon is on ultimate
	 */
	protected boolean isOnUltimate;
	/**
	 * The x-axis speed multiplier of the weapon
	 */
	protected double XSpeedMultiplier;
	/**
	 * The y-axis speed multiplier of the weapon
	 */
	protected double YSpeedMultiplier;

	/**
	 * Constructor
	 * 
	 * @param name             name of the weapon
	 * @param image            image of the weapon
	 * @param XSpeedMultiplier X speed multiplier of the weapon
	 * @param YSpeedMultiplier Y speed multiplier of the weapon
	 */
	public Weapon(String name, Image image, double XSpeedMultiplier, double YSpeedMultiplier) {
		super(name, image);
		this.attackState = READY;
		this.XSpeedMultiplier = XSpeedMultiplier;
		this.YSpeedMultiplier = YSpeedMultiplier;
		this.isOnUltimate = false;
	}

	/**
	 * Get the X speed multiplier of the weapon
	 * 
	 * @return X speed multiplier of the weapon
	 */
	public double getXSpeedMultiplier() {
		return XSpeedMultiplier;
	}

	/**
	 * Get the Y speed multiplier of the weapon
	 * 
	 * @return Y speed multiplier of the weapon
	 */
	public double getYSpeedMultiplier() {
		return YSpeedMultiplier;
	}

	/**
	 * Initialize the cooldown thread
	 * 
	 * @param delay the delay of the cooldown
	 */
	protected void initCooldown(int delay) {
		cooldown = new Thread(() -> {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				System.out.println("attack cooldown interrupted");
			}
		});
	}

	/**
	 * Initialize the ultimate thread
	 * 
	 * @param delay the delay of the ultimate
	 */
	protected void initOnUltimate(int delay) {
		onUltimate = new Thread(() -> {
			try {
				Thread.sleep(delay);
				resetStatus();
			} catch (InterruptedException e) {
				System.out.println("ultimate cooldown interrupted");
			}
		});
	}

	/**
	 * Draw the weapon
	 * 
	 * @param gc           the graphics context of the canvas
	 * @param x            the x coordinate of the weapon
	 * @param y            the y coordinate of the weapon
	 * @param width        the width of the weapon
	 * @param height       the height of the weapon
	 * @param isFacingLeft whether the entity who is holding the weapon is facing
	 *                     left
	 */
	public abstract void draw(GraphicsContext gc, double x, double y, double width, double height,
			boolean isFacingLeft);

	/**
	 * Update the attacking progress
	 * 
	 * @param attacker the entity that is attacking
	 * @return the attacking state
	 */
	public abstract int updateAttack(Entity attacker);

	/**
	 * Attack the target
	 * 
	 * @param targetX  the x coordinate of the target
	 * @param targetY  the y coordinate of the target
	 * @param attacker the entity that is attacking
	 * @return the attacking state
	 */
	public abstract int attack(double targetX, double targetY, Entity attacker);

	/**
	 * Use the ultimate of the weapon
	 */
	public abstract void useUlitmate();

	/**
	 * Reset the status of the weapon
	 */
	public abstract void resetStatus();

	/**
	 * Cancel the attack
	 */
	public void cancelAttack() {
		this.attackState = READY;
		if (cooldown != null)
			cooldown.interrupt();
		cooldown = null;
	}

	/**
	 * Cancel the ultimate
	 */
	public void cancelUltimate() {
		if (onUltimate != null)
			onUltimate.interrupt();
		isOnUltimate = false;
	}

	/**
	 * Clear all the threads
	 */
	public void clearThread() {
		if (cooldown != null)
			cooldown.interrupt();
		if (onUltimate != null)
			onUltimate.interrupt();
	}

}
