package entity;

import java.awt.Rectangle;

import application.Main;
import entity.base.Entity;
import input.InputUtility;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;

public class Player extends Entity {
	
	private int WIDTH = 40;
	private int HEIGHT = 40;
	private int base_x_speed = 2;
	private int xspeed = 2;
	private int yspeed = 16;
	private int weight = 1;
	private Rectangle hitbox;

	public Player(int x, int y) {
		super(x, y);
		hitbox = new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public void update() {
		if (InputUtility.getKeyPressed(KeyCode.W)) {

			this.hitbox.y += 1;
			for(Renderable block: RenderableHolder.getInstance().getEntities()) {
				if(block instanceof Block) {
					if(((Block) block).getHitbox().intersects(hitbox)) {
						this.jump();
					}
				}
			}
			this.hitbox.y -= 1;
			
		}
		if (InputUtility.getKeyPressed(KeyCode.A)) {
			this.xspeed = -this.base_x_speed;
		} else if (InputUtility.getKeyPressed(KeyCode.D)) {
			this.xspeed = this.base_x_speed;
		} else {
			this.xspeed = 0;
		}

		this.move();
		
		this.clampInCanvas();
		
		this.hitbox.x = this.getX();
		this.hitbox.y = this.getY();
	}
	
	private void jump() {
		this.yspeed = -16;
	}

	private void move() {
		this.hitbox.x += this.xspeed;
		for(Renderable block: RenderableHolder.getInstance().getEntities()) {
			if(block instanceof Block) {
				if(hitbox.intersects(((Block) block).getHitbox())) {
					this.hitbox.x -= this.xspeed;
					while(!((Block) block).getHitbox().intersects(hitbox)) {
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
		for(Renderable block: RenderableHolder.getInstance().getEntities()) {
			if(block instanceof Block) {
				if(hitbox.intersects(((Block) block).getHitbox())) {
					this.hitbox.y -= this.yspeed;
					while(!((Block) block).getHitbox().intersects(hitbox)) {
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
		if(this.getX() < 0) {
			this.setX(0);
		} else if(this.getX() > Main.CANVAS_WIDTH - this.WIDTH) {
			this.setX(Main.CANVAS_WIDTH - this.WIDTH);
		}
		if(this.getY() < 0) {
			this.setY(0);
		}/* else if(this.getY() > Main.CANVAS_HEIGHT - this.WIDTH) {
			this.setY(Main.CANVAS_HEIGHT - this.WIDTH);
		}*/
	}

	@Override
	public void draw(GraphicsContext gc) {
		// draw red rectangle
		gc.setFill(Color.GREEN);
		gc.fillRect(this.getX(), this.getY(), this.WIDTH, this.HEIGHT);
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
}
