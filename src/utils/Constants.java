package utils;

public class Constants {
	public static final int FPS = 60;
	public static final int UPS = 150;
	public final static float SCALE = 1.5f;

	public static class Resolution {
		public static final int WIDTH = 1280;
		public static final int HEIGHT = 960;
	}

	public static class GameState {
		public static final int MENU = 0;
		public static final int PLAYING = 1;
		public static final int PAUSE = 2;
	}

	public static class UI {
		public static final int FONT_SIZE = (int) Math.min(20 * SCALE, 30);

		public static class GameOverlay {
			public static final int OFFSET_HP_X = 30;
			public static final int OFFSET_HP_Y = 80;
			public static final int OFFSET_POWER_X = 30;
			public static final int OFFSET_POWER_Y = 50;
			public static final int OFFSET_INVENTORY_Y = 80;
			public static final int ITEM_SIZE = (int) Math.min(36 * SCALE, 54);
			public static final int INVENTORY_GAP = (int) Math.min(50 * SCALE, 75);
			public static final int INVENTORY_PADDING = (int) Math.min(2 * SCALE, 3);
		}
	}

	public static class BlockConstants {
		public static final int DEFAULT_SIZE = 32;
		public static final int SIZE = (int) (DEFAULT_SIZE * SCALE);
	}

	public static class Directions {
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}

	public static class AttackState {
		public static final int READY = 0;
		public static final int MELEE_IN_PROGRESS = 1;
		public static final int MELEE_ON_COOLDOWN = 2;
		public static final int MELEE_HIT = 3;
		public static final int RANGED_IN_PROGRESS = 4;
		public static final int RANGED_ON_COOLDOWN = 5;
	}

	public static class Weapon {
		public static class SpearConstants {
			public static final int ATTACK_RANGE = (int) (12 * SCALE);
			public static final int DAMAGE = 10;
			public static final int ATTACK_SPEED = (int) (3 * SCALE);
			public static final int ATTACK_DELAY = 50;
			public static final int ATTACK_BOX_HEIGHT = (int) (5 * SCALE);
		}

		public static class GunConstants {
			public static final int ATTACK_DELAY = 1000;
		}

		public static class BulletConstants {
			public static final int WIDTH = (int) (16 * SCALE);
			public static final int HEIGHT = (int) (16 * SCALE);
			public static final double SPEED = 3 * SCALE;
			public static final int DAMAGE = 5;
		}
	}

	public static class PlayerConstants {
		public static final int WIDTH = (int) (32 * SCALE);
		public static final int HEIGHT = (int) (32 * SCALE);
		public static final double MAX_Y_SPEED = 5 * SCALE;
		public static final double BASE_X_SPEED = 2.5 * SCALE;
		public static final double INITIAL_X_SPEED = 0 * SCALE;
		public static final double INITIAL_Y_SPEED = 4 * SCALE;
		public static final double WEIGHT = 0.12 * SCALE;
		public static final int INVENTORY_SIZE = 10;
		public static final int INITIAL_MAX_HEALTH = 200;
		public static final int INITIAL_MAX_POWER = 100;

		public static class Animations {
			public static final int ANIMATION_STATE_COUNT = 3;
			public static final int SPRITE_SIZE = 32;
			public static final int ANIMATION_SPEED = 6;

			public static final int IDLE = 0;
			public static final int RUNNING = 1;
			public static final int JUMPING = 2;

			public static final int IDLE_FRAMES_COUNT = 4;
			public static final int RUNNING_FRAMES_COUNT = 4;
			public static final int JUMPING_FRAMES_COUNT = 4;
		}
	}

	public static class EnemyConstants {
		public static class MeleeConstants {
			public static final int WIDTH = (int) (32 * SCALE);
			public static final int HEIGHT = (int) (32 * SCALE);
			public static final double MAX_Y_SPEED = 5 * SCALE;
			public static final double BASE_X_SPEED = 1 * SCALE;
			public static final double INITIAL_X_SPEED = 0 * SCALE;
			public static final double INITIAL_Y_SPEED = 5 * SCALE;
			public static final double WEIGHT = 1 * SCALE;
			public static final int SIGHT_SIZE = (int) (400 * SCALE);
			public static final int INITIAL_MAX_HEALTH = 30;

			public static class Animations {
				public static final int ANIMATION_STATE_COUNT = 1;
				public static final int SPRITE_SIZE = 32;
				public static final int ANIMATION_SPEED = 6;

				public static final int IDLE = 0;

				public static final int IDLE_FRAMES_COUNT = 4;
			}
		}

		public static class RangedConstants {
			public static final int WIDTH = (int) (32 * SCALE);
			public static final int HEIGHT = (int) (32 * SCALE);
			public static final double MAX_Y_SPEED = 5 * SCALE;
			public static final double BASE_X_SPEED = 1 * SCALE;
			public static final double INITIAL_X_SPEED = 0 * SCALE;
			public static final double INITIAL_Y_SPEED = 5 * SCALE;
			public static final double WEIGHT = 1 * SCALE;
			public static final int SIGHT_SIZE = (int) (1000 * SCALE);
			public static final int INITIAL_MAX_HEALTH = 20;
			public static final int ATTACK_RANGE = (int) (400 * SCALE);

			public static class Animations {
				public static final int ANIMATION_STATE_COUNT = 2;
				public static final int SPRITE_SIZE = 32;
				public static final int ANIMATION_SPEED = 6;

				public static final int IDLE = 0;
				public static final int ATTACK_COOLDOWN = 1;

				public static final int IDLE_FRAMES_COUNT = 1;
				public static final int ATTACK_COOLDOWN_FRAMES_COUNT = 1;
			}
		}
	}

	public static class DroppedItemConstants {
		public static final int DEFAULT_SIZE = 24;
		public static final int SIZE = (int) (DEFAULT_SIZE * SCALE);
	}

	public static class InputConstants {
		public static final int CURSOR_OFFSET_X = 8;
		public static final int CURSOR_OFFSET_Y = 30;
	}
}
