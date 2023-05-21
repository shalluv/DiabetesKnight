package entity;

import static utils.Constants.EnemyConstants.GunnerConstants.ATTACK_RANGE;
import static utils.Constants.EnemyConstants.GunnerConstants.BASE_X_SPEED;
import static utils.Constants.EnemyConstants.GunnerConstants.HEIGHT;
import static utils.Constants.EnemyConstants.GunnerConstants.INITIAL_MAX_HEALTH;
import static utils.Constants.EnemyConstants.GunnerConstants.INITIAL_X_SPEED;
import static utils.Constants.EnemyConstants.GunnerConstants.INITIAL_Y_SPEED;
import static utils.Constants.EnemyConstants.GunnerConstants.MAX_Y_SPEED;
import static utils.Constants.EnemyConstants.GunnerConstants.SIGHT_SIZE;
import static utils.Constants.EnemyConstants.GunnerConstants.WEIGHT;
import static utils.Constants.EnemyConstants.GunnerConstants.WIDTH;
import static utils.Constants.EnemyConstants.GunnerConstants.Animations.ANIMATION_SPEED;
import static utils.Constants.EnemyConstants.GunnerConstants.Animations.ANIMATION_STATE_COUNT;
import static utils.Constants.EnemyConstants.GunnerConstants.Animations.ATTACK_COOLDOWN;
import static utils.Constants.EnemyConstants.GunnerConstants.Animations.ATTACK_COOLDOWN_FRAMES_COUNT;
import static utils.Constants.EnemyConstants.GunnerConstants.Animations.IDLE;
import static utils.Constants.EnemyConstants.GunnerConstants.Animations.IDLE_FRAMES_COUNT;
import static utils.Constants.EnemyConstants.GunnerConstants.Animations.SPRITE_SIZE;

import java.awt.geom.Rectangle2D;

import entity.base.RangedEnemy;
import item.derived.Gun;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import logic.GameLogic;
import utils.Helper;
import utils.Loader;

/**
 * Gunner
 * Represents a gunner in the game
 * A gunner is a ranged enemy
 * @see entity.base.RangedEnemy
 * @see item.derived.Gun
 */
public class Gunner extends RangedEnemy {

	/**
	 * Constructor
	 * @param x x coordinate of the gunner
	 * @param y y coordinate of the gunner
	 */
	public Gunner(double x, double y) {
		super(x, y, WIDTH, HEIGHT, SIGHT_SIZE, INITIAL_MAX_HEALTH, new Gun());
		loadResources();
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		maxYSpeed = MAX_Y_SPEED;
		baseXSpeed = BASE_X_SPEED;
		initialXSpeed = INITIAL_X_SPEED;
		weight = WEIGHT;
		animationAttackCooldown = ATTACK_COOLDOWN;
		animationIDLE = IDLE;
	}

	/**
	 * Load the sprites of the gunner
	 * @see utils.Loader
	 */
	@Override
	protected void loadResources() {
		isFacingLeft = false;
		animation = new Image[ANIMATION_STATE_COUNT + 1];
		animationFrame = 0;
		frameCount = 0;
		animationState = IDLE;
		animation[0] = Loader.GetSpriteAtlas(Loader.RANGE_IDLE_ATLAS);
		animation[1] = Loader.GetSpriteAtlas(Loader.RANGE_ATTACK_COOLDOWN_ATLAS);
	}

	/**
	 * Draw the gunner on the screen
	 * @param gc GraphicsContext of the canvas
	 * @param screen Rectangle2D.Double representing the screen
	 * @see javafx.scene.canvas.GraphicsContext
	 * @see java.awt.geom.Rectangle2D.Double
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
			case ATTACK_COOLDOWN:
				animationFrame %= ATTACK_COOLDOWN_FRAMES_COUNT;
			default:
				break;
			}
		}

		double drawX = hitbox.x + (isFacingLeft ? 0 : width);
		double drawY = hitbox.y;
		double drawW = width * (isFacingLeft ? 1 : -1);
		double drawH = height;

		gc.drawImage(animation[animationState], animationFrame * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, drawX, drawY,
				drawW, drawH);

		// Draw health
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.BOTTOM);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(currentHealth), hitbox.x, hitbox.y);
	}

	/**
	 * Check if the gunner can attack
	 * @return true if the gunner can attack, false otherwise
	 */
	@Override
	protected boolean canAttack() {
		Rectangle2D.Double canAttackBox = new Rectangle2D.Double(hitbox.getCenterX() - ATTACK_RANGE,
				hitbox.getCenterY() - ATTACK_RANGE, 2 * ATTACK_RANGE, 2 * ATTACK_RANGE);
		if (canAttackBox.intersects(GameLogic.getPlayer().getHitbox()) && Helper.IsEntityOnFloor(hitbox)) {
			return Bullet.canBulletHit(GameLogic.getPlayer().getHitbox(), hitbox, canAttackBox);
		}
		return false;
	}

}
