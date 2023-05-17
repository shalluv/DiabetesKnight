package entity.base;

import java.awt.geom.Rectangle2D;

import sharedObject.Renderable;

public abstract class Entity implements Renderable {

	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected boolean isDestroy;
	protected Rectangle2D.Double hitbox;

	public Entity(double x, double y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isDestroy = false;
	}

	public abstract void update();

	@Override
	public boolean isDestroyed() {
		return isDestroy;
	}

	public Rectangle2D.Double getHitbox() {
		return hitbox;
	}

	public void setHitbox(Rectangle2D.Double hitbox) {
		this.hitbox = hitbox;
	}

	public void initHitbox(double x, double y, int width, int height) {
		hitbox = new Rectangle2D.Double(x, y, width, height);
	}

	public void setDestroy(boolean isDestroy) {
		this.isDestroy = isDestroy;
	}
}
