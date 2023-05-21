package entity;

import java.awt.geom.Rectangle2D;

import entity.base.Entity;
import item.Item;
import javafx.scene.canvas.GraphicsContext;
import utils.Helper;

/**
 * DroppedItem
 * Represents a dropped item in the game
 * @see item.Item
 */
public class DroppedItem extends Entity {

	/**
	 * The item data of the dropped item
	 * @see item.Item
	 */
	private Item item;
	/**
	 * The y-axis speed of the dropped item
	 */
	private int yspeed = 8;

	/**
	 * Constructor
	 * @param x x coordinate of the dropped item
	 * @param y y coordinate of the dropped item
	 * @param width width of the dropped item
	 * @param height height of the dropped item
	 * @param item the item
	 */
	public DroppedItem(double x, double y, int width, int height, Item item) {
		super(x, y, width, height);
		initHitbox(x, y, width, height);
		this.item = item;
	}

	/**
	 * Make the dropped item fall in case it is not on the ground
	 * @see utils.Helper#CanMoveHere(double, double, double, double)
	 * @see utils.Helper#GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Double, double)
	 */
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

	/**
	 * Draw the dropped item
	 * @param gc GraphicsContext
	 * @param screen the screen
	 */
	@Override
	public void draw(GraphicsContext gc, Rectangle2D.Double screen) {
		if (!screen.intersects(hitbox))
			return;
		gc.drawImage(item.getImage(), hitbox.x, hitbox.y, hitbox.width, hitbox.height);
	}

	/**
	 * Get the z coordinate of the dropped item
	 * @return 1
	 */
	@Override
	public int getZ() {
		return 1;
	}

	/**
	 * Update the dropped item
	 * @see #fall()
	 */
	@Override
	public void update() {
		fall();
	}

	/**
	 * Get the item
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}
}
