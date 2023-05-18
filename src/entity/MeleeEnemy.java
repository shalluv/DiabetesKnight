package entity;

import static utils.Constants.AttackState.READY;
import static utils.Constants.EnemyConstants.BASE_X_SPEED;
import static utils.Constants.EnemyConstants.HEIGHT;
import static utils.Constants.EnemyConstants.INITIAL_X_SPEED;
import static utils.Constants.EnemyConstants.INITIAL_Y_SPEED;
import static utils.Constants.EnemyConstants.MAX_Y_SPEED;
import static utils.Constants.EnemyConstants.SIGHT_SIZE;
import static utils.Constants.EnemyConstants.WEIGHT;
import static utils.Constants.EnemyConstants.WIDTH;

import java.awt.geom.Rectangle2D;

import entity.base.Enemy;
import item.Weapon;
import item.derived.Spear;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import logic.GameLogic;
import utils.Helper;

public class MeleeEnemy extends Enemy {

	private double xspeed;
	private double yspeed;
	private int attackState;
	private Spear spear;
	// private Image image;

	public MeleeEnemy(double x, double y) {
		super(x, y, WIDTH, HEIGHT, SIGHT_SIZE);
		xspeed = INITIAL_X_SPEED;
		yspeed = INITIAL_Y_SPEED;
		attackState = READY;
		initHitbox(x, y, width, height);
		spear = new Spear();
		// image = new Image("file:res/Slime/stand_and_maybe_jump/slime2-1.png");
	}

	@Override
	public void draw(GraphicsContext gc) {
//		 Hitbox Rect
		gc.setFill(Color.RED);
		if (attackState != READY) {
			((Weapon) spear).draw(gc, this);
		}
		gc.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

		// draw HP
		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.BOTTOM);
		gc.setFill(Color.RED);
		gc.fillText(Integer.toString(currentHealth), hitbox.x, hitbox.y);
//		 gc.drawImage(image, hitbox.x, hitbox.y, width, height);
	}

	private void jump() {
		yspeed = -MAX_Y_SPEED;
	}

	private void move() {
		if (Helper.CanMoveHere(hitbox.x + xspeed, hitbox.y, hitbox.width, hitbox.height)) {
			hitbox.x += xspeed;
		} else {
			hitbox.x = Helper.GetEntityXPosNextToWall(hitbox, xspeed);
			if (attackState == READY)
				jump();
		}

		if (Helper.CanMoveHere(hitbox.x, hitbox.y + yspeed, hitbox.width, hitbox.height)) {
			hitbox.y += yspeed;
			yspeed += WEIGHT;
		} else {
			hitbox.y = Helper.GetEntityYPosUnderRoofOrAboveFloor(hitbox, yspeed);
			if (yspeed < 0) {
				yspeed = 0;
				yspeed += WEIGHT;
			}
		}
	}

	private boolean canAttack() {
		Rectangle2D.Double canAttackBox = new Rectangle2D.Double(hitbox.x - spear.getAttackRange(), hitbox.y,
				WIDTH + 2 * spear.getAttackRange(), HEIGHT);
		return canAttackBox.intersects(GameLogic.getPlayer().getHitbox()) && Helper.IsEntityOnFloor(hitbox);
	}

	private void updateXSpeed() {
		if (isInSight(GameLogic.getPlayer())) {
			if (GameLogic.getPlayer().getHitbox().getMaxX() + spear.getAttackRange() / 2 < hitbox.x && Helper
					.IsEntityOnFloor(new Rectangle2D.Double(hitbox.x - WIDTH, hitbox.y + 3 * HEIGHT, WIDTH, HEIGHT))) {
				xspeed = -BASE_X_SPEED;
			} else if (GameLogic.getPlayer().getHitbox().x > hitbox.getMaxX() + spear.getAttackRange() / 2 && Helper
					.IsEntityOnFloor(new Rectangle2D.Double(hitbox.getMaxX(), hitbox.y + 3 * HEIGHT, WIDTH, HEIGHT))) {
				xspeed = BASE_X_SPEED;
			} else {
				xspeed = INITIAL_X_SPEED;
			}
		} else {
			xspeed = INITIAL_X_SPEED;
		}
	}

	@Override
	public void update() {
		if (GameLogic.getPlayer() != null) {
			updateXSpeed();
		}

		yspeed = Math.max(-MAX_Y_SPEED, Math.min(yspeed, MAX_Y_SPEED));

		move();

		if (attackState != READY)
			attackState = spear.updateAttack(this);
		if (GameLogic.getPlayer() != null && canAttack() && attackState == READY)
			attackState = spear.attack(GameLogic.getPlayer().getHitbox().getCenterX(),
					GameLogic.getPlayer().getHitbox().getCenterY(), this);

		if (currentHealth == 0) {
			isDestroy = true;
			if (attackCooldown != null)
				attackCooldown.interrupt();
		}
	}

}
