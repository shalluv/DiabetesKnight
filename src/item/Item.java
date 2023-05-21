package item;

import javafx.scene.image.Image;

/**
 * Item
 * Represents an item in the game
 */
public abstract class Item {

	/**
	 * The name of the item
	 */
	protected String name;
	/**
	 * The image of the item
	 * @see javafx.scene.image.Image
	 */
	protected Image image;

	/**
	 * Constructor
	 * @param name Name of the item
	 * @param image Image of the item
	 */
	public Item(String name, Image image) {
		this.name = name;
		this.image = image;
	}

	/**
	 * Get the image of the item
	 * @return Image of the item
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Get the name of the item
	 * @return Name of the item
	 */
	public String getName() {
		return name;
	}
}
