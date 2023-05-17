package entity;


import entity.base.Entity;
import item.Item;
import javafx.scene.canvas.GraphicsContext;
import utils.Helper;

public class DroppedItem extends Entity {

	private Item item;
	private int yspeed = 8;
	
	public DroppedItem(double x, double y, int width, int height, Item item) {
		super(x, y, width, height);
		initHitbox(x, y, width, height);
		this.item = item;
	}
	
	private void fall() {
		if (Helper.CanMoveHere(hitbox.x, hitbox.y + 1, hitbox.width, hitbox.height)) {
			hitbox.y += 1;
			yspeed += 1;
		} else {
			hitbox.y = Helper.GetEntityYPosUnderRoofOrAboveFloor(hitbox, yspeed);
			if (yspeed < 0) {
				yspeed = 0;
				yspeed += 1;
			}
		}
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(item.getColor());
		gc.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
	}

	@Override
	public int getZ() {
		return 2;
	}

	@Override
	public void update() {
		fall();
	}
	
	public Item getItem() {
		return item;
	}
}
