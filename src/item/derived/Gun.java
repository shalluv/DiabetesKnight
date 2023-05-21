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

public class Gun extends RangedWeapon implements Reloadable {

	private int currentAmmo;
	private int maxAmmo;
	private int attackDelay;
	private int reloadDelay;
	private Image image;

	public Gun() {
		super("Gun", Loader.GetSpriteAtlas(Loader.GUN_ATLAS), BASE_X_SPEED_MULTIPLIER, BASE_Y_SPEED_MULTIPLIER);
		image = Loader.GetSpriteAtlas(Loader.GUN_ATLAS);
		this.currentAmmo = BASE_MAX_AMMO;
		this.maxAmmo = BASE_MAX_AMMO;
		this.attackDelay = BASE_ATTACK_DELAY;
		this.reloadDelay = BASE_RELOAD_DELAY;
	}

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

	private void setAmmo(int value) {
		value = Math.max(0, Math.min(value, maxAmmo));
		currentAmmo = value;
	}

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

	@Override
	public int getAmmo() {
		return currentAmmo;
	}

	@Override
	public void cancelReload() {
		if (cooldown != null)
			cooldown.interrupt();
		if (attackState == ON_RELOAD)
			attackState = READY;
	}

	@Override
	public int getMaxAmmo() {
		return maxAmmo;
	}

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
