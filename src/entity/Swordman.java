package entity;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.EnemyConstants.SwordmenConstants.BASE_X_SPEED;
import static utils.Constants.EnemyConstants.SwordmenConstants.HEIGHT;
import static utils.Constants.EnemyConstants.SwordmenConstants.INITIAL_MAX_HEALTH;
import static utils.Constants.EnemyConstants.SwordmenConstants.INITIAL_X_SPEED;
import static utils.Constants.EnemyConstants.SwordmenConstants.INITIAL_Y_SPEED;
import static utils.Constants.EnemyConstants.SwordmenConstants.MAX_Y_SPEED;
import static utils.Constants.EnemyConstants.SwordmenConstants.SIGHT_SIZE;
import static utils.Constants.EnemyConstants.SwordmenConstants.WEIGHT;
import static utils.Constants.EnemyConstants.SwordmenConstants.WIDTH;
import static utils.Constants.EnemyConstants.SwordmenConstants.Animations.ANIMATION_SPEED;
import static utils.Constants.EnemyConstants.SwordmenConstants.Animations.ANIMATION_STATE_COUNT;
import static utils.Constants.EnemyConstants.SwordmenConstants.Animations.IDLE;
import static utils.Constants.EnemyConstants.SwordmenConstants.Animations.IDLE_FRAMES_COUNT;
import static utils.Constants.EnemyConstants.SwordmenConstants.Animations.SPRITE_SIZE;

import java.awt.geom.Rectangle2D;

import entity.base.MeleeEnemy;
import item.derived.Sword;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import utils.Loader;

/**
 * Swordman
 * Represents a swordman in the game
 * A swordman is a melee enemy
 * @see entity.base.MeleeEnemy
 * @see item.derived.Sword
 */
public class Swordman extends MeleeEnemy {

	/**
	 * Constructor
	 * @param x x coordinate of the swordman
	 * @param y y coordinate of the swordman
	 */
	public Swordman(double x, double y) {
		super(x, y, WIDTH, HEIGHT, SIGHT_SIZE, INITIAL_MAX_HEALTH, new Sword());
		loadResources();

		// Initialize stats
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		maxYSpeed = MAX_Y_SPEED;
		baseXSpeed = BASE_X_SPEED;
		initialXSpeed = INITIAL_X_SPEED;
		weight = WEIGHT;
		weapon = new Sword();
	}

	/**
	 * Load resources
	 */
	@Override
	protected void loadResources() {
		isFacingLeft = false;
		animation = new Image[ANIMATION_STATE_COUNT + 1];
		animationFrame = 0;
		frameCount = 0;
		animationState = IDLE;
		animation[0] = Loader.GetSpriteAtlas(Loader.MELEE_IDLE_ATLAS);
	}

	/**
	 * Draw the swordman
	 * @param gc GraphicsContext to draw on
	 * @param screen Rectangle2D.Double representing the screen
	 */
	@Override
	public void draw(GraphicsContext gc, Rectangle2D.Double screen) {
		if (!hitbox.intersects(screen))
			return;

		frameCount++;
		if (frameCount > ANIMATION_SPEED) {
			frameCount -= ANIMATION_SPEED;
			animationFrame++;
			switch (animationState) {
			case IDLE:
				animationFrame %= IDLE_FRAMES_COUNT;
				break;
			default:
				break;
			}
		}

		// Get the position to draw the sprite
		double drawX = hitbox.x + (isFacingLeft ? 0 : width);
		double drawY = hitbox.y;
		double drawW = width * (isFacingLeft ? 1 : -1);
		double drawH = height;

		gc.drawImage(animation[animationState], animationFrame * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, drawX, drawY,
				drawW, drawH);
		if (attackState == IN_PROGRESS)
			weapon.draw(gc, hitbox.x, hitbox.y, 32, 32, isFacingLeft);
		// draw HP
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.BOTTOM);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(currentHealth), hitbox.x, hitbox.y);
	}

}
