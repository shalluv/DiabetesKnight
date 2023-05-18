package item.derived;

import static utils.Constants.AttackState.*;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.PlayerConstants.ATTACK_BOX_HEIGHT;
import static utils.Constants.PlayerConstants.MELEE_ATTACK_DELAY;
import static utils.Constants.PlayerConstants.MELEE_ATTACK_RANGE;
import static utils.Constants.PlayerConstants.MELEE_ATTACK_SPEED;
import static utils.Constants.PlayerConstants.MELEE_DAMAGE;

import java.awt.geom.Rectangle2D;

import entity.Player;
import entity.base.Enemy;
import entity.base.Entity;
import input.InputUtility;
import interfaces.Damageable;
import item.Weapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import logic.GameLogic;
import utils.Helper;

public class Spear extends Weapon {

	private int attackDirection;
	private int attackProgress;
	private Rectangle2D.Double attackBox;

	public Spear() {
		super("Spear", Color.RED);
	}

	@Override
	public void draw(GraphicsContext gc, Entity attacker) {
		Rectangle2D.Double hitbox = attacker.getHitbox();
		if (attackState != READY && attackProgress > 0) {
			switch (attackDirection) {
			case LEFT:
				gc.fillRect(hitbox.x - attackProgress, hitbox.getCenterY() - ATTACK_BOX_HEIGHT / 2,
						attackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
				break;
			case RIGHT:
				gc.fillRect(hitbox.getCenterX(), hitbox.getCenterY() - ATTACK_BOX_HEIGHT / 2,
						attackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public int updateAttack(Entity attacker) {
		if (attackState == MELEE_IN_PROGRESS) {
			checkAttackHit(attacker);
		}
		if (cooldown != null && cooldown.isAlive()) // cooldown then don't update progress
			return attackState;
		if (attackState == MELEE_IN_PROGRESS || attackState == MELEE_HIT) {
			if (attackProgress < MELEE_ATTACK_RANGE) {
				if (cooldown == null || !cooldown.isAlive()) // if it is not cooldown init cooldown thread
					initCooldown(MELEE_ATTACK_DELAY);
				attackProgress += MELEE_ATTACK_SPEED;
			}
			updateAttackBox(attacker);
			if (isAttackingWall(attacker)) {
				attackProgress -= MELEE_ATTACK_SPEED;
				attackState = MELEE_ON_COOLDOWN;
			}
			cooldown.start();
			if (attackProgress >= MELEE_ATTACK_RANGE) // if it is out of range then stop the attack
				attackState = MELEE_ON_COOLDOWN;
		} else if (attackState == MELEE_ON_COOLDOWN) {
			if (attackProgress > 0) {
				if (cooldown == null || !cooldown.isAlive())
					initCooldown(MELEE_ATTACK_DELAY);
				attackProgress -= MELEE_ATTACK_SPEED;
				cooldown.start();
			} else {
				cooldown = null;
				attackProgress = 0;
				attackState = READY;
				attackBox = null;
			}
		}
		return attackState;
	}

	@Override
	public int attack(double targetX, double targetY, Entity attacker) {
		updateAttackDirection(attacker);
		this.attackState = MELEE_IN_PROGRESS;
		return attackState;
	}

	private void updateAttackDirection(Entity attacker) {
		if (InputUtility.getMouseX() >= attacker.getHitbox().getCenterX())
			attackDirection = RIGHT;
		else
			attackDirection = LEFT;
	}

	private void checkAttackHit(Entity attacker) {
		if (attackBox == null)
			return;
		for (Entity entity : GameLogic.getGameObjectContainer()) {
			if (!entity.isDestroyed() && entity instanceof Damageable && isEnemy(entity, attacker)) {
				if (attackBox.intersects(entity.getHitbox())) {
					((Damageable) entity).receiveDamage(MELEE_DAMAGE);
					attackState = MELEE_HIT;
					return;
				}
			}
		}
	}

	private boolean isEnemy(Entity entity, Entity attacker) {
		if ((entity instanceof Enemy && attacker instanceof Player)
				|| (entity instanceof Player && attacker instanceof Enemy))
			return true;
		return false;
	}

	private void updateAttackBox(Entity attacker) {
		Rectangle2D.Double hitbox = attacker.getHitbox();
		switch (attackDirection) {
		case LEFT:
			attackBox = new Rectangle2D.Double(hitbox.x - attackProgress, hitbox.getCenterY() - ATTACK_BOX_HEIGHT / 2,
					attackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
			break;
		case RIGHT:
			attackBox = new Rectangle2D.Double(hitbox.getCenterX(), hitbox.getCenterY() - ATTACK_BOX_HEIGHT / 2,
					attackProgress + hitbox.width / 2, ATTACK_BOX_HEIGHT);
			break;
		default:
			break;
		}
	}

	private boolean isAttackingWall(Entity attacker) {
		Rectangle2D.Double hitbox = attacker.getHitbox();
		if (attackDirection == LEFT
				&& !Helper.CanMoveHere(attackBox.x - attackProgress, attackBox.y, attackBox.width, attackBox.height))
			return true;
		if (attackDirection == RIGHT && !Helper.CanMoveHere(attackBox.x + hitbox.width / 2, attackBox.y,
				attackBox.width + attackProgress - hitbox.width / 2, attackBox.height))
			return true;
		return false;
	}

}
