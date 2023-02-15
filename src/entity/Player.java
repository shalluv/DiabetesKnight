package entity;

import java.awt.Rectangle;

import entity.base.Entity;
import input.InputUtility;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import logic.Map;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;

public class Player extends Entity {

	private int width = 45;
	private int height = 45;
	private int maxYSpeed = 16;
	private int baseXSpeed = 5;
	private int xspeed = 0;
	private int yspeed = 32;
	private int weight = 1;
	private Rectangle hitbox;
	private Image image;
	private int offsetHitboxX = 8;
	private int offsetHitboxY = 8;
	private int hitboxWidthReducer = 20;
	private boolean isFaceLeft = false;

	public Player(int x, int y) {
		super(x, y);
		setHitbox(new Rectangle(x - getOffsetHitboxX(), y + getOffsetHitboxY(), getWidth() - getHitboxWidthReducer(),
				getHeight() - getOffsetHitboxY()));
		setImage(new Image("file:res/Owlet_Monster/Owlet_Monster.png"));
	}

	private void clampInCanvas() {
		if (getHitbox().x < 0) {
			setX(-getOffsetHitboxX());
		} else if (getHitbox().x + getWidth() + getOffsetHitboxX() - getHitboxWidthReducer() > Map.getWidth()) {
			setX(Map.getWidth() - getWidth() + getOffsetHitboxX());
		}
		if (getY() < 0) {
			setY(0);
		} else if (getY() > Map.getHeight() - getHeight()) {
			Platform.exit();
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
//		Player Rect
//		gc.setFill(Color.RED);
//		gc.fillRect(getX(), getY(), width, height);

//		Hitbox Rect
		gc.setFill(Color.GREEN);
		gc.fillRect(getHitbox().x, getHitbox().y, getHitbox().width, getHitbox().height);

//		Image
		if (isFaceLeft()) {
			gc.drawImage(getImage(), getX() + getWidth(), getY(), -getWidth(), getHeight());
		} else {
			gc.drawImage(getImage(), getX(), getY(), getWidth(), getHeight());
		}
	}

	private void jump() {
		setYspeed(-getMaxYSpeed());
	}

	private void move() {
		getHitbox().x += getXspeed();
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid()) {
				if (((Block) block).getHitbox().intersects(getHitbox())) {
					getHitbox().x -= getXspeed();
					while (!((Block) block).getHitbox().intersects(getHitbox())) {
						getHitbox().x += Math.signum(getXspeed());
					}
					getHitbox().x -= Math.signum(getXspeed());
					setXspeed(0);
					setX(getHitbox().x - getOffsetHitboxX());
				}
			}
		}

		// gravity
		setYspeed(getYspeed() + getWeight());
		getHitbox().y += getYspeed();
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid()) {
				if (((Block) block).getHitbox().intersects(getHitbox())) {
					getHitbox().y -= getYspeed();
					while (!((Block) block).getHitbox().intersects(getHitbox())) {
						getHitbox().y += Math.signum(getYspeed());
					}
					getHitbox().y -= Math.signum(getYspeed());
					setYspeed(0);
					setY(getHitbox().y - getOffsetHitboxY());
				}
			}
		}
		setX(getX() + getXspeed());
		setY(getY() + getYspeed());
	}

	public void update() {
		if (InputUtility.getKeyPressed(KeyCode.SPACE)) {

			getHitbox().y += 1;
			for (Renderable block : RenderableHolder.getInstance().getEntities()) {
				if (block instanceof Block && ((Block) block).isSolid()) {
					if (((Block) block).getHitbox().intersects(getHitbox())) {
						jump();
					}
				}
			}
			getHitbox().y -= 1;

		}
		if (InputUtility.getKeyPressed(KeyCode.A)) {
			setXspeed(-getBaseXSpeed());
		} else if (InputUtility.getKeyPressed(KeyCode.D)) {
			setXspeed(getBaseXSpeed());
		} else {
			setXspeed(0);
		}

		if (getXspeed() > 0) {
			setFaceLeft(false);
			// TODO: Get rid of magic number
			setOffsetHitboxX(8);
		} else if (getXspeed() < 0) {
			setFaceLeft(true);
			// TODO: Get rid of magic number
			setOffsetHitboxX(12);
		}

		if (getYspeed() < -getMaxYSpeed()) {
			setYspeed(-getMaxYSpeed());
		} else if (getYspeed() > getMaxYSpeed()) {
			setYspeed(getMaxYSpeed());
		}

		move();

		clampInCanvas();

		getHitbox().x = getX() + getOffsetHitboxX();
		getHitbox().y = getY() + getOffsetHitboxY();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getMaxYSpeed() {
		return maxYSpeed;
	}

	public void setMaxYSpeed(int maxYSpeed) {
		this.maxYSpeed = maxYSpeed;
	}

	public int getBaseXSpeed() {
		return baseXSpeed;
	}

	public void setBaseXSpeed(int baseXSpeed) {
		this.baseXSpeed = baseXSpeed;
	}

	public int getXspeed() {
		return xspeed;
	}

	public void setXspeed(int xspeed) {
		this.xspeed = xspeed;
	}

	public int getYspeed() {
		return yspeed;
	}

	public void setYspeed(int yspeed) {
		this.yspeed = yspeed;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public int getOffsetHitboxX() {
		return offsetHitboxX;
	}

	public void setOffsetHitboxX(int offsetHitboxX) {
		this.offsetHitboxX = offsetHitboxX;
	}

	public int getOffsetHitboxY() {
		return offsetHitboxY;
	}

	public void setOffsetHitboxY(int offsetHitboxY) {
		this.offsetHitboxY = offsetHitboxY;
	}

	public int getHitboxWidthReducer() {
		return hitboxWidthReducer;
	}

	public void setHitboxWidthReducer(int hitboxWidthReducer) {
		this.hitboxWidthReducer = hitboxWidthReducer;
	}

	public boolean isFaceLeft() {
		return isFaceLeft;
	}

	public void setFaceLeft(boolean isFaceLeft) {
		this.isFaceLeft = isFaceLeft;
	}
}
