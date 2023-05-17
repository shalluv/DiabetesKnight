package entity.base;

import static utils.Constants.EnemyConstants.HEIGHT;
import static utils.Constants.EnemyConstants.SIGHT_SIZE;
import static utils.Constants.EnemyConstants.WIDTH;
import static utils.Constants.PlayerConstants.MELEE_ATTACK_RANGE;

import java.awt.geom.Rectangle2D;

import entity.Player;
import interfaces.Damageable;
import item.Item;
import utils.Helper;

public abstract class Enemy extends Entity implements Damageable {

	protected int maxHealth;
	protected int currentHealth;
	protected Thread attackCooldown;

	public Enemy(double x, double y, int width, int height) {
		super(x, y, width, height);
		this.maxHealth = 100;
		this.currentHealth = 100;
	}

	@Override
	public int getZ() {
		return 1;
	}

	protected void setCurrentHealth(int value) {
		if (value < 0) {
			currentHealth = 0;
		} else if (value > maxHealth) {
			currentHealth = maxHealth;
		} else {
			currentHealth = value;
		}
	}

	protected void initAttackCooldown(int delay) {
		attackCooldown = new Thread(() -> {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				System.out.println("enemy melee cooldown interrupted");
			}
		});
	}

	protected boolean canAttack(Player player) {
		Rectangle2D.Double canAttackBox = new Rectangle2D.Double(hitbox.x - MELEE_ATTACK_RANGE, hitbox.y,
				WIDTH + 2 * MELEE_ATTACK_RANGE, HEIGHT);
		return canAttackBox.intersects(player.getHitbox()) && Helper.IsEntityOnFloor(hitbox);
	}

	protected boolean isInSight(Player player) {
		Rectangle2D.Double enemySight = new Rectangle2D.Double(hitbox.x - SIGHT_SIZE, hitbox.y - SIGHT_SIZE,
				hitbox.width + 2 * SIGHT_SIZE, hitbox.height + 2 * SIGHT_SIZE);
		return enemySight.intersects(player.getHitbox());
	}

	@Override
	public void receiveDamage(int damage) {
		setCurrentHealth(currentHealth - damage);
		System.out.println("Enemy is now " + currentHealth + " hp");
	}

	public abstract Item getLootItem();

}
