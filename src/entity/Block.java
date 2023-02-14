package entity;

import java.awt.Rectangle;

import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Block extends Entity {

	private int WIDTH = 40;
	private int HEIGHT = 40;
	private Rectangle hitbox;

	public Block(int x, int y) {
		super(x, y);
		hitbox = new Rectangle(x, y, WIDTH, HEIGHT);
	}

	@Override
	public void draw(GraphicsContext gc) {
		// draw red rectangle
		gc.setFill(Color.RED);
		gc.fillRect(this.getX(), this.getY(), 40, 40);
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
}
