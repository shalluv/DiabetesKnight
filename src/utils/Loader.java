package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.scene.image.Image;

public class Loader {

	public static final String TILES_ATLAS = "res/1_Tiles/";
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
