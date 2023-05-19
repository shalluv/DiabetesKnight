package item.derived;

import static utils.Constants.Weapon.SwordConstants.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.AttackState.ON_COOLDOWN;
import static utils.Constants.AttackState.READY;
import static utils.Constants.Directions.*;
import static utils.Constants.SCALE;

import entity.base.Entity;
import item.MeleeWeapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utils.Loader;

public class Sword extends MeleeWeapon {

	private boolean startedCooldown;

	public Sword() {
		super("sword", Loader.GetSpriteAtlas(Loader.SPEAR_ATLAS), ATTACK_RANGE, DAMAGE, SPEED_MULTIPLIER);
		this.attackProgress = 0;
		this.startedCooldown = false;
	}

	@Override
	protected int updateProgress(Entity attacker) {
		if (cooldown != null && cooldown.isAlive()) // cooldown then don't update progress
			return attackState;
		if (attackState == IN_PROGRESS) {
			inProgressUpdate(attacker);
		} else if (attackState == ON_COOLDOWN) {
			onCooldownUpdate();
		}
		return attackState;
	}

	@Override
	protected void updateAttackBox(Entity attacker) {
		Rectangle2D.Double hitbox = attacker.getHitbox();
		Rectangle2D.Double baseAttackBox = new Rectangle2D.Double(hitbox.getCenterX() - ATTACK_BOX_HEIGHT / 2,
				hitbox.getCenterY() - ATTACK_RANGE - hitbox.height / 2, ATTACK_BOX_HEIGHT,
				ATTACK_RANGE + hitbox.height / 2);
		double swingAngle = Math.toRadians(attackProgress);
		AffineTransform transform = new AffineTransform();
		transform.rotate(swingAngle, hitbox.getCenterX(), hitbox.getCenterY());
		attackBox = transform.createTransformedShape(baseAttackBox);
	}

	@Override
	public void draw(GraphicsContext gc, double x, double y, double width, double height, boolean isFacingLeft) {
		if (attackState != IN_PROGRESS)
			return;
		double scaledWidth = width * SCALE;
		double scaledHeight = height * SCALE;
		double rotationCenterX = x + scaledWidth / 2;
		double rotationCenterY = y + scaledHeight / 2;
		gc.setFill(Color.RED);
		gc.save();
		gc.translate(rotationCenterX, rotationCenterY);
		gc.rotate(attackProgress);
		gc.translate(-rotationCenterX, -rotationCenterY);
		gc.fillRect(rotationCenterX - ATTACK_BOX_HEIGHT / 2, rotationCenterY - ATTACK_RANGE - scaledHeight / 2,
				ATTACK_BOX_HEIGHT, ATTACK_RANGE + scaledHeight / 2);
		gc.restore();
	}

	private void inProgressUpdate(Entity attacker) {
		if (Math.abs(attackProgress) < Math.abs(MAXIMUM_SWING)) {
			if (cooldown == null || !cooldown.isAlive()) // if it is not cooldown init cooldown thread
				initCooldown(ATTACK_DELAY);
			if (attackDirection == LEFT)
				attackProgress -= SWING_SPEED;
			else
				attackProgress += SWING_SPEED;
			updateAttackBox(attacker);
			if (isAttackingWall() || Math.abs(attackProgress) >= Math.abs(MAXIMUM_SWING))
				attackState = ON_COOLDOWN;
			else
				cooldown.start();
		}
	}

	private void onCooldownUpdate() {
		if (cooldown == null || !cooldown.isAlive() && !startedCooldown) {
			initCooldown(AFTER_ATTACK_DELAY);
			cooldown.start();
			startedCooldown = true;
		} else {
			cooldown = null;
			attackProgress = 0;
			attackState = READY;
			attackBox = null;
			startedCooldown = false;
		}
	}

}
