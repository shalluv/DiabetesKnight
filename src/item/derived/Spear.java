package item.derived;

import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.AttackState.ON_COOLDOWN;
import static utils.Constants.AttackState.READY;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.PlayerConstants.Animations.WEAPON_OFFSET_X;
import static utils.Constants.PlayerConstants.Animations.WEAPON_OFFSET_Y;
import static utils.Constants.Weapon.SpearConstants.ATTACK_BOX_HEIGHT;
import static utils.Constants.Weapon.SpearConstants.ATTACK_DELAY;
import static utils.Constants.Weapon.SpearConstants.ATTACK_RANGE;
import static utils.Constants.Weapon.SpearConstants.ATTACK_SPEED;
import static utils.Constants.Weapon.SpearConstants.DAMAGE;

import java.awt.geom.Rectangle2D;

import entity.Player;
import entity.base.Entity;
import item.MeleeWeapon;
import javafx.scene.canvas.GraphicsContext;
import utils.Loader;

public class Spear extends MeleeWeapon {

	private int attackProgress;

	public Spear() {
		super("Spear", Loader.GetSpriteAtlas(Loader.SPEAR_ATLAS), ATTACK_RANGE, DAMAGE);
		this.attackRange = ATTACK_RANGE;
	}

	@Override
	public void draw(GraphicsContext gc, double x, double y, double width, double height, boolean isFacingLeft) {
		if (isFacingLeft) {
			width = -width;
			x -= attackProgress;
			x += WEAPON_OFFSET_X * 2;
		} else {
			x += WEAPON_OFFSET_X;
			x += attackProgress;
		}
		gc.drawImage(image, x, y + WEAPON_OFFSET_Y, width, height);
	}

	@Override
	protected void updateAttackBox(Entity attacker) {
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

	private void inProgressUpdate(Entity attacker) {
		if (attackProgress < ATTACK_RANGE) {
			if (cooldown == null || !cooldown.isAlive()) // if it is not cooldown init cooldown thread
				initCooldown(ATTACK_DELAY);
			attackProgress += ATTACK_SPEED;
		}
		updateAttackBox(attacker);
		if (attacker instanceof Player) {
		}
		if (isAttackingWall()) {
			attackProgress -= ATTACK_SPEED;
			attackState = ON_COOLDOWN;
		} else if (attackProgress >= ATTACK_RANGE) {// if it is out of range then stop the attack
			attackState = ON_COOLDOWN;
		} else
			cooldown.start();
	}

	private void onCooldownUpdate() {
		if (attackProgress > 0) {
			if (cooldown == null || !cooldown.isAlive())
				initCooldown(ATTACK_DELAY);
			attackProgress -= ATTACK_SPEED;
			cooldown.start();
		} else {
			cooldown = null;
			attackProgress = 0;
			attackState = READY;
			attackBox = null;
		}
	}

}
