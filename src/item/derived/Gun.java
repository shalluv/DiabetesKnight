package item.derived;

import static utils.Constants.AttackState.*;
import static utils.Constants.GunConstants.ATTACK_DELAY;

import java.awt.geom.Rectangle2D;

import entity.Bullet;
import entity.base.Enemy;
import entity.base.Entity;
import item.Weapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utils.Constants.BulletConstants;

public class Gun extends Weapon {

	private double targetX;
	private double targetY;

	public Gun() {
		super("Gun", Color.BLUE);
	}

	@Override
	public void draw(GraphicsContext gc, Entity attacker) {
		return;
	}

	@Override
	public int updateAttack(Entity attacker) {
		Rectangle2D.Double hitbox = attacker.getHitbox();
		if (attackState == RANGED_IN_PROGRESS) {
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
			attackState = RANGED_ON_COOLDOWN;
		} else if (attackState == RANGED_ON_COOLDOWN && !cooldown.isAlive()) {
			cooldown = null;
			attackState = READY;
		}
		return attackState;
	}

	@Override
	public int attack(double targetX, double targetY, Entity attacker) {
		this.targetX = targetX;
		this.targetY = targetY;
		this.attackState = RANGED_IN_PROGRESS;
		return attackState;
	}

}
