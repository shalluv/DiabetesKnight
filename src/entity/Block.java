package entity;

import java.awt.Rectangle;

import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Block extends Entity {

	public static int default_width = 40;
	public static int default_height = 40;
	private boolean isSolid;
	private Image image;
	private Rectangle hitbox;

	public Block(int x, int y, String image_url, boolean isSolid) {
		super(x, y);
		setHitbox(new Rectangle(x, y, Block.default_width, Block.default_height));
		setImage(new Image(image_url));
		setSolid(isSolid);
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.drawImage(image, x, y, Block.width, Block.height);
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public Image getImage() {
		return image;
	}

	public boolean isSolid() {
		return isSolid;
	}

	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setSolid(boolean isSolid) {
		this.isSolid = isSolid;
	}
}
