package entity;

import static utils.Constants.PlayerConstants.BASE_X_SPEED;
import static utils.Constants.PlayerConstants.HEIGHT;
import static utils.Constants.PlayerConstants.HITBOX_WIDTH_REDUCER;
import static utils.Constants.PlayerConstants.INITIAL_X_SPEED;
import static utils.Constants.PlayerConstants.INITIAL_Y_SPEED;
import static utils.Constants.PlayerConstants.MAX_Y_SPEED;
import static utils.Constants.PlayerConstants.OFFSET_HITBOX_X;
import static utils.Constants.PlayerConstants.OFFSET_HITBOX_Y;
import static utils.Constants.PlayerConstants.WEIGHT;
import static utils.Constants.PlayerConstants.WIDTH;

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

	private int maxHealth;
	private int currentHealth;
	private double xspeed;
	private double yspeed;
	private Rectangle hitbox;
	private Image image;

	public Player(int x, int y) {
		super(x, y);
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		hitbox = new Rectangle(x - OFFSET_HITBOX_X, y + OFFSET_HITBOX_Y, WIDTH - HITBOX_WIDTH_REDUCER,
				HEIGHT - OFFSET_HITBOX_Y);
		image = new Image("file:res/Owlet_Monster/Owlet_Monster.png");
		maxHealth = 100;
		currentHealth = 100;
	}

	private void clampInCanvas() {
		if (hitbox.x < 0) {
			setX(-OFFSET_HITBOX_X);
		} else if (hitbox.x + WIDTH + OFFSET_HITBOX_X - HITBOX_WIDTH_REDUCER > Map.getWidth()) {
			setX(Map.getWidth() - WIDTH + OFFSET_HITBOX_X);
		}
		if (y < 0) {
			setY(0);
		} else if (y > Map.getHeight() - HEIGHT) {
			Platform.exit();
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		// Player Rect
		// gc.setFill(Color.RED);
		// gc.fillRect(getX(), getY(), WIDTH, HEIGHT);

		// Hitbox Rect
		// gc.setFill(Color.GREEN);
		// gc.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

		// Image
		gc.drawImage(image, x, y, WIDTH, HEIGHT);
	}

	private void setCurrentHealth(int value) {
		if (value < 0) {
			currentHealth = 0;
		} else if (value > maxHealth) {
			currentHealth = maxHealth;
		} else {
			currentHealth = value;
		}
	}

	public void receiveDamage(int damage) {
		setCurrentHealth(currentHealth - damage);
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	private void jump() {
		yspeed = -MAX_Y_SPEED;
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
					setX(hitbox.x - OFFSET_HITBOX_X);
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
			xspeed = -BASE_X_SPEED;
		} else if (InputUtility.getKeyPressed(KeyCode.D)) {
			xspeed = BASE_X_SPEED;
		} else {
			xspeed = 0;
		}

		if (yspeed < -MAX_Y_SPEED) {
			yspeed = -MAX_Y_SPEED;
		} else if (yspeed > MAX_Y_SPEED) {
			yspeed = MAX_Y_SPEED;
		}

		move();

		// if the player is dead
		if (currentHealth == 0) {
			Platform.exit();
		}

		clampInCanvas();

		hitbox.x = (int) (x + OFFSET_HITBOX_X);
		hitbox.y = (int) (y + OFFSET_HITBOX_Y);
	}

	@Override
	public int getZ() {
		return 69;
	}
}
