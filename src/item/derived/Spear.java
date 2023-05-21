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

/**
 * Spear
 * Represents a spear in the game
 * A spear is a melee weapon
 * @see item.MeleeWeapon
 */
public class Spear extends MeleeWeapon {

	/**
	 * The attack progress of the spear
	 */
	private int attackProgress;
	/**
	 * The attack speed of the spear
	 */
	private int attackSpeed;

	/**
	 * Constructor
	 */
	public Spear() {
		super("Fork", Loader.GetSpriteAtlas(Loader.SPEAR_ATLAS), BASE_ATTACK_RANGE, BASE_DAMAGE,
				BASE_X_SPEED_MULTIPLIER, BASE_Y_SPEED_MULTIPLIER, true);
		this.attackRange = BASE_ATTACK_RANGE;
		this.attackSpeed = BASE_ATTACK_SPEED;
	}

	/**
	 * Draw the spear
	 * @param gc The graphics context
	 * @param x The x position
	 * @param y The y position
	 * @param width The width
	 * @param height The height
	 * @param isFacingLeft Whether the entity who is holding the spear is facing left
	 */
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

	/**
	 * Updates the attack box of the spear
	 * @param attacker The entity who is holding the spear
	 */
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

	/**
	 * Updates the attack progress of the spear
	 * @param attacker The entity who is holding the spear
	 * @return The attack state
	 */
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

	/**
	 * Updates the attack progress when the spear attacking state is in progress
	 * @param attacker The entity who is holding the spear
	 */
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

	/**
	 * Updates the attack progress when the spear attacking state is on cooldown
	 */
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

	/**
	 * Use the ultimate of the spear
	 */
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

	/**
	 * Resets the status of the spear
	 */
	@Override
	public void resetStatus() {
		isOnUltimate = false;
		XSpeedMultiplier = BASE_X_SPEED_MULTIPLIER;
		YSpeedMultiplier = BASE_Y_SPEED_MULTIPLIER;
		damage = BASE_DAMAGE;
		attackRange = BASE_ATTACK_RANGE;
		attackSpeed = BASE_ATTACK_SPEED;
	}

}
