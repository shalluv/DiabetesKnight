package item.derived;

import static utils.Constants.SCALE;
import static utils.Constants.AttackState.IN_PROGRESS;
import static utils.Constants.AttackState.ON_COOLDOWN;
import static utils.Constants.AttackState.READY;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Weapon.SwordConstants.AFTER_ATTACK_DELAY;
import static utils.Constants.Weapon.SwordConstants.ATTACK_BOX_HEIGHT;
import static utils.Constants.Weapon.SwordConstants.ATTACK_DELAY;
import static utils.Constants.Weapon.SwordConstants.BASE_ATTACK_RANGE;
import static utils.Constants.Weapon.SwordConstants.BASE_DAMAGE;
import static utils.Constants.Weapon.SwordConstants.BASE_X_SPEED_MULTIPLIER;
import static utils.Constants.Weapon.SwordConstants.BASE_Y_SPEED_MULTIPLIER;
import static utils.Constants.Weapon.SwordConstants.MAXIMUM_SWING;
import static utils.Constants.Weapon.SwordConstants.SWING_SPEED;
import static utils.Constants.Weapon.SwordConstants.ULTIMATE_ATTACK_RANGE;
import static utils.Constants.Weapon.SwordConstants.ULTIMATE_COST;
import static utils.Constants.Weapon.SwordConstants.ULTIMATE_DAMAGE;
import static utils.Constants.Weapon.SwordConstants.ULTIMATE_DURATION;
import static utils.Constants.Weapon.SwordConstants.ULTIMATE_X_SPEED_MULTIPLIER;
import static utils.Constants.Weapon.SwordConstants.ULTIMATE_Y_SPEED_MULTIPLIER;
import static utils.Constants.Weapon.SwordConstants.Animations.ANIMATION_HEIGHT_OFFSET;
import static utils.Constants.Weapon.SwordConstants.Animations.ANIMATION_OFFSET_Y;
import static utils.Constants.Weapon.SwordConstants.Animations.SPRITE_SIZE;
import static utils.Constants.Weapon.SwordConstants.Animations.SWING_ANIMATION;
import static utils.Constants.Weapon.SwordConstants.Animations.SWING_DONE;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import entity.base.Entity;
import item.MeleeWeapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import logic.GameLogic;
import utils.Loader;

/**
 * Sword Represents a sword weapon in the game sword is a melee weapon
 * 
 * @see item.MeleeWeapon
 */
public class Sword extends MeleeWeapon {

	/**
	 * Whehter the sword is on cooldown
	 */
	private boolean startedCooldown;
	/**
	 * Image of the sword
	 * 
	 * @see javafx.scene.image.Image
	 */
	private Image image;
	/**
	 * The frame counter of the sword's animation
	 */
	private int animationFrame;

	/**
	 * Constructor
	 */
	public Sword() {
		super("Yelly sword", getIdleImage(), BASE_ATTACK_RANGE, BASE_DAMAGE, BASE_X_SPEED_MULTIPLIER,
				BASE_Y_SPEED_MULTIPLIER, false);
		loadResources();
		this.attackProgress = 0;
		this.startedCooldown = false;
	}

	/**
	 * Load sword resources
	 */
	private void loadResources() {
		animationFrame = 0;
		image = Loader.GetSpriteAtlas(Loader.SWORD_ATLAS);
	}

	/**
	 * Get the image of the sword when it is idle
	 * 
	 * @return the image of the sword when it is idle
	 */
	private static Image getIdleImage() {
		PixelReader reader = Loader.GetSpriteAtlas(Loader.SWORD_ATLAS).getPixelReader();
		Image idle = new WritableImage(reader, 0, 0, SPRITE_SIZE, SPRITE_SIZE);
		return idle;
	}

	/**
	 * Update the attacking progress of the sword
	 * 
	 * @param attacker the entity who is holding the sword
	 * @return the attack state of the sword
	 */
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

	/**
	 * Update the attack box of the sword
	 * 
	 * @param attacker the entity who is holding the sword
	 */
	@Override
	protected void updateAttackBox(Entity attacker) {
		Rectangle2D.Double hitbox = attacker.getHitbox();
		Rectangle2D.Double baseAttackBox = new Rectangle2D.Double(hitbox.getCenterX() - ATTACK_BOX_HEIGHT / 2,
				hitbox.getCenterY() - attackRange - hitbox.height / 2, ATTACK_BOX_HEIGHT,
				attackRange + hitbox.height / 2);
		double swingAngle = Math.toRadians(attackProgress);
		AffineTransform transform = new AffineTransform();
		transform.rotate(swingAngle, hitbox.getCenterX(), hitbox.getCenterY());
		attackBox = transform.createTransformedShape(baseAttackBox);
	}

	/**
	 * Draw the sword
	 * 
	 * @param gc           the graphics context
	 * @param x            the x position of the sword
	 * @param y            the y position of the sword
	 * @param width        the width of the sword
	 * @param height       the height of the sword
	 * @param isFacingLeft whether the entity who is holding the sword is facing
	 *                     left
	 */
	@Override
	public void draw(GraphicsContext gc, double x, double y, double width, double height, boolean isFacingLeft) {
		if (attackState == READY) {
			gc.drawImage(image, 0, 0, SPRITE_SIZE, SPRITE_SIZE, x + (isFacingLeft ? 1 : 8), y + 2, width, height);
		}
		if (attackState != IN_PROGRESS)
			return;
		double scaledWidth = width * SCALE / 2;
		double scaledHeight = height * SCALE / 2;
		double centerX = x + scaledWidth;
		double centerY = y + scaledHeight;
		if (isFacingLeft)
			width = -width - attackRange;
		else
			width = width + attackRange;
		gc.drawImage(image, animationFrame * SPRITE_SIZE, 0, SPRITE_SIZE, SPRITE_SIZE, centerX - ATTACK_BOX_HEIGHT / 2,
				centerY - attackRange - scaledHeight / 2 - ANIMATION_OFFSET_Y, width, height + ANIMATION_HEIGHT_OFFSET);
		if (Math.abs(attackProgress) >= MAXIMUM_SWING / 5 && Math.abs(attackProgress) <= MAXIMUM_SWING / 2
				&& animationFrame != SWING_ANIMATION)
			animationFrame += 1;
		else if (Math.abs(attackProgress) > MAXIMUM_SWING / 2 && animationFrame != SWING_DONE)
			animationFrame += 1;
	}

	/**
	 * Update the attack progress when the sword's attacking state is in progress
	 * 
	 * @param attacker the entity who is holding the sword
	 */
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

	/**
	 * Update the attack progress when the sword's attacking state is on cooldown
	 */
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
			animationFrame = 0;
		}
	}

	/**
	 * Use the sword's ultimate
	 */
	@Override
	public void useUlitmate() {
		if (isOnUltimate)
			return;
		int currentPower = GameLogic.getPlayer().getCurrentPower();
		if (currentPower >= ULTIMATE_COST) {
			GameLogic.getPlayer().setCurrentPower(currentPower - ULTIMATE_COST);
			XSpeedMultiplier = ULTIMATE_X_SPEED_MULTIPLIER;
			YSpeedMultiplier = ULTIMATE_Y_SPEED_MULTIPLIER;
			damage = ULTIMATE_DAMAGE;
			attackRange = ULTIMATE_ATTACK_RANGE;
			canMultipleHit = true;
			isOnUltimate = true;
			initOnUltimate(ULTIMATE_DURATION);
			onUltimate.start();
		}
	}

	/**
	 * Reset the sword's status
	 */
	@Override
	public void resetStatus() {
		isOnUltimate = false;
		XSpeedMultiplier = BASE_X_SPEED_MULTIPLIER;
		YSpeedMultiplier = ULTIMATE_Y_SPEED_MULTIPLIER;
		attackRange = BASE_ATTACK_RANGE;
		canMultipleHit = false;
		damage = BASE_DAMAGE;
	}

	@Override
	protected void playAttackSound() {
		Loader.playSound(Loader.SWORD_ATTACK_SOUND_ATLAS);
	}

}
