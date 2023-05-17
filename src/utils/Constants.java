package utils;

public class Constants {
	public static final int FPS = 60;
	public static final int UPS = 200;
	public final static float SCALE = 1f;

	public static class Resolution {
		public static final int WIDTH = 1280;
		public static final int HEIGHT = 960;
	}

	public static class GameState {
		public static final int MENU = 0;
		public static final int PLAYING = 1;
		public static final int PAUSE = 2;
	}

	public static class BlockConstants {
		public static final int DEFAULT_SIZE = 40;
		public static final int SIZE = (int) (DEFAULT_SIZE * SCALE);
	}

	public static class PlayerConstants {
		public static final int INITIAL_X = 50;
		public static final int INITIAL_Y = 600;
		public static final int WIDTH = 40;
		public static final int HEIGHT = 40;
		public static final double MAX_Y_SPEED = 8;
		public static final double BASE_X_SPEED = 3;
		public static final double INITIAL_X_SPEED = 0;
		public static final double INITIAL_Y_SPEED = 8;
		public static final double WEIGHT = 0.2;
		public static final int OFFSET_HITBOX_X = 7;
		public static final int OFFSET_HITBOX_Y = 8;
		public static final int HITBOX_WIDTH_REDUCER = 20;
		public static final int MELEE_ATTACK_RANGE = 12;
		public static final int MELEE_DAMAGE = 10;
		public static final int MELEE_ATTACK_SPEED = 3;
		public static final int MELEE_ATTACK_DELAY = 50;
		public static final int RANGE_ATTACK_DELAY = 500;
		public static final int ATTACK_BOX_HEIGHT = 10;
	}

	public static class EnemyConstants {
		public static final int INITIAL_X = 1200;
		public static final int INITIAL_Y = 600;
		public static final int WIDTH = 40;
		public static final int HEIGHT = 40;
		public static final double MAX_Y_SPEED = 5;
		public static final double BASE_X_SPEED = 1;
		public static final double INITIAL_X_SPEED = 0;
		public static final double INITIAL_Y_SPEED = 5;
		public static final double WEIGHT = 1;
		public static final int OFFSET_HITBOX_Y = 5;
		public static final int MELEE_ATTACK_RANGE = 12;
		public static final int MELEE_DAMAGE = 10;
		public static final int MELEE_ATTACK_SPEED = 3;
		public static final int MELEE_ATTACK_DELAY = 50;
		public static final int SIGHT_SIZE = 800;
		public static final int ATTACK_BOX_HEIGHT = 10;
	}

	public static class BulletConstants {
		public static final int WIDTH = 10;
		public static final int HEIGHT = 5;
		public static final double X_SPEED = 5;
		public static final int DAMAGE = 5;
	}
}
