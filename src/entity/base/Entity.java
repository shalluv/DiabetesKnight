package entity.base;

import sharedObject.Renderable;

public abstract class Entity implements Renderable {

	protected double x;
	protected double y;

	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
}
