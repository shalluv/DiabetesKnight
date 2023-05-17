package entity;

import application.Main;
import entity.base.Entity;
import interfaces.Damageable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
		Main.gameLogic.addNewObject(this);
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
		for (Entity entity : Main.gameLogic.getGameObjectContainer()) {
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

}
