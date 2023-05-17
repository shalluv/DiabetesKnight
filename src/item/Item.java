package item;

import javafx.scene.paint.Color;

public abstract class Item {

	protected String name;
	protected int power;
	protected Color color;

	public Item(String name, int power, Color color) {
		this.name = name;
		this.power = power;
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public int getPower() {
		return power;
	}

	public String getName() {
		return name;
	}
}
