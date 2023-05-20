package item;

import static utils.Constants.AttackState.READY;

import java.awt.geom.Rectangle2D;

import entity.Bullet;
import entity.base.Enemy;
import entity.base.Entity;
import javafx.scene.image.Image;
import utils.Constants.Weapon.BulletConstants;

public abstract class RangedWeapon extends Weapon {

	protected double targetX;
	protected double targetY;
	protected Image image;

	public RangedWeapon(String name, Image image, double XSpeedMultiplier, double YSpeedMultiplier) {
		super(name, image, XSpeedMultiplier, YSpeedMultiplier);
	}

	protected void inProgressUpdate(Entity attacker) {
		Rectangle2D.Double hitbox = attacker.getHitbox();
		double bulletX = hitbox.getCenterX() - BulletConstants.WIDTH / 2;
		double bulletY = hitbox.getCenterY() - BulletConstants.HEIGHT / 2;
		if (attacker instanceof Enemy) {
			if (targetX > bulletX)
				bulletX = hitbox.getMaxX();
			else if (targetX < bulletX)
				bulletX = hitbox.x;
		}
		new Bullet(bulletX, bulletY, targetX, targetY, attacker);
	}

	protected void onCooldownUpdate() {
		cooldown = null;
		attackState = READY;
	}
}
