package entity;

import java.awt.geom.Rectangle2D;

import entity.base.Enemy;
import entity.base.Entity;
import interfaces.Damageable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;
import logic.GameLogic;
import utils.Constants.Weapon.BulletConstants;
import utils.Helper;
import utils.Loader;

/**
 * Bullet
 * Represents a bullet in the game
 */
public class Bullet extends Entity {
	/**
	 * The x-axis speed of the bullet
	 */
	private double xspeed;
	/**
	 * The y-axis speed of the bullet
	 */
	private double yspeed;
	/**
	 * The shooter of the bullet
	 * @see entity.base.Entity
	 */
	private Entity owner;
	/**
	 * The image of the bullet
	 * @see javafx.scene.image.Image
	 */
	private Image image;

	/**
	 * Constructor
	 * @param x x coordinate of the bullet
	 * @param y y coordinate of the bullet
	 * @param targetX x coordinate of the target
	 * @param targetY y coordinate of the target
	 * @param owner the owner of the bullet
	 */
	public Bullet(double x, double y, double targetX, double targetY, Entity owner) {
		super(x, y, BulletConstants.WIDTH, BulletConstants.HEIGHT);
		initHitbox(x, y, width, height);
		loadResources();
		this.owner = owner;
		calculateSpeed(x, y, targetX, targetY);
		GameLogic.addNewObject(this);
	}

	/**
	 * Load resources
	 * Load the image of the bullet
	 * @see utils.Loader
	 */
	private void loadResources() {
		image = Loader.GetSpriteAtlas(Loader.BULLET_ATLAS);
	}

	/**
	 * Draw the bullet
	 * @param gc GraphicsContext
	 * @param screen the screen
	 */
	@Override
	public void draw(GraphicsContext gc, Rectangle2D.Double screen) {
		if (!hitbox.intersects(screen))
			return;
		double angle = Math.toDegrees(Math.atan((y - hitbox.y) / (x - hitbox.x)));
		Rotate rotate = new Rotate(angle, hitbox.x, hitbox.y);

		double drawX = hitbox.x + ((hitbox.x - x) < 0 ? 0 : width);
		double drawY = hitbox.y;
		double drawW = width * ((hitbox.x - x) < 0 ? 1 : -1);
		double drawH = height;

		gc.save();
		gc.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(), rotate.getTx(),
				rotate.getTy());
		gc.drawImage(image, drawX, drawY, drawW, drawH);
		gc.restore();
	}

	/**
	 * Get the z coordinate of the bullet
	 * @return 0
	 * @see sharedObject.Renderable
	 */
	@Override
	public int getZ() {
		return 0;
	}

	/**
	 * Update the bullet
	 * Move the bullet and check if it hits anything
	 * @see entity.base.Entity
	 */
	@Override
	public void update() {
		move();
		checkHit();
	}

	/**
	 * Move the bullet
	 * If the bullet can't move, destroy it
	 * @see utils.Helper#CanMoveHere(double, double, double, double)
	 */
	private void move() {
		if (Helper.CanMoveHere(hitbox.x + xspeed, hitbox.y + yspeed, hitbox.width, hitbox.height)) {
			hitbox.x += xspeed;
			hitbox.y += yspeed;
		} else {
			isDestroy = true;
		}
	}

	/**
	 * Check if the bullet hits anything
	 * If it hits something, deal damage to the target
	 * @see interfaces.Damageable
	 */
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

	/**
	 * Calculate the speed of the bullet
	 * @param x x coordinate of the bullet
	 * @param y y coordinate of the bullet
	 * @param targetX x coordinate of the target
	 * @param targetY y coordinate of the target
	 */
	private void calculateSpeed(double x, double y, double targetX, double targetY) {
		double dx = targetX - x;
		double dy = targetY - y;
		double vectorSize = Math.sqrt(dx * dx + dy * dy);
		xspeed = BulletConstants.SPEED * dx / vectorSize;
		yspeed = BulletConstants.SPEED * dy / vectorSize;
	}

	/**
	 * Check if the bullet can hit the target
	 * @param targetHitbox the hitbox of the target
	 * @param sourceHitbox the hitbox of the source
	 * @param attackBox the attack box of the source
	 * @return true if the bullet can hit the target, false otherwise
	 */
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
