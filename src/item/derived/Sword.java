package item.derived;

import static utils.Constants.Weapon.SwordConstants.*;
import static utils.Constants.Weapon.SwordConstants.Animations.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.AttackState.ON_COOLDOWN;
import static utils.Constants.AttackState.READY;
import static utils.Constants.Directions.*;
import static utils.Constants.SCALE;

import entity.base.Entity;
import item.MeleeWeapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import logic.GameLogic;
import utils.Loader;

public class Sword extends MeleeWeapon {

	private boolean startedCooldown;
	private Image image;
	private int animationFrame;

	public Sword() {
		super("sword", getIdleImage(), BASE_ATTACK_RANGE, BASE_DAMAGE, BASE_X_SPEED_MULTIPLIER, BASE_Y_SPEED_MULTIPLIER,
				false);
		loadResources();
		this.attackProgress = 0;
		this.startedCooldown = false;
	}

	private void loadResources() {
		animationFrame = 0;
		image = Loader.GetSpriteAtlas(Loader.SWORD_ATLAS);
	}

	private static Image getIdleImage() {
		PixelReader reader = Loader.GetSpriteAtlas(Loader.SWORD_ATLAS).getPixelReader();
		Image idle = new WritableImage(reader, 0, 0, SPRITE_SIZE, SPRITE_SIZE);
		return idle;
	}

	@Override
	protected int updateProgress(Entity attacker) {
		if (cooldown != null && cooldown.isAlive()) // cooldown then don't update progress
			return attackState;
		if (attackState == IN_PROGRESS) {
			inProgressUpdate(attacker);
		} else if (attackState == ON_COOLDOWN) {
			onCooldownUpdate();
		}
		return attackState;
	}

	@Override
	protected void updateAttackBox(Entity attacker) {
		Rectangle2D.Double hitbox = attacker.getHitbox();
		Rectangle2D.Double baseAttackBox = new Rectangle2D.Double(hitbox.getCenterX() - ATTACK_BOX_HEIGHT / 2,
				hitbox.getCenterY() - attackRange - hitbox.height / 2, ATTACK_BOX_HEIGHT,
				attackRange + hitbox.height / 2);
		double swingAngle = Math.toRadians(attackProgress);
		AffineTransform transform = new AffineTransform();
		transform.rotate(swingAngle, hitbox.getCenterX(), hitbox.getCenterY());
		attackBox = transform.createTransformedShape(baseAttackBox);
	}

	@Override
	public void draw(GraphicsContext gc, double x, double y, double width, double height, boolean isFacingLeft) {
		if (attackState != IN_PROGRESS)
			return;
		double scaledWidth = width * SCALE / 2;
		double scaledHeight = height * SCALE / 2;
		double centerX = x + scaledWidth;
		double centerY = y + scaledHeight;
		if (isFacingLeft)
			width = -width - attackRange;
		else
			width = width + attackRange;
		gc.drawImage(image, animationFrame * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, centerX - ATTACK_BOX_HEIGHT / 2,
				centerY - attackRange - scaledHeight / 2 - ANIMATION_OFFSET_Y, width, height + ANIMATION_HEIGHT_OFFSET);
		if (Math.abs(attackProgress) >= MAXIMUM_SWING / 5 && Math.abs(attackProgress) <= MAXIMUM_SWING / 2
				&& animationFrame != SWING_ANIMATION)
			animationFrame += 1;
		else if (Math.abs(attackProgress) > MAXIMUM_SWING / 2 && animationFrame != SWING_DONE)
			animationFrame += 1;
	}

	private void inProgressUpdate(Entity attacker) {
		if (Math.abs(attackProgress) < Math.abs(MAXIMUM_SWING)) {
			if (cooldown == null || !cooldown.isAlive()) // if it is not cooldown init cooldown thread
				initCooldown(ATTACK_DELAY);
			if (attackDirection == LEFT)
				attackProgress -= SWING_SPEED;
			else
				attackProgress += SWING_SPEED;
			updateAttackBox(attacker);
			if (isAttackingWall() || Math.abs(attackProgress) >= Math.abs(MAXIMUM_SWING))
				attackState = ON_COOLDOWN;
			else
				cooldown.start();
		}
	}

	private void onCooldownUpdate() {
		if (cooldown == null || !cooldown.isAlive() && !startedCooldown) {
			initCooldown(AFTER_ATTACK_DELAY);
			cooldown.start();
			startedCooldown = true;
		} else {
			cooldown = null;
			attackProgress = 0;
			attackState = READY;
			attackBox = null;
			startedCooldown = false;
			animationFrame = 0;
		}
	}

	@Override
	public void useUlitmate() {
		if (isOnUltimate)
			return;
		int currentPower = GameLogic.getPlayer().getCurrentPower();
		if (currentPower >= ULTIMATE_COST) {
			GameLogic.getPlayer().setCurrentPower(currentPower - ULTIMATE_COST);
			XSpeedMultiplier = ULTIMATE_X_SPEED_MULTIPLIER;
			YSpeedMultiplier = ULTIMATE_Y_SPEED_MULTIPLIER;
			damage = ULTIMATE_DAMAGE;
			attackRange = ULTIMATE_ATTACK_RANGE;
			canMultipleHit = true;
			isOnUltimate = true;
			initOnUltimate(ULTIMATE_DURATION);
			onUltimate.start();
		}
	}

	@Override
	public void resetStatus() {
		isOnUltimate = false;
		XSpeedMultiplier = BASE_X_SPEED_MULTIPLIER;
		YSpeedMultiplier = ULTIMATE_Y_SPEED_MULTIPLIER;
		attackRange = BASE_ATTACK_RANGE;
		canMultipleHit = false;
		damage = BASE_DAMAGE;
	}

}
