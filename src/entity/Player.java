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

import java.awt.geom.Rectangle2D;

import entity.base.Entity;
import input.InputUtility;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import utils.Helper;

public class Player extends Entity {

	private int maxHealth;
	private int currentHealth;
	private double xspeed;
	private double yspeed;
	private Image image;

	public Player(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		initHitbox(x - OFFSET_HITBOX_X, y + OFFSET_HITBOX_Y, WIDTH - HITBOX_WIDTH_REDUCER, HEIGHT - OFFSET_HITBOX_Y);
		image = new Image("file:res/Owlet_Monster/Owlet_Monster.png");
		maxHealth = 100;
		currentHealth = 100;
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.drawImage(image, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
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

	public Rectangle2D.Double getHitbox() {
		return hitbox;
	}

	private void jump() {
		yspeed = -MAX_Y_SPEED;
	}

	private void move() {
		if (Helper.CanMoveHere(hitbox.x + xspeed, hitbox.y, hitbox.width, hitbox.height)) {
			hitbox.x += xspeed;
		} else {
			hitbox.x = Helper.GetEntityXPosNextToWall(hitbox, xspeed);
		}

		if (Helper.CanMoveHere(hitbox.x, hitbox.y + yspeed, hitbox.width, hitbox.height)) {
			hitbox.y += yspeed;
			yspeed += WEIGHT;
		} else {
			hitbox.y = Helper.GetEntityYPosUnderRoofOrAboveFloor(hitbox, yspeed);
		}
	}

	public void update() {
		if (InputUtility.getKeyPressed(KeyCode.SPACE)) {

			if (!Helper.IsEntityOnFloor(hitbox)) {
				jump();
			}

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
	}

	@Override
	public int getZ() {
		return 69;
	}
}
