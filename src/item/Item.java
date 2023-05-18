package item;

import javafx.scene.paint.Color;

public abstract class Item {

	protected String name;
	protected Color color;

	public Item(String name, Color color) {
		this.name = name;
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public String getName() {
		return name;
	}
}
