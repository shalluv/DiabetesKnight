package entity;

import java.awt.Rectangle;

import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;

import static utils.Constants.EnemyConstants.*;

public class Enemy extends Entity {

	private int xspeed;
	private int yspeed;
	private Rectangle hitbox;
	private Image image;

	public Enemy(int x, int y) {
		super(x, y);
		xspeed = ORIGIN_X_SPEED;
		yspeed = ORIGIN_Y_SPEED;
		hitbox = new Rectangle(x, y + OFFSET_HITBOX_Y, WIDTH, HEIGHT - OFFSET_HITBOX_Y);
		image = new Image("file:res/Slime/stand_and_maybe_jump/slime2-1.png");
	}

	@Override
	public void draw(GraphicsContext gc) {

//		 Hitbox Rect
		gc.setFill(Color.GREEN);
		gc.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

		gc.drawImage(image, x, y, WIDTH, HEIGHT);
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
					setX(hitbox.x);
				}
			}
		}

		// gravity
		yspeed += WEIGHT;
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
					setY(hitbox.y - OFFSET_HITBOX_Y);
				}
			}
		}
		setX(x + xspeed);
		setY(y + yspeed);
	}

	public void update() {
		move();

		hitbox.x = x;
		hitbox.y = y + OFFSET_HITBOX_Y;
	}

	@Override
	public int getZ() {
		return 1;
	}

}
