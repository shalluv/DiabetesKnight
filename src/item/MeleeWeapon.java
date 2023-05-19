package item;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import entity.Player;
import entity.base.Enemy;
import entity.base.Entity;
import interfaces.Damageable;
import javafx.scene.image.Image;
import logic.GameLogic;
import utils.Helper;

public abstract class MeleeWeapon extends Weapon {
	protected int attackDirection;
	protected int attackProgress;
	protected int attackRange;
	protected int damage;
	protected Shape attackBox;
	protected boolean hit;
	protected boolean canMultipleHit;

	public MeleeWeapon(String name, Image image, int attackRange, int damage, double speedMultiplier,
			boolean canMultipleHit) {
		super(name, image, speedMultiplier);
		this.attackRange = attackRange;
		this.damage = damage;
		this.canMultipleHit = canMultipleHit;
	}

	protected abstract int updateProgress(Entity attacker);

	protected abstract void updateAttackBox(Entity attacker);

	public int getAttackRange() {
		return attackRange;
	}

	@Override
	public int updateAttack(Entity attacker) {
		if (!hit)
			checkAttackHit(attacker);
		if (cooldown != null && cooldown.isAlive()) // cooldown then don't update progress
			return attackState;
		return updateProgress(attacker);
	}

	@Override
	public int attack(double targetX, double targetY, Entity attacker) {
		this.hit = false;
		this.attackState = IN_PROGRESS;
		updateAttackDirection(targetX, attacker);
		updateAttackBox(attacker);
		return attackState;
	}

	protected boolean isEnemy(Entity entity, Entity attacker) {
		if ((entity instanceof Enemy && attacker instanceof Player)
				|| (entity instanceof Player && attacker instanceof Enemy))
			return true;
		return false;
	}

	protected boolean isAttackingWall() {
		Rectangle2D.Double rectangleAttackBox = (Double) attackBox.getBounds2D();
		if (attackDirection == LEFT
				&& !Helper.CanMoveHere(rectangleAttackBox.getMinX(), rectangleAttackBox.getMaxY(), 1, 1))
			return true;
		if (attackDirection == RIGHT
				&& !Helper.CanMoveHere(rectangleAttackBox.getMaxX(), rectangleAttackBox.getMaxY(), 1, 1))
			return true;
		return false;
	}

	protected void checkAttackHit(Entity attacker) {
		if (attackBox == null)
			return;
		if (attacker instanceof Damageable && ((Damageable) attacker).getHealth() < 0)
			return;
		for (Entity entity : GameLogic.getGameObjectContainer()) {
			if (!entity.isDestroyed() && entity instanceof Damageable && isEnemy(entity, attacker)) {
				if (attackBox.intersects(entity.getHitbox())) {
					((Damageable) entity).receiveDamage(damage);
					if (!canMultipleHit) {
						hit = true;
						return;
					}
				}
			}
		}
	}

	protected void updateAttackDirection(double targetX, Entity attacker) {
		if (targetX >= attacker.getHitbox().getCenterX())
			attackDirection = RIGHT;
		else
			attackDirection = LEFT;
	}

}
