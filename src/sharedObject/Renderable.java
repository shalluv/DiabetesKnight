package sharedObject;

import java.awt.geom.Rectangle2D;

import javafx.scene.canvas.GraphicsContext;

public interface Renderable {
	public void draw(GraphicsContext gc, Rectangle2D.Double screen);

	public int getZ();

	public boolean isDestroyed();
}
