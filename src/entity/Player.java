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
		if (y < 0) {
			setY(0);
		} else if (y > Map.getHeight() - height) {
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
		gc.drawImage(image, x, y, width, height);
	}

	public Rectangle getHitbox() {
		return hitbox;
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
		setX(x + xspeed);
		setY(y + yspeed);
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

		hitbox.x = x + offsetHitboxX;
		hitbox.y = y + offsetHitboxY;
	}
}
