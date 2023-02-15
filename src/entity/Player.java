package entity;

import java.awt.Rectangle;

import application.Main;
import application.MapData;
import entity.base.Entity;
import input.InputUtility;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;

public class Player extends Entity {

	private int width = 45;
	private int height = 45;
	private int MAX_Y_SPEED = 16;
	private int baseXSpeed = 5;
	private int xspeed = 0;
	private int yspeed = 32;
	private int weight = 1;
	private Rectangle hitbox;
	private Image image;
	private int offsetHitboxX = 8;
	private int offsetHitboxY = 8;
	private int hitboxWidthReducer = 20;

	public Player(int x, int y) {
		super(x, y);
		hitbox = new Rectangle(x - this.offsetHitboxX, y + this.offsetHitboxY, width - this.hitboxWidthReducer,
				height - +this.offsetHitboxY);
		image = new Image("file:res/Owlet_Monster/Owlet_Monster.png");
	}

	public void update() {
		if (InputUtility.getKeyPressed(KeyCode.SPACE)) {

			hitbox.y += 1;
			for (Renderable block : RenderableHolder.getInstance().getEntities()) {
				if (block instanceof Block && ((Block) block).isSolid()) {
					if (((Block) block).getHitbox().intersects(hitbox)) {
						this.jump();
					}
				}
			}
			hitbox.y -= 1;

		}
		if (InputUtility.getKeyPressed(KeyCode.A)) {
			this.xspeed = -this.baseXSpeed;
		} else if (InputUtility.getKeyPressed(KeyCode.D)) {
			this.xspeed = this.baseXSpeed;
		} else {
			this.xspeed = 0;
		}

		if (this.yspeed < -this.MAX_Y_SPEED) {
			this.yspeed = -this.MAX_Y_SPEED;
		} else if (this.yspeed > this.MAX_Y_SPEED) {
			this.yspeed = this.MAX_Y_SPEED;
		}

		this.move();

		this.clampInCanvas();
		
		this.updateScreen();

		this.hitbox.x = this.getX() + this.offsetHitboxX;
		this.hitbox.y = this.getY() + this.offsetHitboxY;
	}

	private void jump() {
		this.yspeed = -this.MAX_Y_SPEED;
	}

	private void move() {
		hitbox.x += this.xspeed;
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid()) {
				if (((Block) block).getHitbox().intersects(hitbox)) {
					hitbox.x -= this.xspeed;
					while (!((Block) block).getHitbox().intersects(hitbox)) {
						hitbox.x += Math.signum(this.xspeed);
					}
					this.hitbox.x -= Math.signum(this.xspeed);
					this.xspeed = 0;
					this.setX(this.hitbox.x - this.offsetHitboxX);
				}
			}
		}

		// gravity
		this.yspeed += this.weight;
		this.hitbox.y += this.yspeed;
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid()) {
				if (((Block) block).getHitbox().intersects(hitbox)) {
					this.hitbox.y -= this.yspeed;
					while (!((Block) block).getHitbox().intersects(hitbox)) {
						this.hitbox.y += Math.signum(this.yspeed);
					}
					this.hitbox.y -= Math.signum(this.yspeed);
					this.yspeed = 0;
					this.setY(this.hitbox.y - this.offsetHitboxY);
				}
			}
		}
		this.setX(this.getX() + this.xspeed);
		this.setY(this.getY() + this.yspeed);
	}

	private void clampInCanvas() {
		if (this.hitbox.x < 0) {
			this.setX(-this.offsetHitboxX);
		} else if (this.hitbox.x + this.width + this.offsetHitboxX - this.hitboxWidthReducer > MapData.width) {
			this.setX(MapData.width - this.width + this.offsetHitboxX);
		}
		if (this.getY() < 0) {
			this.setY(0);
		} else if (this.getY() > MapData.height - this.height) {
			Platform.exit();
		}
	}
	
	private void updateScreen() {
		if (this.getX()>640 && this.getX()+640<MapData.width) {
			Main.gameScreen.updateX(-(this.getX()-640));
		}
		if (this.getY()>480 && this.getY()+480<MapData.height) {
			Main.gameScreen.updateY(-(this.getY()-480));
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
//		Player Rect
//		gc.setFill(Color.RED);
//		gc.fillRect(this.getX(), this.getY(), this.width, this.height);

//		Hitbox Rect
//		gc.setFill(Color.GREEN);
//		gc.fillRect(this.hitbox.x, this.hitbox.y, this.hitbox.width, this.hitbox.height);

//		Image
		gc.drawImage(this.image, this.getX(), this.getY(), this.width, this.height);
	}

	/*
	 * public void setListener() { hitbox.translateXProperty().addListener((obs,
	 * old, newValue) -> { int offset = newValue.intValue(); if (offset > 640 &&
	 * offset < MapData.width - 640) { Main.gameScreen.updateX(-(offset - 640)); }
	 * }); hitbox.translateYProperty().addListener((obs, old, newValue) -> { int
	 * offset = newValue.intValue(); if (offset > 480 && offset + 480 <
	 * MapData.height) { Main.gameScreen.updateY(-(offset - 480)); } }); }
	 */

	public Rectangle getHitbox() {
		return hitbox;
	}
}
