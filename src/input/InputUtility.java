package input;

import java.util.ArrayList;

import javafx.scene.input.KeyCode;

public class InputUtility {
	private static ArrayList<KeyCode> keyPressed = new ArrayList<>();
	private static boolean isLeftDown = false;
	private static boolean isRightDown = false;

	public static boolean getKeyPressed(KeyCode keycode) {
		return keyPressed.contains(keycode);
	}

	public static void setKeyPressed(KeyCode keycode, boolean pressed) {
		if (pressed) {
			if (!keyPressed.contains(keycode)) {
				keyPressed.add(keycode);
			}
		} else {
			keyPressed.remove(keycode);
		}
	}

	public static void mouseLeftDown() {
		isLeftDown = true;
	}

	public static void mouseLeftUp() {
		isLeftDown = false;
	}

	public static void mouseRightDown() {
		isRightDown = true;
	}

	public static void mouseRightUp() {
		isRightDown = false;
	}

	public static boolean isLeftDown() {
		return isLeftDown;
	}

	public static boolean isRightDown() {
		return isRightDown;
	}
}
