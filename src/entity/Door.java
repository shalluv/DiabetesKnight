package entity;

import static utils.Constants.DoorConstants.HEIGHT;
import static utils.Constants.DoorConstants.WIDTH;
import static utils.Constants.DoorConstants.Animations.ANIMATION_SPEED;
import static utils.Constants.DoorConstants.Animations.FRAMES_COUNT;
import static utils.Constants.DoorConstants.Animations.SPRITE_SIZE;

import java.awt.geom.Rectangle2D.Double;

import application.Main;
import entity.base.Entity;
import interfaces.Interactable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Constants.GameState;
import utils.Helper;
import utils.Loader;

public class Door extends Entity implements Interactable {

	private Image image;
	private int yspeed = 8;
	private int frameCount;
	private int animationFrame;

	public Door(double x, double y) {
		super(x, y, WIDTH, HEIGHT);
		initHitbox(x, y, width, height);
		loadResources();
	}

	private void loadResources() {
		frameCount = 0;
		animationFrame = 0;
		image = Loader.GetSpriteAtlas(Loader.DOOR_ATLAS);
	}

	@Override
	public void draw(GraphicsContext gc, Double screen) {
		if (!screen.intersects(hitbox))
			return;
		frameCount++;
		if (frameCount > ANIMATION_SPEED) {
			frameCount -= ANIMATION_SPEED;
			animationFrame++;
			animationFrame %= FRAMES_COUNT;
		}
		gc.drawImage(image, animationFrame * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, hitbox.x, hitbox.y, hitbox.width,
				hitbox.height);
	}

	@Override
	public int getZ() {
		return -1;
	}

	@Override
	public void update() {
		fall();
	}

	private void fall() {
		if (Helper.CanMoveHere(hitbox.x, hitbox.y + 1, hitbox.width, hitbox.height)) {
			hitbox.y += 1;
			yspeed += 1;
		} else {
			hitbox.y = Helper.GetEntityYPosUnderRoofOrAboveFloor(hitbox, yspeed);
			if (yspeed < 0) {
				yspeed = 0;
				yspeed += 1;
			}
		}
	}

	@Override
	public void interact() {
		Main.gameState = GameState.CHANGING_LEVEL;
	}

}
