package entity;

import static utils.Constants.EnemyConstants.GunnerConstants.*;
import static utils.Constants.EnemyConstants.GunnerConstants.Animations.*;

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

public class Gunner extends RangedEnemy {

	public Gunner(double x, double y) {
		super(x, y, WIDTH, HEIGHT, SIGHT_SIZE, INITIAL_MAX_HEALTH);
		loadResources();
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		maxYSpeed = MAX_Y_SPEED;
		baseXSpeed = BASE_X_SPEED;
		initialXSpeed = INITIAL_X_SPEED;
		weight = WEIGHT;
		animationAttackCooldown = ATTACK_COOLDOWN;
		animationIDLE = IDLE;
		weapon = new Gun();
	}

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

		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.BOTTOM);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(currentHealth), hitbox.x, hitbox.y);
	}

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
