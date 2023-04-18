package entity;

import java.awt.Rectangle;

import entity.base.Entity;
import input.InputUtility;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
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

	public Player(int x, int y) {
		super(x, y);
		hitbox = new Rectangle(x - offsetHitboxX, y + offsetHitboxY, width - hitboxWidthReducer,
				height - offsetHitboxY);
		image = new Image("file:res/Owlet_Monster/Owlet_Monster.png");
	}

	private void clampInCanvas() {
		if (hitbox.x < 0) {
			setX(-offsetHitboxX);
		} else if (hitbox.x + width + offsetHitboxX - hitboxWidthReducer > Map.getWidth()) {
			setX(Map.getWidth() - width + offsetHitboxX);
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
//		gc.setFill(Color.GREEN);
//		gc.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

//		Image
		gc.drawImage(image, x, y, width, height);
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	private void jump() {
		yspeed = -maxYSpeed;
	}

	private void move() {
		hitbox.x += xspeed;
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid()) {
				if (((Block) block).getHitbox().intersects(hitbox)) {
					hitbox.x -= xspeed;
					while (!((Block) block).getHitbox().intersects(hitbox)) {
						hitbox.x += Math.signum(xspeed);
					}
					hitbox.x -= Math.signum(xspeed);
					xspeed = 0;
					setX(hitbox.x - offsetHitboxX);
				}
			}
		}

		// gravity
		yspeed += weight;
		hitbox.y += yspeed;
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid()) {
				if (((Block) block).getHitbox().intersects(hitbox)) {
					hitbox.y -= yspeed;
					while (!((Block) block).getHitbox().intersects(hitbox)) {
						hitbox.y += Math.signum(yspeed);
					}
					hitbox.y -= Math.signum(yspeed);
					yspeed = 0;
					setY(hitbox.y - offsetHitboxY);
				}
			}
		}
		setX(x + xspeed);
		setY(y + yspeed);
	}

	public void update() {
		if (InputUtility.getKeyPressed(KeyCode.SPACE)) {

			hitbox.y += 1;
			for (Renderable block : RenderableHolder.getInstance().getEntities()) {
				if (block instanceof Block && ((Block) block).isSolid()) {
					if (((Block) block).getHitbox().intersects(hitbox)) {
						jump();
					}
				}
			}
			hitbox.y -= 1;

		}
		if (InputUtility.getKeyPressed(KeyCode.A)) {
			xspeed = -baseXSpeed;
		} else if (InputUtility.getKeyPressed(KeyCode.D)) {
			xspeed = baseXSpeed;
		} else {
			xspeed = 0;
		}

		if (yspeed < -maxYSpeed) {
			yspeed = -maxYSpeed;
		} else if (yspeed > maxYSpeed) {
			yspeed = maxYSpeed;
		}

		move();

		clampInCanvas();

		hitbox.x = x + offsetHitboxX;
		hitbox.y = y + offsetHitboxY;
	}
}
