package item;

import static utils.Constants.AttackState.READY;

import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Weapon extends Item {

	protected int attackState;
	protected Thread cooldown;

	public Weapon(String name, Color color) {
		super(name, color);
		this.attackState = READY;
	}

	protected void initCooldown(int delay) {
		cooldown = new Thread(() -> {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				System.out.println("attack cooldown interrupted");
			}
		});
	}

	public abstract void draw(GraphicsContext gc, Entity attacker);

	public abstract int updateAttack(Entity attacker);

	public abstract int attack(double targetX, double targetY, Entity attacker);

	public void cancelAttack() {
		this.attackState = READY;
		if (cooldown != null)
			cooldown.interrupt();
		cooldown = null;
	}

}
