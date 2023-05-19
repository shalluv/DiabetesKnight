package item.derived;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.AttackState.ON_COOLDOWN;
import static utils.Constants.AttackState.READY;
import static utils.Constants.PlayerConstants.Animations.WEAPON_OFFSET_X;
import static utils.Constants.PlayerConstants.Animations.WEAPON_OFFSET_Y;
import static utils.Constants.Weapon.GunConstants.ATTACK_DELAY;

import java.awt.geom.Rectangle2D;

import entity.Bullet;
import entity.base.Enemy;
import entity.base.Entity;
import item.Weapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Constants.Weapon.BulletConstants;
import utils.Loader;

public class Gun extends Weapon {

	private double targetX;
	private double targetY;
	private Image image;

	public Gun() {
		super("Gun", Loader.GetSpriteAtlas(Loader.GUN_ATLAS));
		image = Loader.GetSpriteAtlas(Loader.GUN_ATLAS);
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
		Rectangle2D.Double hitbox = attacker.getHitbox();
		if (attackState == IN_PROGRESS) {
			double bulletX = hitbox.getCenterX() - BulletConstants.WIDTH / 2;
			double bulletY = hitbox.getCenterY() - BulletConstants.HEIGHT / 2;
			if (attacker instanceof Enemy) {
				if (targetX > bulletX)
					bulletX = hitbox.getMaxX();
				else if (targetX < bulletX)
					bulletX = hitbox.x;
			}
			new Bullet(bulletX, bulletY, targetX, targetY, attacker);
			initCooldown(ATTACK_DELAY);
			cooldown.start();
			attackState = ON_COOLDOWN;
		} else if (attackState == ON_COOLDOWN && !cooldown.isAlive()) {
			cooldown = null;
			attackState = READY;
		}
		return attackState;
	}

	@Override
	public int attack(double targetX, double targetY, Entity attacker) {
		this.targetX = targetX;
		this.targetY = targetY;
		this.attackState = IN_PROGRESS;
		return attackState;
	}

}
