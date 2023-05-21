package entity.base;

import java.awt.geom.Rectangle2D;

import sharedObject.Renderable;

/**
 * Entity
 * Represents an entity in the game
 * @see sharedObject.Renderable
 */
public abstract class Entity implements Renderable {

	/**
	 * x coordinate of the entity
	 */
	protected double x;
	/**
	 * y coordinate of the entity
	 */
	protected double y;
	/**
	 * width of the entity
	 */
	protected int width;
	/**
	 * height of the entity
	 */
	protected int height;
	/**
	 * Whether the entity is destroyed
	 */
	protected boolean isDestroy;
	/**
	 * The hitbox of the entity
	 * @see java.awt.geom.Rectangle2D.Double
	 */
	protected Rectangle2D.Double hitbox;

	/**
	 * Constructor
	 * @param x x coordinate of the entity
	 * @param y y coordinate of the entity
	 * @param width width of the entity
	 * @param height height of the entity
	 */
	public Entity(double x, double y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isDestroy = false;
	}

	/**
	 * Update the entity
	 */
	public abstract void update();

	/**
	 * Check if the entity is destroyed
	 * @return true if the entity is destroyed, false otherwise
	 */
	@Override
	public boolean isDestroyed() {
		return isDestroy;
	}

	/**
	 * Get the hitbox of the entity
	 * @return the hitbox of the entity
	 * @see java.awt.geom.Rectangle2D.Double
	 */
	public Rectangle2D.Double getHitbox() {
		return hitbox;
	}

	/**
	 * Set the hitbox of the entity
	 * @param hitbox the hitbox to be set
	 * @see java.awt.geom.Rectangle2D.Double
	 */
	public void setHitbox(Rectangle2D.Double hitbox) {
		this.hitbox = hitbox;
	}

	/**
	 * Initialize the hitbox of the entity
	 * @param x x coordinate of the hitbox
	 * @param y y coordinate of the hitbox
	 * @param width width of the hitbox
	 * @param height height of the hitbox
	 * @see java.awt.geom.Rectangle2D.Double
	 */
	public void initHitbox(double x, double y, int width, int height) {
		hitbox = new Rectangle2D.Double(x, y, width, height);
	}

	/**
	 * Set the entity to be destroyed
	 * @param isDestroy true if the entity is to be destroyed, false otherwise
	 */
	public void setDestroy(boolean isDestroy) {
		this.isDestroy = isDestroy;
	}
}
