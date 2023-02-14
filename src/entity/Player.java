package entity;

import java.awt.Rectangle;

import application.Main;
import entity.base.Entity;
import input.InputUtility;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;

public class Player extends Entity {

	private int WIDTH = 40;
	private int HEIGHT = 40;
	private int MAX_Y_SPEED = 16;
	private int baseXSpeed = 5; 
	private int xspeed = 0;
	private int yspeed = 32;
	private int weight = 1;
	private Rectangle hitbox;
	private Image image;

	public Player(int x, int y) {
		super(x, y);
		hitbox = new Rectangle(x, y, WIDTH, HEIGHT);
		image = new Image("file:res/Owlet_Monster/Owlet_Monster.png");
	}

	public void update() {
		if (InputUtility.getKeyPressed(KeyCode.SPACE)) {

			this.hitbox.y += 1;
			for (Renderable block : RenderableHolder.getInstance().getEntities()) {
				if(block instanceof Block && ((Block) block).isSolid()) {
					if (((Block) block).getHitbox().intersects(hitbox)) {
						this.jump();
					}
				}
			}
			this.hitbox.y -= 1;

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

		this.hitbox.x = this.getX();
		this.hitbox.y = this.getY();
	}

	private void jump() {
		this.yspeed = -this.MAX_Y_SPEED;
	}

	private void move() {
		this.hitbox.x += this.xspeed;
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid()) {
				if (((Block) block).getHitbox().intersects(hitbox)) {
					this.hitbox.x -= this.xspeed;
					while (!((Block) block).getHitbox().intersects(hitbox)) {
						this.hitbox.x += Math.signum(this.xspeed);
					}
					this.hitbox.x -= Math.signum(this.xspeed);
					this.xspeed = 0;
					this.setX(this.hitbox.x);
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
					this.setY(this.hitbox.y);
				}
			}
		}

		this.setX(getX() + this.xspeed);
		this.setY(this.getY() + this.yspeed);
	}

	private void clampInCanvas() {
		if (this.hitbox.x < 0) {
			this.setX(0);
		} else if (this.hitbox.x > Main.CANVAS_WIDTH - this.hitbox.width) {
			this.setX(Main.CANVAS_WIDTH - this.hitbox.width);
		}
		if (this.getY() < 0) {
			this.setY(0);
		} else if (this.getY() > Main.CANVAS_HEIGHT - this.WIDTH) {
//			this.setY(Main.CANVAS_HEIGHT - this.WIDTH);
			Platform.exit(); 
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		// Player Rect
		// gc.setFill(Color.RED);
		// gc.fillRect(this.getX(), this.getY(), this.WIDTH, this.HEIGHT);

		// Hitbox Rect
		// gc.setFill(Color.GREEN);
		// gc.fillRect(this.hitbox.x, this.hitbox.y, this.hitbox.width,
		// this.hitbox.height);

		// Image
		gc.drawImage(this.image, this.getX(), this.getY(), this.WIDTH, this.HEIGHT);
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
}
