package item.derived;

import static utils.Constants.AttackState.*;
import static utils.Constants.PlayerConstants.Animations.WEAPON_OFFSET_X;
import static utils.Constants.PlayerConstants.Animations.WEAPON_OFFSET_Y;
import static utils.Constants.Weapon.GunConstants.*;

import entity.base.Entity;
import interfaces.Reloadable;
import item.RangedWeapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Loader;

public class Gun extends RangedWeapon implements Reloadable {

	private int currentAmmo;
	private int maxAmmo;
	private Image image;

	public Gun() {
		super("Gun", Loader.GetSpriteAtlas(Loader.GUN_ATLAS));
		image = Loader.GetSpriteAtlas(Loader.GUN_ATLAS);
		this.currentAmmo = MAX_AMMO;
		this.maxAmmo = MAX_AMMO;
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
			initCooldown(ATTACK_DELAY);
			cooldown.start();
			attackState = ON_COOLDOWN;
		} else if (attackState == ON_COOLDOWN && !cooldown.isAlive()) {
			onCooldownUpdate();
			if (currentAmmo == 0) {
				initCooldown(RELOAD_DELAY);
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
			initCooldown(RELOAD_DELAY);
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
		initCooldown(RELOAD_DELAY);
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
}
