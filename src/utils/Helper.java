package utils;

import java.awt.geom.Rectangle2D;

import application.Main;
import utils.Constants.BlockConstants;

public class Helper {

	public static boolean CanMoveHere(double x, double y, double width, double height) {
		return (!IsSolid(x, y) && !IsSolid(x + width, y + height) && !IsSolid(x + width, y) && !IsSolid(x, y + height));
	}

	private static boolean IsSolid(double x, double y) {
		int[][] mapData = Main.mapManager.getCurrentMap().getMapData();
		if (x < 0 || x >= BlockConstants.SIZE * 80)
			return true;
		if (y < 0 || y >= BlockConstants.SIZE * 25)
			return true;

		double xIndex = x / BlockConstants.SIZE;
		double yIndex = y / BlockConstants.SIZE;

		int value = mapData[(int) yIndex][(int) xIndex];

		return value > 0;
	}

	public static float GetEntityXPosNextToWall(Rectangle2D.Double hitbox, double xspeed) {
		if (xspeed > 0) {
			// Right
			int currentTile = (int) (hitbox.getMaxX() / BlockConstants.SIZE);
			int tileXPos = currentTile * BlockConstants.SIZE;
			int xOffset = (int) (40 - hitbox.getWidth());
			return tileXPos + xOffset - 1;
		} else {
			// Left
			int currentTile = (int) (hitbox.getMinX() / BlockConstants.SIZE);
			return currentTile * BlockConstants.SIZE;
		}
	}

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

	public static boolean IsEntityOnFloor(Rectangle2D.Double hitbox) {
		// Check the pixel below bottom-left and bottom-right
		if (!IsSolid(hitbox.getMinX(), hitbox.getMaxY() + 1))
			if (!IsSolid(hitbox.getMaxX(), hitbox.getMaxY() + 1))
				return false;

		return true;

	}
}
