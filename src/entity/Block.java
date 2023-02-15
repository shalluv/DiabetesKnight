package entity;

import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Block extends Entity {

	private boolean isSolid;
	public static int WIDTH = 40;
	public static int HEIGHT = 40;
	private Image image;
	private Rectangle hitbox;

	public Block(double x, double y, String image_url, boolean isSolid) {
		super(x, y);
		hitbox = new Rectangle(Block.WIDTH, Block.HEIGHT);
		hitbox.setTranslateX(x);
		hitbox.setTranslateY(y);
		image = new Image(image_url);
		this.isSolid = isSolid;
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.drawImage(this.image, this.getX(), this.getY(), Block.WIDTH, Block.HEIGHT);
	}
	
	public boolean isSolid() {
		return isSolid;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
}
