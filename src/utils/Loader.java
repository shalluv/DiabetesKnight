package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.image.Image;

/**
 * Loader
 * a class that utilizes the loading of resources
 */
public class Loader {

	/**
	 * The path to tiles atlas
	 */
	public static final String TILES_ATLAS = "res/tiles/";

	/**
	 * The path to the player sprite when idle atlas
	 */
	public static final String PLAYER_IDLE_ATLAS = "res/player/idle_4.png";
	/**
	 * The path to the player sprite when running atlas
	 */
	public static final String PLAYER_RUN_ATLAS = "res/player/running_4.png";
	/**
	 * The path to the player sprite when jumping atlas
	 */
	public static final String PLAYER_JUMP_ATLAS = "res/player/running_4.png";
	/**
	 * The path to the dust sprite atlas
	 */
	public static final String DUST_ATLAS = "res/player/dust_6.png";

	/**
	 * The path to the melee enemy sprite when idle atlas
	 */
	public static final String MELEE_IDLE_ATLAS = "res/enemy/pudrior/idle_4.png";
	/**
	 * The path to the ranged enemy sprite when idle atlas
	 */
	public static final String RANGE_IDLE_ATLAS = "res/enemy/icecher/idle_1.png";
	/**
	 * The path to the ranged enemy sprite when attacking is on cooldown atlas
	 */
	public static final String RANGE_ATTACK_COOLDOWN_ATLAS = "res/enemy/icecher/attack_cooldown_1.png";

	/**
	 * The path to the spear atlas
	 */
	public static final String SPEAR_ATLAS = "res/weapon/spear.png";

	/**
	 * The path to the gun atlas
	 */
	public static final String GUN_ATLAS = "res/weapon/gun.png";

	/**
	 * The path to the bullet atlas
	 */
	public static final String BULLET_ATLAS = "res/weapon/bullet.png";

	/**
	 * The path to the sword atlas
	 */
	public static final String SWORD_ATLAS = "res/weapon/sword.png";

	/**
	 * The path to the sugar atlas
	 */
	public static final String SUGAR_ATLAS = "res/item/sugar.png";

	/**
	 * The path to the insulin atlas
	 */
	public static final String INSULIN_ATLAS = "res/item/insulin.png";

	/**
	 * The path to the health potion atlas
	 */
	public static final String HEALTH_POTION_ATLAS = "res/item/health_potion.png";

	/**
	 * The path to the door sprite atlas
	 */
	public static final String DOOR_ATLAS = "res/door/door_sprite.png";

	/**
	 * The path to the background atlas
	 */
	public static final String BACKGROUND_ATLAS = "res/background/";

	/**
	 * private constructor to prevent instantiation
	 */
	private Loader() { }

	/**
	 * Get the image of the sprite atlas
	 * @param filename the filename of the sprite atlas
	 * @return the image of the sprite atlas
	 */
	public static Image GetSpriteAtlas(String filename) {
		Image img = null;
		try {
			FileInputStream is = new FileInputStream(filename);

			img = new Image(is);

			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return img;
	}
}
