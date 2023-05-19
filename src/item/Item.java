package item;

import javafx.scene.image.Image;

public abstract class Item {

	protected String name;
	protected Image image;

	public Item(String name, Image image) {
		this.name = name;
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	public String getName() {
		return name;
	}
}
