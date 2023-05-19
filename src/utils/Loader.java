package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.image.Image;

public class Loader {

	public static final String TILES_ATLAS = "res/tiles/";
	public static final String PLAYER_IDLE_ATLAS = "res/player/idle_4.png";
	public static final String PLAYER_RUN_ATLAS = "res/player/running_4.png";
	//TODO: change this to real jump atlas
	public static final String PLAYER_JUMP_ATLAS = "res/player/running_4.png"; 
	public static final String MELEE_IDLE_ATLAS = "res/enemy/pudrior/idle_4.png";
	public static final String DUST_ATLAS = "res/player/dust_6.png";
	public static final String BACKGROUND_ATLAS = "res/2_Background/Day/Background.png";

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
