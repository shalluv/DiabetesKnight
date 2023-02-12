package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sharedObject.Renderable;

public class Entity implements Renderable {

	private int x;
	private int y;

	public Entity(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void draw(GraphicsContext gc) {
		// draw red rectangle
		gc.setFill(Color.RED);
		gc.fillRect(x, y, 40, 40);
	}

	@Override
	public boolean isDestroyed() {
		return false;
	}
}
