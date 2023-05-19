package item;

import static utils.Constants.AttackState.READY;

import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Weapon extends Item {

	protected int attackState;
	protected Thread cooldown;
	protected double speedReducer;

	public Weapon(String name, Image image) {
		super(name, image);
		this.attackState = READY;
		this.speedReducer = 1;
	}

	public Weapon(String name, Image image, double weight) {
		super(name, image);
		this.attackState = READY;
		this.speedReducer = weight;
	}

	public double getSpeedReducer() {
		return speedReducer;
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

	public abstract void draw(GraphicsContext gc, double x, double y, double width, double height,
			boolean isFacingLeft);

	public abstract int updateAttack(Entity attacker);

	public abstract int attack(double targetX, double targetY, Entity attacker);

	public void cancelAttack() {
		this.attackState = READY;
		if (cooldown != null)
			cooldown.interrupt();
		cooldown = null;
	}

}
