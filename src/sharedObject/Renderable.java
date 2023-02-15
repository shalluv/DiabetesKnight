package sharedObject;

import javafx.scene.canvas.GraphicsContext;

public interface Renderable {
	public void draw(GraphicsContext gc);

	public boolean isDestroyed();
}
