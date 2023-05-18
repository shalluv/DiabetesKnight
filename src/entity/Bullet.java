package entity;

import java.awt.geom.Rectangle2D;
import entity.base.Enemy;
import entity.base.Entity;
import interfaces.Damageable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import logic.GameLogic;
import utils.Constants.BulletConstants;
import utils.Helper;

public class Bullet extends Entity {

	private double xspeed;
	private double yspeed;
	private Entity owner;

	public Bullet(double x, double y, double targetX, double targetY, Entity owner) {
		super(x, y, BulletConstants.WIDTH, BulletConstants.HEIGHT);
		initHitbox(x, y, width, height);
		this.owner = owner;
		calculateSpeed(x, y, targetX, targetY);
		GameLogic.addNewObject(this);
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.GREEN);
		gc.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
	}

	@Override
	public int getZ() {
		return 0;
	}

	@Override
	public void update() {
		move();
		checkHit();
	}

	private void move() {
		if (Helper.CanMoveHere(hitbox.x + xspeed, hitbox.y + yspeed, hitbox.width, hitbox.height)) {
			hitbox.x += xspeed;
			hitbox.y += yspeed;
		} else {
			isDestroy = true;
		}
	}

	private void checkHit() {
		for (Entity entity : GameLogic.getGameObjectContainer()) {
			if (entity instanceof Damageable
					&& ((entity instanceof Enemy && owner instanceof Player)
							|| (entity instanceof Player && owner instanceof Enemy))
					&& hitbox.intersects(entity.getHitbox())) {
				((Damageable) entity).receiveDamage(BulletConstants.DAMAGE);
				isDestroy = true;
				break;
			}
		}
	}

	private void calculateSpeed(double x, double y, double targetX, double targetY) {
		double dx = targetX - x;
		double dy = targetY - y;
		double vectorSize = Math.sqrt(dx * dx + dy * dy);
		xspeed = BulletConstants.SPEED * dx / vectorSize;
		yspeed = BulletConstants.SPEED * dy / vectorSize;
	}

	public static boolean canBulletHit(Rectangle2D.Double targetHitbox, Rectangle2D.Double sourceHitbox,
			Rectangle2D.Double attackBox) {
		double sx = sourceHitbox.getCenterX() - BulletConstants.WIDTH / 2;
		double sy = sourceHitbox.getCenterY() - BulletConstants.HEIGHT / 2;
		if (targetHitbox.getCenterX() > sx)
			sx = sourceHitbox.getMaxX();
		else if (targetHitbox.getCenterX() < sx)
			sx = sourceHitbox.x;
		double dx = targetHitbox.getCenterX() - sx;
		double dy = targetHitbox.getCenterY() - sy;
		double vectorSize = Math.sqrt(dx * dx + dy * dy);
		double vx = BulletConstants.SPEED * dx / vectorSize;
		double vy = BulletConstants.SPEED * dy / vectorSize;
		Rectangle2D.Double bullet = new Rectangle2D.Double(sx, sy, BulletConstants.WIDTH, BulletConstants.HEIGHT);
		while (attackBox.contains(bullet)) {
			if (Helper.CanMoveHere(bullet.x + vx, bullet.y + vy, bullet.width, bullet.height)) {
				bullet.x += vx;
				bullet.y += vy;
				if (bullet.intersects(targetHitbox))
					return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
