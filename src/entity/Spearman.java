package entity;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.EnemyConstants.SpearmenConstants.BASE_X_SPEED;
import static utils.Constants.EnemyConstants.SpearmenConstants.HEIGHT;
import static utils.Constants.EnemyConstants.SpearmenConstants.INITIAL_MAX_HEALTH;
import static utils.Constants.EnemyConstants.SpearmenConstants.INITIAL_X_SPEED;
import static utils.Constants.EnemyConstants.SpearmenConstants.INITIAL_Y_SPEED;
import static utils.Constants.EnemyConstants.SpearmenConstants.MAX_Y_SPEED;
import static utils.Constants.EnemyConstants.SpearmenConstants.SIGHT_SIZE;
import static utils.Constants.EnemyConstants.SpearmenConstants.WEIGHT;
import static utils.Constants.EnemyConstants.SpearmenConstants.WIDTH;
import static utils.Constants.EnemyConstants.SpearmenConstants.Animations.ANIMATION_SPEED;
import static utils.Constants.EnemyConstants.SpearmenConstants.Animations.ANIMATION_STATE_COUNT;
import static utils.Constants.EnemyConstants.SpearmenConstants.Animations.IDLE;
import static utils.Constants.EnemyConstants.SpearmenConstants.Animations.IDLE_FRAMES_COUNT;
import static utils.Constants.EnemyConstants.SpearmenConstants.Animations.SPRITE_SIZE;

import java.awt.geom.Rectangle2D.Double;

import entity.base.MeleeEnemy;
import item.derived.Spear;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import utils.Loader;

public class Spearman extends MeleeEnemy {

	public Spearman(double x, double y) {
		super(x, y, WIDTH, HEIGHT, SIGHT_SIZE, INITIAL_MAX_HEALTH, new Spear());
		loadResources();
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		maxYSpeed = MAX_Y_SPEED;
		baseXSpeed = BASE_X_SPEED;
		initialXSpeed = INITIAL_X_SPEED;
		weight = WEIGHT;
		weapon = new Spear();
	}

	@Override
	public void draw(GraphicsContext gc, Double screen) {
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

	@Override
	protected void loadResources() {
		isFacingLeft = false;
		animation = new Image[ANIMATION_STATE_COUNT + 1];
		animationFrame = 0;
		frameCount = 0;
		animationState = IDLE;
		animation[0] = Loader.GetSpriteAtlas(Loader.MELEE_IDLE_ATLAS);
	}

}
