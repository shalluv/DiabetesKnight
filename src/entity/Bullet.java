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

	public Bullet(double x, double y, double speed) {
		super(x, y, BulletConstants.WIDTH, BulletConstants.HEIGHT);
		initHitbox(x, y, width, height);
		this.xspeed = speed;
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

	public void move() {
		if (Helper.CanMoveHere(hitbox.x + xspeed, hitbox.y, hitbox.width, hitbox.height)) {
			hitbox.x += xspeed;
		} else {
			isDestroy = true;
		}
	}

	public void checkHit() {
		for (Entity entity : Main.gameLogic.getGameObjectContainer()) {
			if (entity instanceof Damageable && hitbox.intersects(entity.getHitbox())) {
				((Damageable) entity).receiveDamage(BulletConstants.DAMAGE);
				isDestroy = true;
				break;
			}
		}
	}

}
