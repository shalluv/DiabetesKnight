package entity.base;

import sharedObject.Renderable;

public abstract class Entity implements Renderable {

	protected int x;
	protected int y;

	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
