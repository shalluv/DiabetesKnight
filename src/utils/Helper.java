package utils;

import java.awt.geom.Rectangle2D;

import application.Main;
import utils.Constants.BlockConstants;

/**
 * Helper
 * Provides helper functions
 */
public class Helper {
	/**
	 * private constructor to prevent instantiation
	 */
	private Helper() { }

	/**
	 * Check if the entity can move to the specified position
	 * @param x The x position
	 * @param y The y position
	 * @param width The width of the entity
	 * @param height The height of the entity
	 * @return True if the entity can move to the specified position, false otherwise
	 */
	public static boolean CanMoveHere(double x, double y, double width, double height) {
		return (!IsSolid(x, y) && !IsSolid(x + width, y + height) && !IsSolid(x + width, y) && !IsSolid(x, y + height));
	}

	/**
	 * Check if the tile at the specified position is solid
	 * @param x The x position
	 * @param y The y position
	 * @return True if the tile at the specified position is solid, false otherwise
	 */
	private static boolean IsSolid(double x, double y) {
		int[][] mapData = Main.mapManager.getCurrentMap().getMapData();
		if (x < 0 || x >= BlockConstants.SIZE * mapData[0].length)
			return true;
		if (y < 0 || y >= BlockConstants.SIZE * mapData.length)
			return true;

		double xIndex = x / BlockConstants.SIZE;
		double yIndex = y / BlockConstants.SIZE;

		int value = mapData[(int) yIndex][(int) xIndex];

		return value > 0;
	}

	/**
	 * Get the x position of the tile next to the wall
	 * @param hitbox The hitbox of the entity
	 * @param xspeed The x speed of the entity
	 * @return The x position of the tile next to the wall
	 */
	public static float GetEntityXPosNextToWall(Rectangle2D.Double hitbox, double xspeed) {
		if (xspeed > 0) {
			// Right
			int currentTile = (int) (hitbox.getMaxX() / BlockConstants.SIZE);
			int tileXPos = currentTile * BlockConstants.SIZE;
			int xOffset = (int) (BlockConstants.SIZE - hitbox.getWidth());
			return tileXPos + xOffset - 1;
		} else {
			// Left
			int currentTile = (int) (hitbox.getMinX() / BlockConstants.SIZE);
			return currentTile * BlockConstants.SIZE;
		}
	}

	/**
	 * Get the y position of the tile under the roof or above the floor
	 * @param hitbox The hitbox of the entity
	 * @param ypeed The y speed of the entity
	 * @return The y position of the tile under the roof or above the floor
	 */
	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Double hitbox, double ypeed) {
		if (ypeed > 0) {
			// Falling - touching floor
			int currentTile = (int) (hitbox.getMaxY() / BlockConstants.SIZE);
			int tileYPos = currentTile * BlockConstants.SIZE;
			int yOffset = (int) (BlockConstants.SIZE - hitbox.getHeight());
			return tileYPos + yOffset - 1;
		} else {
			// Jumping
			int currentTile = (int) (hitbox.getMinY() / BlockConstants.SIZE);
			return currentTile * BlockConstants.SIZE;
		}

	}

	/**
	 * Check if the entity is on the floor
	 * @param hitbox The hitbox of the entity
	 * @return True if the entity is on the floor, false otherwise
	 */
	public static boolean IsEntityOnFloor(Rectangle2D.Double hitbox) {
		// Check the pixel below bottom-left and bottom-right
		if (!IsSolid(hitbox.getMinX(), hitbox.getMaxY() + 1))
			if (!IsSolid(hitbox.getMaxX(), hitbox.getMaxY() + 1))
				return false;

		return true;

	}
}
