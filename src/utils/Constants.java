package utils;

public class Constants {
	public static final int FPS = 120;
	public static final int UPS = 200;

	public static class Resolution {
		public static final int WIDTH = 1280;
		public static final int HEIGHT = 960;
	}

	public static class BlockConstants {
		public static final int WIDTH = 40;
		public static final int HEIGHT = 40;
	}

	public static class PlayerConstants {
		public static final int ORIGIN_X = 50;
		public static final int ORIGIN_Y = 600;
		public static final int WIDTH = 45;
		public static final int HEIGHT = 45;
		public static final int MAX_Y_SPEED = 16;
		public static final int BASE_X_SPEED = 5;
		public static final int ORIGIN_X_SPEED = 0;
		public static final int ORIGIN_Y_SPEED = 32;
		public static final int WEIGHT = 1;
		public static final int OFFSET_HITBOX_X = 8;
		public static final int OFFSET_HITBOX_Y = 8;
		public static final int HITBOX_WIDTH_REDUCER = 20;
	}

	public static class EnemyConstants {
		public static final int ORIGIN_X = 1200;
		public static final int ORIGIN_Y = 600;
		public static final int WIDTH = 32;
		public static final int HEIGHT = 20;
		public static final int MAX_Y_SPEED = 16;
		public static final int BASE_X_SPEED = 1;
		public static final int ORIGIN_X_SPEED = 0;
		public static final int ORIGIN_Y_SPEED = 32;
		public static final int WEIGHT = 1;
		public static final int OFFSET_HITBOX_Y = 5;
		public static final int ATTACK_RANGE = 8;
		public static final int DAMAGE = 50;
		public static final int ATTACK_DELAY = 1500;
		public static final int AFTER_ATTACK_DELAY = 800;
		public static final int SIGHT_SIZE = 400;
	}
}
