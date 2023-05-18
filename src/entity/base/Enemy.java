package entity.base;

import java.awt.geom.Rectangle2D;

import entity.Player;
import interfaces.Damageable;
import item.Item;
import item.derived.Sugar;

public abstract class Enemy extends Entity implements Damageable {

	protected int maxHealth;
	protected int currentHealth;
	protected Thread attackCooldown;
	protected int sightSize;
	protected Item lootItem;

	public Enemy(double x, double y, int width, int height, int sightSize) {
		super(x, y, width, height);
		this.maxHealth = 100;
		this.currentHealth = 100;
		this.sightSize = sightSize;
		this.lootItem = new Sugar(10);
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
				System.out.println("enemy cooldown interrupted");
			}
		});
	}

	protected boolean isInSight(Player player) {
		Rectangle2D.Double enemySight = new Rectangle2D.Double(hitbox.x - sightSize, hitbox.y - sightSize,
				hitbox.width + 2 * sightSize, hitbox.height + 2 * sightSize);
		return enemySight.intersects(player.getHitbox());
	}

	@Override
	public void receiveDamage(int damage) {
		setCurrentHealth(currentHealth - damage);
		System.out.println("Enemy is now " + currentHealth + " hp");
	}

	public Item getLootItem() {
		return lootItem;
	}

}
