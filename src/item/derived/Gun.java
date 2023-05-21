package item.derived;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.AttackState.ON_COOLDOWN;
import static utils.Constants.AttackState.ON_RELOAD;
import static utils.Constants.AttackState.READY;
import static utils.Constants.PlayerConstants.Animations.WEAPON_OFFSET_X;
import static utils.Constants.PlayerConstants.Animations.WEAPON_OFFSET_Y;
import static utils.Constants.Weapon.GunConstants.BASE_ATTACK_DELAY;
import static utils.Constants.Weapon.GunConstants.BASE_MAX_AMMO;
import static utils.Constants.Weapon.GunConstants.BASE_RELOAD_DELAY;
import static utils.Constants.Weapon.GunConstants.BASE_X_SPEED_MULTIPLIER;
import static utils.Constants.Weapon.GunConstants.BASE_Y_SPEED_MULTIPLIER;
import static utils.Constants.Weapon.GunConstants.ULTIMATE_ATTACK_DELAY;
import static utils.Constants.Weapon.GunConstants.ULTIMATE_COST;
import static utils.Constants.Weapon.GunConstants.ULTIMATE_DURATION;
import static utils.Constants.Weapon.GunConstants.ULTIMATE_MAX_AMMO;
import static utils.Constants.Weapon.GunConstants.ULTIMATE_RELOAD_DELAY;

import entity.base.Entity;
import interfaces.Reloadable;
import item.RangedWeapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import logic.GameLogic;
import utils.Loader;

/**
 * Gun
 * Represents a gun in the game
 * A gun is a ranged weapon
 * @see item.RangedWeapon
 * @see interfaces.Reloadable
 * @see entity.Bullet
 */
public class Gun extends RangedWeapon implements Reloadable {

	/**
	 * The current ammo of the gun
	 */
	private int currentAmmo;
	/**
	 * The maximum ammo of the gun
	 */
	private int maxAmmo;
	/**
	 * The attack delay of the gun
	 */
	private int attackDelay;
	/**
	 * The reload delay of the gun
	 */
	private int reloadDelay;
	/**
	 * The image of the gun
	 * @see javafx.scene.image.Image
	 */
	private Image image;

	/**
	 * Constructor
	 */
	public Gun() {
		super("Ice cream gun", Loader.GetSpriteAtlas(Loader.GUN_ATLAS), BASE_X_SPEED_MULTIPLIER,
				BASE_Y_SPEED_MULTIPLIER);
		image = Loader.GetSpriteAtlas(Loader.GUN_ATLAS);
		this.currentAmmo = BASE_MAX_AMMO;
		this.maxAmmo = BASE_MAX_AMMO;
		this.attackDelay = BASE_ATTACK_DELAY;
		this.reloadDelay = BASE_RELOAD_DELAY;
	}

	/**
	 * Draws the gun
	 * @param gc The graphics context to draw on
	 * @param x The x coordinate of the gun
	 * @param y The y coordinate of the gun
	 * @param width The width of the gun
	 * @param height The height of the gun
	 * @param isFacingLeft Whether the gun is facing left
	 */
	@Override
	public void draw(GraphicsContext gc, double x, double y, double width, double height, boolean isFacingLeft) {
		if (isFacingLeft) {
			width = -width;
			x += WEAPON_OFFSET_X;
		} else {
			x += WEAPON_OFFSET_X;
		}
		gc.drawImage(image, x, y + WEAPON_OFFSET_Y, width, height);
	}

	/**
	 * Updates the gun state
	 * @param attacker The entity that is attacking
	 * @return The attack state
	 */
	@Override
	public int updateAttack(Entity attacker) {
		if (attackState == IN_PROGRESS) {
			inProgressUpdate(attacker);
			setAmmo(currentAmmo - 1);
			Loader.playSound(Loader.SHOOT_SOUND_ATLAS);
			initCooldown(attackDelay);
			cooldown.start();
			attackState = ON_COOLDOWN;
		} else if (attackState == ON_COOLDOWN && !cooldown.isAlive()) {
			onCooldownUpdate();
			if (currentAmmo == 0) {
				initCooldown(reloadDelay);
				cooldown.start();
				attackState = ON_RELOAD;
			}
		} else if (attackState == ON_RELOAD && !cooldown.isAlive()) {
			reload();
		}
		return attackState;
	}

	/**
	 * Attacks
	 * @param targetX The x coordinate of the target
	 * @param targetY The y coordinate of the target
	 * @param attacker The entity that is attacking
	 * @return The attack state
	 */
	@Override
	public int attack(double targetX, double targetY, Entity attacker) {
		if (currentAmmo == 0) {
			initCooldown(reloadDelay);
			cooldown.start();
			attackState = ON_RELOAD;
			return ON_RELOAD;
		}
		this.targetX = targetX;
		this.targetY = targetY;
		this.attackState = IN_PROGRESS;
		return attackState;
	}

	/**
	 * Sets the ammo
	 * @param value The value to set the ammo to, must be between 0 and maxAmmo
	 */
	private void setAmmo(int value) {
		value = Math.max(0, Math.min(value, maxAmmo));
		currentAmmo = value;
	}

	/**
	 * Reloads the gun
	 */
	@Override
	public void reload() {
		setAmmo(currentAmmo + 1);
		attackState = ON_RELOAD;
		if (currentAmmo == maxAmmo) {
			attackState = READY;
			return;
		}
		initCooldown(reloadDelay);
		cooldown.start();
	}

	/**
	 * Gets the ammo
	 * @return currentAmmo
	 */
	@Override
	public int getAmmo() {
		return currentAmmo;
	}

	/**
	 * Cancels the reload
	 */
	@Override
	public void cancelReload() {
		if (cooldown != null)
			cooldown.interrupt();
		if (attackState == ON_RELOAD)
			attackState = READY;
	}

	/**
	 * Gets the max ammo
	 * @return maxAmmo
	 */
	@Override
	public int getMaxAmmo() {
		return maxAmmo;
	}

	/**
	 * Uses the ultimate
	 */
	@Override
	public void useUlitmate() {
		if (isOnUltimate || attackState == ON_RELOAD)
			return;
		int currentPower = GameLogic.getPlayer().getCurrentPower();
		if (currentPower >= ULTIMATE_COST) {
			GameLogic.getPlayer().setCurrentPower(currentPower - ULTIMATE_COST);
			reloadDelay = ULTIMATE_RELOAD_DELAY;
			attackDelay = ULTIMATE_ATTACK_DELAY;
			currentAmmo = ULTIMATE_MAX_AMMO;
			maxAmmo = ULTIMATE_MAX_AMMO;
			isOnUltimate = true;
			initOnUltimate(ULTIMATE_DURATION);
			onUltimate.start();
		}
	}

	/**
	 * Resets the status
	 */
	@Override
	public void resetStatus() {
		isOnUltimate = false;
		cancelReload();
		currentAmmo = BASE_MAX_AMMO;
		maxAmmo = BASE_MAX_AMMO;
		reloadDelay = BASE_RELOAD_DELAY;
		attackDelay = BASE_ATTACK_DELAY;
	}
}
