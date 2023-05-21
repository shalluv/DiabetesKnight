package sharedObject;

import java.awt.geom.Rectangle2D;

import javafx.scene.canvas.GraphicsContext;

/**
 * Renderable
 * Represents an object that can be rendered on the screen
 */
public interface Renderable {
	/**
	 * Draws the object on the screen
	 * @param gc The GraphicsContext to draw on
	 * @param screen The Rectangle2D.Double representing the screen
	 */
	public void draw(GraphicsContext gc, Rectangle2D.Double screen);

	/**
	 * Gets the z coordinate of the object
	 * @return The z coordinate of the object
	 */
	public int getZ();

	/**
	 * Checks if the object is destroyed
	 * @return True if the object is destroyed, false otherwise
	 */
	public boolean isDestroyed();
}
