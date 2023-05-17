package item;

import javafx.scene.paint.Color;

public abstract class Item {
	
	protected int power;
	protected Color color;

	public Item() {
		
	}

	public Color getColor() {
		return color;
	}
	
	public int getPower() {
		return power;
	}
}
