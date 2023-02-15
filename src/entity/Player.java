package entity;

import application.Main;
import application.MapData;
import entity.base.Entity;
import input.InputUtility;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import sharedObject.Renderable;
import sharedObject.RenderableHolder;

public class Player extends Entity {

	private int WIDTH = 40;
	private int HEIGHT = 40;
	private double MAX_Y_SPEED = 16;
	private double baseXSpeed = 5; 
	private double xspeed = 0;
	private double yspeed = 32;
	private double weight = 1;
	private Rectangle hitbox;
	private Image image;

	public Player(double x, double y) {
		super(x, y);
		hitbox = new Rectangle(WIDTH, HEIGHT);
		hitbox.setTranslateX(x);
		hitbox.setTranslateY(y);
		setListener();
		image = new Image("file:res/Owlet_Monster/Owlet_Monster.png");
	}

	public void update() {
		if (InputUtility.getKeyPressed(KeyCode.SPACE)) {

			hitbox.setTranslateY(hitbox.getTranslateY()+1);
			for (Renderable block : RenderableHolder.getInstance().getEntities()) {
				if(block instanceof Block && ((Block) block).isSolid()) {
					if (((Block) block).getHitbox().getBoundsInParent().intersects(hitbox.getBoundsInParent())) {
						this.jump();
					}
				}
			}
			hitbox.setTranslateY(hitbox.getTranslateY()-1);

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

		hitbox.setTranslateX(this.getX());
		hitbox.setTranslateY(this.getY());;
	}

	private void jump() {
		this.yspeed = -this.MAX_Y_SPEED;
	}

	private void move() {
		hitbox.setTranslateX(hitbox.getTranslateX()+this.xspeed);
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid()) {
				if (((Block) block).getHitbox().getBoundsInParent().intersects(hitbox.getBoundsInParent())) {
					hitbox.setTranslateX(hitbox.getTranslateX()-this.xspeed);
					while (!((Block) block).getHitbox().getBoundsInParent().intersects(hitbox.getBoundsInParent())) {
						hitbox.setTranslateX(hitbox.getTranslateX()+Math.signum(this.xspeed));
					}
					hitbox.setTranslateX(hitbox.getTranslateX()-Math.signum(this.xspeed));
					this.xspeed = 0;
					this.setX(hitbox.getTranslateX());
				}
			}
		}

		// gravity
		this.yspeed += this.weight;
		hitbox.setTranslateY(hitbox.getTranslateY()+this.yspeed);;
		for (Renderable block : RenderableHolder.getInstance().getEntities()) {
			if (block instanceof Block && ((Block) block).isSolid()) {
				if (((Block) block).getHitbox().getBoundsInParent().intersects(hitbox.getBoundsInParent())) {
					hitbox.setTranslateY(hitbox.getTranslateY()-this.yspeed);;
					while (!((Block) block).getHitbox().getBoundsInParent().intersects(hitbox.getBoundsInParent())) {
						hitbox.setTranslateY(hitbox.getTranslateY()+Math.signum(this.yspeed));
					}
					hitbox.setTranslateY(hitbox.getTranslateY()-Math.signum(this.yspeed));
					this.yspeed = 0;
					this.setY(hitbox.getTranslateY());
				}
			}
		}

		this.setX(getX() + this.xspeed);
		this.setY(this.getY() + this.yspeed);
	}

	private void clampInCanvas() {
		if (hitbox.getTranslateX() < 0) {
			this.setX(0);
		} else if (hitbox.getTranslateX() > MapData.width - WIDTH) {
			this.setX(MapData.width - WIDTH);
		}
		if (this.getY() < 0) {
			this.setY(0);
		} else if (this.getY() > MapData.height - WIDTH) {
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
	
	public void setListener() {
		hitbox.translateXProperty().addListener((obs, old, newValue) -> {
			int offset = newValue.intValue();
			if(offset > 640 && offset < MapData.width-640) {
				Main.gameScreen.updateX(-(offset - 640));
			}
		});
		hitbox.translateYProperty().addListener((obs, old, newValue) -> {
			int offset = newValue.intValue();
			if(offset > 480 && offset + 480 < MapData.height) {
				Main.gameScreen.updateY(-(offset - 480));
			}
		});
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
}
