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

/**
 * Door Represents a door in the game
 */
public class Door extends Entity implements Interactable {

	/**
	 * The image of the door
	 * 
	 * @see javafx.scene.image.Image
	 */
	private Image image;
	/**
	 * The y-axis speed of the door
	 */
	private int yspeed = 8;
	/**
	 * The counter of the door's animation frame
	 */
	private int frameCount;
	/**
	 * The frame of the animation of the door
	 */
	private int animationFrame;

	/**
	 * Constructor
	 * 
	 * @param x x coordinate of the door
	 * @param y y coordinate of the door
	 */
	public Door(double x, double y) {
		super(x, y, WIDTH, HEIGHT);
		initHitbox(x, y, width, height);
		loadResources();
	}

	/**
	 * Load resources Load the image of the door
	 * 
	 * @see utils.Loader
	 */
	private void loadResources() {
		frameCount = 0;
		animationFrame = 0;
		image = Loader.GetSpriteAtlas(Loader.DOOR_ATLAS);
	}

	/**
	 * Draw the door
	 * 
	 * @param gc     GraphicsContext
	 * @param screen the screen
	 */
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

	/**
	 * Get the z coordinate of the door
	 * 
	 * @return the z coordinate of the door
	 * @see sharedObject.Renderable
	 */
	@Override
	public int getZ() {
		return -1;
	}

	/**
	 * Update the door
	 * 
	 * @see #fall()
	 */
	@Override
	public void update() {
		fall();
	}

	/**
	 * Make the door fall in case it is not on the ground
	 * 
	 * @see utils.Helper#CanMoveHere(double, double, double, double)
	 * @see utils.Helper#GetEntityYPosUnderRoofOrAboveFloor(java.awt.geom.Rectangle2D.Double,
	 *      double)
	 */
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

	/**
	 * Interact with the door
	 * 
	 * @see interfaces.Interactable
	 */
	@Override
	public void interact() {
		Main.gameState = GameState.CHANGING_LEVEL;
		Loader.playSound(Loader.WARP_SOUND_ATLAS);
	}

}
