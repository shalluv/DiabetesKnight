package input;

import java.util.ArrayList;

import application.Main;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import utils.Constants.InputConstants;

/**
 * InputUtility Handles input from the user
 */
public class InputUtility {
	/**
	 * private constructor to prevent instantiation
	 */
	private InputUtility() {
	}

	/**
	 * The array list of keys pressed
	 * 
	 * @see javafx.scene.input.KeyCode
	 */
	private static ArrayList<KeyCode> keyPressed = new ArrayList<>();
	/**
	 * Whether the mouse left button is down
	 */
	private static boolean isLeftDown = false;
	/**
	 * Whether the mouse right button is down
	 */
	private static boolean isRightDown = false;
	/**
	 * The x coordinate of the mouse
	 */
	private static double mouseX;
	/**
	 * The y coordinate of the mouse
	 */
	private static double mouseY;
	/**
	 * The scroll delta of the mouse in the y-axis
	 */
	private static double scrollDeltaY;

	/**
	 * Get whether a key is pressed
	 * 
	 * @param keycode the key to be checked
	 * @return whether the key is pressed
	 * @see javafx.scene.input.KeyCode
	 */
	public static boolean getKeyPressed(KeyCode keycode) {
		return keyPressed.contains(keycode);
	}

	/**
	 * Set whether a key is pressed
	 * 
	 * @param keycode the key to be set
	 * @param pressed whether the key is pressed
	 * @see javafx.scene.input.KeyCode
	 */
	public static void setKeyPressed(KeyCode keycode, boolean pressed) {
		if (pressed) {
			if (!keyPressed.contains(keycode)) {
				keyPressed.add(keycode);
			}
		} else {
			keyPressed.remove(keycode);
		}
	}

	/**
	 * Set the mouse left button to be down
	 */
	public static void mouseLeftDown() {
		isLeftDown = true;
	}

	/**
	 * Set the mouse left button to be up
	 */
	public static void mouseLeftUp() {
		isLeftDown = false;
	}

	/**
	 * Set the mouse right button to be down
	 */
	public static void mouseRightDown() {
		isRightDown = true;
	}

	/**
	 * Set the mouse right button to be up
	 */
	public static void mouseRightUp() {
		isRightDown = false;
	}

	/**
	 * Get whether the mouse left button is down
	 * 
	 * @return whether the mouse left button is down
	 */
	public static boolean isLeftDown() {
		return isLeftDown;
	}

	/**
	 * Get whether the mouse right button is down
	 * 
	 * @return whether the mouse right button is down
	 */
	public static boolean isRightDown() {
		return isRightDown;
	}

	/**
	 * Set the x coordinate of the mouse
	 * 
	 * @param x x coordinate of the mouse
	 */
	public static void setMouseX(double x) {
		mouseX = x;
	}

	/**
	 * Get the x coordinate of the mouse
	 * 
	 * @return x coordinate of the mouse
	 */
	public static double getMouseX() {
		return mouseX;
	}

	/**
	 * Set the y coordinate of the mouse
	 * 
	 * @param y y coordinate of the mouse
	 */
	public static void setMouseY(double y) {
		mouseY = y;
	}

	/**
	 * Get the y coordinate of the mouse
	 * 
	 * @return y coordinate of the mouse
	 */
	public static double getMouseY() {
		return mouseY;
	}

	/**
	 * Set the scroll delta of the mouse
	 * 
	 * @param deltaY scroll delta of the mouse
	 */
	public static void setScrollDeltaY(double deltaY) {
		scrollDeltaY = deltaY;
	}

	/**
	 * Get the scroll delta of the mouse
	 * 
	 * @return scroll delta of the mouse
	 */
	public static double getScrollDeltaY() {
		return scrollDeltaY;
	}

	/**
	 * Update the input utility
	 */
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
