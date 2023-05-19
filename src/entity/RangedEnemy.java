package entity;

import static utils.Constants.AttackState.READY;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.RangedConstants.ATTACK_RANGE;
import static utils.Constants.EnemyConstants.RangedConstants.BASE_X_SPEED;
import static utils.Constants.EnemyConstants.RangedConstants.HEIGHT;
import static utils.Constants.EnemyConstants.RangedConstants.INITIAL_MAX_HEALTH;
import static utils.Constants.EnemyConstants.RangedConstants.INITIAL_X_SPEED;
import static utils.Constants.EnemyConstants.RangedConstants.INITIAL_Y_SPEED;
import static utils.Constants.EnemyConstants.RangedConstants.MAX_Y_SPEED;
import static utils.Constants.EnemyConstants.RangedConstants.SIGHT_SIZE;
import static utils.Constants.EnemyConstants.RangedConstants.WEIGHT;
import static utils.Constants.EnemyConstants.RangedConstants.WIDTH;
import static utils.Constants.EnemyConstants.RangedConstants.Animations.ANIMATION_SPEED;
import static utils.Constants.EnemyConstants.RangedConstants.Animations.ANIMATION_STATE_COUNT;
import static utils.Constants.EnemyConstants.RangedConstants.Animations.ATTACK_COOLDOWN;
import static utils.Constants.EnemyConstants.RangedConstants.Animations.ATTACK_COOLDOWN_FRAMES_COUNT;
import static utils.Constants.EnemyConstants.RangedConstants.Animations.IDLE;
import static utils.Constants.EnemyConstants.RangedConstants.Animations.IDLE_FRAMES_COUNT;
import static utils.Constants.EnemyConstants.RangedConstants.Animations.SPRITE_SIZE;

import java.awt.geom.Rectangle2D;

import entity.base.Enemy;
import item.RangedWeapon;
import item.derived.Gun;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import logic.GameLogic;
import utils.Helper;
import utils.Loader;

public class RangedEnemy extends Enemy {

	private double xspeed;
	private double yspeed;
	private int attackState;
	private RangedWeapon weapon;
	private int animationFrame;
	private int animationState;
	private int frameCount;
	private Image[] animation;
	private boolean isFacingLeft;

	public RangedEnemy(double x, double y) {
		super(x, y, WIDTH, HEIGHT, SIGHT_SIZE, INITIAL_MAX_HEALTH);
		attackState = READY;
		loadResources();
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		initHitbox(x, y, width, height);
		weapon = new Gun();
	}

	private void loadResources() {
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
	public void update() {
		if (GameLogic.getPlayer() != null) {
			updateXSpeed();
			if (GameLogic.getPlayer().getHitbox().getMinX() > hitbox.getMaxX()) {
				isFacingLeft = false;
			} else if (GameLogic.getPlayer().getHitbox().getMaxX() < hitbox.getMinX()) {
				isFacingLeft = true;
			}
		}
		xspeed *= weapon.getSpeedMultiplier();
		yspeed = Math.max(-MAX_Y_SPEED, Math.min(yspeed, MAX_Y_SPEED));
		move();
		if (attackState != READY) {
			animationState = ATTACK_COOLDOWN;
			attackState = weapon.updateAttack(this);
		} else {
			animationState = IDLE;
		}
		if (GameLogic.getPlayer() != null && canAttack() && attackState == READY)
			attackState = weapon.attack(GameLogic.getPlayer().getHitbox().getCenterX(),
					GameLogic.getPlayer().getHitbox().getCenterY(), this);
		if (currentHealth <= 0) {
			isDestroy = true;
			if (attackState != READY)
				weapon.cancelAttack();
		}
	}

	private void move() {
		if (Helper.CanMoveHere(hitbox.x + xspeed, hitbox.y, hitbox.width, hitbox.height)) {
			hitbox.x += xspeed;
		} else {
			hitbox.x = Helper.GetEntityXPosNextToWall(hitbox, xspeed);
			if (attackState == READY)
				jump();
		}
		if (Helper.CanMoveHere(hitbox.x, hitbox.y + yspeed, hitbox.width, hitbox.height)) {
			hitbox.y += yspeed;
			yspeed += WEIGHT;
		} else {
			hitbox.y = Helper.GetEntityYPosUnderRoofOrAboveFloor(hitbox, yspeed);
			if (yspeed < 0) {
				yspeed = 0;
				yspeed += WEIGHT;
			}
		}
	}

	private void jump() {
		yspeed = -MAX_Y_SPEED;
	}

	private void updateXSpeed() {
		if (canAttack()) {
			xspeed = INITIAL_X_SPEED;
		} else if (isInSight(GameLogic.getPlayer())) {
			if (GameLogic.getPlayer().getHitbox().getCenterX() < hitbox.x && !moveToFalling(LEFT)) {
				xspeed = -BASE_X_SPEED;
			} else if (GameLogic.getPlayer().getHitbox().getCenterX() > hitbox.getMaxX() && !moveToFalling(RIGHT)) {
				xspeed = BASE_X_SPEED;
			} else {
				xspeed = INITIAL_X_SPEED;
			}
		} else {
			xspeed = INITIAL_X_SPEED;
		}
	}

	private boolean canAttack() {
		Rectangle2D.Double canAttackBox = new Rectangle2D.Double(hitbox.getCenterX() - ATTACK_RANGE,
				hitbox.getCenterY() - ATTACK_RANGE, 2 * ATTACK_RANGE, 2 * ATTACK_RANGE);
		if (canAttackBox.intersects(GameLogic.getPlayer().getHitbox()) && Helper.IsEntityOnFloor(hitbox)) {
			return Bullet.canBulletHit(GameLogic.getPlayer().getHitbox(), hitbox, canAttackBox);
		}
		return false;
	}
}
