package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Loader {

	public static final String TILES_ATLAS = "res/tiles/";
	public static final String PLAYER_IDLE_ATLAS = "res/player/idle_4.png";
	public static final String PLAYER_RUN_ATLAS = "res/player/running_4.png";
	// TODO: change this to real jump atlas
	public static final String PLAYER_JUMP_ATLAS = "res/player/running_4.png";
	public static final String DUST_ATLAS = "res/player/dust_6.png";

	public static final String MELEE_IDLE_ATLAS = "res/enemy/pudrior/idle_4.png";
	public static final String RANGE_IDLE_ATLAS = "res/enemy/icecher/idle_1.png";
	public static final String RANGE_ATTACK_COOLDOWN_ATLAS = "res/enemy/icecher/attack_cooldown_1.png";

	public static final String SPEAR_ATLAS = "res/weapon/spear.png";
	public static final String GUN_ATLAS = "res/weapon/gun.png";
	public static final String BULLET_ATLAS = "res/weapon/bullet.png";
	public static final String SWORD_ATLAS = "res/weapon/sword.png";

	public static final String SUGAR_ATLAS = "res/item/sugar.png";
	public static final String INSULIN_ATLAS = "res/item/insulin.png";
	public static final String HEALTH_POTION_ATLAS = "res/item/health_potion.png";

	public static final String DOOR_ATLAS = "res/door/door_sprite.png";

	public static final String BACKGROUND_ATLAS = "res/background/";

	public static final String BACKGROUND_MUSIC_ATLAS = "res/sound/music.mp3";
	public static final String CONSUME_HEALTH_POTION_SOUND_ATLAS = "res/sound/consumeHealthPotion.wav";
	public static final String CONSUME_INSULIN_SOUND_ATLAS = "res/sound/consumeInsulin.wav";
	public static final String CONSUME_SUGAR_SOUND_ATLAS = "res/sound/consumeSugar.wav";
	public static final String HURT_SOUND_ATLAS = "res/sound/consumeSugar.wav";
	public static final String DIE_SOUND_ATLAS = "res/sound/die.wav";
	public static final String PICKUP_ITEM_SOUND_ATLAS = "res/sound/pickupItem.wav";
	public static final String SELECT_ITEM_SOUND_ATLAS = "res/sound/selectItem.wav";
	public static final String SHOOT_SOUND_ATLAS = "res/sound/shoot.wav";
	public static final String SPEAR_ATTACK_SOUND_ATLAS = "res/sound/spearAttack.wav";
	public static final String SWORD_ATTACK_SOUND_ATLAS = "res/sound/swordAttack.wav";
	public static final String WARP_SOUND_ATLAS = "res/sound/warp.wav";

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

	public static void playSound(String filename) {
		File file = new File(filename);
		Media media = new Media(file.toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}

}
