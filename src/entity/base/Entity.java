package entity.base;

import sharedObject.Renderable;

public abstract class Entity implements Renderable {

	private int x;
	private int y;

	public Entity(int x, int y) {
		setX(x);
		setY(y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
