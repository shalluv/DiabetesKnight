package item.derived;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.AttackState.ON_COOLDOWN;
import static utils.Constants.AttackState.READY;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.PlayerConstants.Animations.WEAPON_OFFSET_X;
import static utils.Constants.PlayerConstants.Animations.WEAPON_OFFSET_Y;
import static utils.Constants.Weapon.SpearConstants.ATTACK_BOX_HEIGHT;
import static utils.Constants.Weapon.SpearConstants.ATTACK_DELAY;
import static utils.Constants.Weapon.SpearConstants.BASE_ATTACK_RANGE;
import static utils.Constants.Weapon.SpearConstants.BASE_ATTACK_SPEED;
import static utils.Constants.Weapon.SpearConstants.BASE_DAMAGE;
import static utils.Constants.Weapon.SpearConstants.BASE_X_SPEED_MULTIPLIER;
import static utils.Constants.Weapon.SpearConstants.BASE_Y_SPEED_MULTIPLIER;
import static utils.Constants.Weapon.SpearConstants.ULTIMATE_ANIMATION_LEFT_X_OFFSET;
import static utils.Constants.Weapon.SpearConstants.ULTIMATE_ANIMATION_RIGHT_X_OFFSET;
import static utils.Constants.Weapon.SpearConstants.ULTIMATE_ATTACK_RANGE;
import static utils.Constants.Weapon.SpearConstants.ULTIMATE_ATTACK_SPEED;
import static utils.Constants.Weapon.SpearConstants.ULTIMATE_COST;
import static utils.Constants.Weapon.SpearConstants.ULTIMATE_DAMAGE;
import static utils.Constants.Weapon.SpearConstants.ULTIMATE_DURATION;
import static utils.Constants.Weapon.SpearConstants.ULTIMATE_X_SPEED_MULTIPLIER;
import static utils.Constants.Weapon.SpearConstants.ULTIMATE_Y_SPEED_MULTIPLIER;

import java.awt.geom.Rectangle2D;

import entity.base.Entity;
import item.MeleeWeapon;
import javafx.scene.canvas.GraphicsContext;
import logic.GameLogic;
import utils.Loader;

public class Spear extends MeleeWeapon {

	private int attackProgress;
	private int attackSpeed;

	public Spear() {
		super("Spear", Loader.GetSpriteAtlas(Loader.SPEAR_ATLAS), BASE_ATTACK_RANGE, BASE_DAMAGE,
				BASE_X_SPEED_MULTIPLIER, BASE_Y_SPEED_MULTIPLIER, true);
		this.attackRange = BASE_ATTACK_RANGE;
		this.attackSpeed = BASE_ATTACK_SPEED;
	}

	@Override
	public void draw(GraphicsContext gc, double x, double y, double width, double height, boolean isFacingLeft) {
		if (isFacingLeft) {
			width = -width;
			x -= attackProgress;
			x += WEAPON_OFFSET_X * 2;
			if (isOnUltimate) {
				x += ULTIMATE_ANIMATION_LEFT_X_OFFSET;
				width -= ULTIMATE_ANIMATION_LEFT_X_OFFSET;
			}
		} else {
			x += WEAPON_OFFSET_X;
			x += attackProgress;
			if (isOnUltimate) {
				x -= ULTIMATE_ANIMATION_RIGHT_X_OFFSET;
				width += ULTIMATE_ANIMATION_RIGHT_X_OFFSET;
			}
		}
		gc.drawImage(image, x, y + WEAPON_OFFSET_Y, width, height);
	}

	@Override
	protected void updateAttackBox(Entity attacker) {
		Rectangle2D.Double hitbox = attacker.getHitbox();
		switch (attackDirection) {
		case LEFT:
			attackBox = new Rectangle2D.Double(hitbox.x - attackProgress, hitbox.getCenterY() - ATTACK_BOX_HEIGHT / 2,
					attackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
			break;
		case RIGHT:
			attackBox = new Rectangle2D.Double(hitbox.getCenterX(), hitbox.getCenterY() - ATTACK_BOX_HEIGHT / 2,
					attackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
			break;
		default:
			break;
		}
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

	private void inProgressUpdate(Entity attacker) {
		if (attackProgress < attackRange) {
			if (cooldown == null || !cooldown.isAlive()) // if it is not cooldown init cooldown thread
				initCooldown(ATTACK_DELAY);
			attackProgress += attackSpeed;
		}
		updateAttackBox(attacker);
		if (isAttackingWall()) {
			attackProgress -= attackSpeed;
			attackState = ON_COOLDOWN;
		} else if (attackProgress >= attackRange) {// if it is out of range then stop the attack
			attackState = ON_COOLDOWN;
		} else
			cooldown.start();
	}

	private void onCooldownUpdate() {
		if (attackProgress > 0) {
			if (cooldown == null || !cooldown.isAlive())
				initCooldown(ATTACK_DELAY);
			attackProgress -= attackSpeed;
			cooldown.start();
		} else {
			cooldown = null;
			attackProgress = 0;
			attackState = READY;
			attackBox = null;
		}
	}

	@Override
	public void useUlitmate() {
		if (isOnUltimate)
			return;
		int currentPower = GameLogic.getPlayer().getCurrentPower();
		if (currentPower >= ULTIMATE_COST) {
			GameLogic.getPlayer().setCurrentPower(currentPower - ULTIMATE_COST);
			isOnUltimate = true;
			XSpeedMultiplier = ULTIMATE_X_SPEED_MULTIPLIER;
			YSpeedMultiplier = ULTIMATE_Y_SPEED_MULTIPLIER;
			damage = ULTIMATE_DAMAGE;
			attackRange = ULTIMATE_ATTACK_RANGE;
			attackSpeed = ULTIMATE_ATTACK_SPEED;
			initOnUltimate(ULTIMATE_DURATION);
			onUltimate.start();
		}
	}

	@Override
	public void resetStatus() {
		isOnUltimate = false;
		XSpeedMultiplier = BASE_X_SPEED_MULTIPLIER;
		YSpeedMultiplier = BASE_Y_SPEED_MULTIPLIER;
		damage = BASE_DAMAGE;
		attackRange = BASE_ATTACK_RANGE;
		attackSpeed = BASE_ATTACK_SPEED;
	}

	@Override
	protected void playAttackSound() {
		Loader.playSound(Loader.SPEAR_ATTACK_SOUND_ATLAS);
	}

}
