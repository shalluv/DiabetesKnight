package input;

import java.util.ArrayList;

import application.Main;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import utils.Constants.InputConstants;

public class InputUtility {
	private static ArrayList<KeyCode> keyPressed = new ArrayList<>();
	private static boolean isLeftDown = false;
	private static boolean isRightDown = false;
	private static double mouseX;
	private static double mouseY;

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

	public static void setMouseX(double x) {
		mouseX = x;
	}

	public static double getMouseX() {
		return mouseX;
	}

	public static void setMouseY(double y) {
		mouseY = y;
	}

	public static double getMouseY() {
		return mouseY;
	}

	public static void update() {
		Platform.runLater(() -> {
			Robot robot = new Robot();
			double mouseX = robot.getMouseX();
			double mouseY = robot.getMouseY();
			setMouseX(mouseX - Main.gameScreen.getLayoutX() - Main.gameStage.getX() - InputConstants.CURSOR_OFFSET_X);
			setMouseY(mouseY - Main.gameScreen.getLayoutY() - Main.gameStage.getY() - InputConstants.CURSOR_OFFSET_Y);
		});
	}
}
