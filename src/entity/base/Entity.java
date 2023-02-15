package entity.base;

import sharedObject.Renderable;

public abstract class Entity implements Renderable {

	private double x;
	private double y;

	public Entity(double x, double y) {
		this.setX(x);
		this.setY(y);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}
}
