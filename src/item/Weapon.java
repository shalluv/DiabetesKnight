package item;

import static utils.Constants.AttackState.READY;

import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Weapon extends Item {

	protected int attackState;
	protected Thread cooldown;
	protected Thread onUltimate;
	protected boolean isOnUltimate;
	protected double XSpeedMultiplier;
	protected double YSpeedMultiplier;

	public Weapon(String name, Image image, double XSpeedMultiplier, double YSpeedMultiplier) {
		super(name, image);
		this.attackState = READY;
		this.XSpeedMultiplier = XSpeedMultiplier;
		this.YSpeedMultiplier = YSpeedMultiplier;
		this.isOnUltimate = false;
	}

	public double getXSpeedMultiplier() {
		return XSpeedMultiplier;
	}

	public double getYSpeedMultiplier() {
		return YSpeedMultiplier;
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

	protected void initOnUltimate(int delay) {
		onUltimate = new Thread(() -> {
			try {
				Thread.sleep(delay);
				resetStatus();
			} catch (InterruptedException e) {
				System.out.println("ultimate cooldown interrupted");
			}
		});
	}

	public abstract void draw(GraphicsContext gc, double x, double y, double width, double height,
			boolean isFacingLeft);

	public abstract int updateAttack(Entity attacker);

	public abstract int attack(double targetX, double targetY, Entity attacker);

	public abstract void useUlitmate();

	public abstract void resetStatus();

	public void cancelAttack() {
		this.attackState = READY;
		if (cooldown != null)
			cooldown.interrupt();
		cooldown = null;
	}

	public void cancelUltimate() {
		if (onUltimate != null)
			onUltimate.interrupt();
		isOnUltimate = false;
	}

	public void clearThread() {
		if (cooldown != null)
			cooldown.interrupt();
		if (onUltimate != null)
			onUltimate.interrupt();
	}

}
