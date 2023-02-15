package entity;

import java.awt.Rectangle;

import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Block extends Entity {

	private boolean isSolid;
	public static int width = 40;
	public static int height = 40;
	private Image image;
	private Rectangle hitbox;

	public Block(int x, int y, String image_url, boolean isSolid) {
		super(x, y);
		hitbox = new Rectangle(x, y, Block.width, Block.height);
		image = new Image(image_url);
		this.isSolid = isSolid;
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.drawImage(this.image, this.getX(), this.getY(), Block.width, Block.height);
	}
	
	public boolean isSolid() {
		return isSolid;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
}
