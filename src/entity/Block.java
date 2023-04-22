package entity;

import java.awt.Rectangle;

import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import static utils.Constants.BlockConstants.*;

public class Block extends Entity {

	private boolean isSolid;
	private Image image;
	private Rectangle hitbox;

	public Block(int x, int y, String image_url, boolean isSolid) {
		super(x, y);
		hitbox = new Rectangle(x, y, WIDTH, HEIGHT);
		image = new Image(image_url);
		this.isSolid = isSolid;
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.drawImage(image, x, y, WIDTH, HEIGHT);
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public boolean isSolid() {
		return isSolid;
	}

	@Override
	public int getZ() {
		return 0;
	}
}
