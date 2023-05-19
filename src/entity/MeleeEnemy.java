package entity;

import static utils.Constants.AttackState.READY;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.MeleeConstants.BASE_X_SPEED;
import static utils.Constants.EnemyConstants.MeleeConstants.HEIGHT;
import static utils.Constants.EnemyConstants.MeleeConstants.INITIAL_MAX_HEALTH;
import static utils.Constants.EnemyConstants.MeleeConstants.INITIAL_X_SPEED;
import static utils.Constants.EnemyConstants.MeleeConstants.INITIAL_Y_SPEED;
import static utils.Constants.EnemyConstants.MeleeConstants.MAX_Y_SPEED;
import static utils.Constants.EnemyConstants.MeleeConstants.SIGHT_SIZE;
import static utils.Constants.EnemyConstants.MeleeConstants.WEIGHT;
import static utils.Constants.EnemyConstants.MeleeConstants.WIDTH;
import static utils.Constants.EnemyConstants.MeleeConstants.Animations.ANIMATION_SPEED;
import static utils.Constants.EnemyConstants.MeleeConstants.Animations.ANIMATION_STATE_COUNT;
import static utils.Constants.EnemyConstants.MeleeConstants.Animations.IDLE;
import static utils.Constants.EnemyConstants.MeleeConstants.Animations.IDLE_FRAMES_COUNT;
import static utils.Constants.EnemyConstants.MeleeConstants.Animations.SPRITE_SIZE;

import java.awt.geom.Rectangle2D;

import entity.base.Enemy;
import item.derived.Spear;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import logic.GameLogic;
import utils.Helper;
import utils.Loader;

public class MeleeEnemy extends Enemy {

	private double xspeed;
	private double yspeed;
	private int attackState;
	private Spear spear;
	private int animationFrame;
	private int animationState;
	private int frameCount;
	private Image[] animation;
	private boolean isFacingLeft;

	public MeleeEnemy(double x, double y) {
		super(x, y, WIDTH, HEIGHT, SIGHT_SIZE, INITIAL_MAX_HEALTH);
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		loadResources();
		attackState = READY;
		initHitbox(x, y, width, height);
		spear = new Spear();
	}

	private void loadResources() {
		isFacingLeft = false;
		animation = new Image[ANIMATION_STATE_COUNT + 1];
		animationFrame = 0;
		frameCount = 0;
		animationState = IDLE;
		animation[0] = Loader.GetSpriteAtlas(Loader.MELEE_IDLE_ATLAS);
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

		// draw HP
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.BOTTOM);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(currentHealth), hitbox.x, hitbox.y);
	}

	private void jump() {
		yspeed = -MAX_Y_SPEED;
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

	private boolean canAttack() {
		Rectangle2D.Double canAttackBox = new Rectangle2D.Double(hitbox.x - spear.getAttackRange(), hitbox.y,
				WIDTH + 2 * spear.getAttackRange(), HEIGHT);
		return canAttackBox.intersects(GameLogic.getPlayer().getHitbox()) && Helper.IsEntityOnFloor(hitbox);
	}

	private void updateXSpeed() {
		if (isInSight(GameLogic.getPlayer())) {
			if (GameLogic.getPlayer().getHitbox().getMaxX() + spear.getAttackRange() / 2 < hitbox.x
					&& !moveToFalling(LEFT)) {
				xspeed = -BASE_X_SPEED;
			} else if (GameLogic.getPlayer().getHitbox().x > hitbox.getMaxX() + spear.getAttackRange() / 2
					&& !moveToFalling(RIGHT)) {
				xspeed = BASE_X_SPEED;
			} else {
				xspeed = INITIAL_X_SPEED;
			}
		} else {
			xspeed = INITIAL_X_SPEED;
		}
	}

	@Override
	public void update() {
		if (GameLogic.getPlayer() != null) {
			updateXSpeed();
		}
		yspeed = Math.max(-MAX_Y_SPEED, Math.min(yspeed, MAX_Y_SPEED));
		move();

		if (attackState != READY) {
			attackState = spear.updateAttack(this);
		}
		if (xspeed > 0) {
			isFacingLeft = false;
		} else if (xspeed < 0) {
			isFacingLeft = true;
		}
		if (GameLogic.getPlayer() != null && canAttack() && attackState == READY) {
			attackState = spear.attack(GameLogic.getPlayer().getHitbox().getCenterX(),
					GameLogic.getPlayer().getHitbox().getCenterY(), this);
			if (GameLogic.getPlayer().getHitbox().getMaxX() < hitbox.getMinX()) {
				isFacingLeft = true;
			} else if (GameLogic.getPlayer().getHitbox().getMinX() > hitbox.getMaxX()) {
				isFacingLeft = false;
			}
		}

		if (currentHealth <= 0) {
			isDestroy = true;
			if (attackState != READY)
				spear.cancelAttack();
		}
	}

}
