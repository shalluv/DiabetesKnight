package utils;

/**
 * Constants Contains all the constants of the game
 * 
 * @see application.Main
 */
public class Constants {
	/**
	 * Private constructor to prevent instantiation
	 */
	private Constants() {
	}

	/**
	 * Frames per second of the game
	 */
	public static final int FPS = 60;
	/**
	 * Updates per second of the game
	 */
	public static final int UPS = 150;
	/**
	 * Scale of the game
	 */
	public final static float SCALE = 1.5f;

	/**
	 * Resolution Contains all the resolution constants
	 * 
	 * @see application.Main
	 * @see drawing.GameScreen
	 * @see drawing.GameCanvas
	 */
	public static class Resolution {
		/**
		 * Private constructor to prevent instantiation
		 */
		private Resolution() {
		}

		/**
		 * Width of the resolution
		 */
		public static final int WIDTH = 1280;
		/**
		 * Height of the resolution
		 */
		public static final int HEIGHT = 960;
	}

	/**
	 * GameState Contains all the game state constants
	 * 
	 * @see logic.GameLogic
	 */
	public static class GameState {
		/**
		 * Private constructor to prevent instantiation
		 */
		private GameState() {
		}

		/**
		 * Menu state
		 */
		public static final int MENU = 0;
		/**
		 * Playing state
		 */
		public static final int PLAYING = 1;
		/**
		 * Changing level state
		 */
		public static final int CHANGING_LEVEL = 2;
		/**
		 * Pause state
		 */
		public static final int PAUSE = 3;
	}

	/**
	 * UI Contains all the UI constants
	 * 
	 * @see ui.GameOverlay
	 * @see ui.MenuOverlay
	 * @see ui.PauseOverlay
	 */
	public static class UI {
		/**
		 * Private constructor to prevent instantiation
		 */
		private UI() {
		}

		/**
		 * Font size of the UI
		 */
		public static final int FONT_SIZE = (int) Math.min(20 * SCALE, 30);

		/**
		 * GameOverlay Contains all the game overlay constants
		 * 
		 * @see ui.GameOverlay
		 */
		public static class GameOverlay {
			/**
			 * Private constructor to prevent instantiation
			 */
			private GameOverlay() {
			}

			/**
			 * Offset of the HP label in x-axis in the game overlay
			 */
			public static final int OFFSET_HP_X = 30;
			/**
			 * Offset of the HP label in y-axis in the game overlay
			 */
			public static final int OFFSET_HP_Y = 110;
			/**
			 * Offset of the power label in x-axis in the game overlay
			 */
			public static final int OFFSET_POWER_X = 30;
			/**
			 * Offset of the power label in y-axis in the game overlay
			 */
			public static final int OFFSET_POWER_Y = 75;
			/**
			 * Offset of the sugar level label in x-axis in the game overlay
			 */
			public static final int OFFSET_SUGAR_X = 30;
			/**
			 * Offset of the sugar level label in y-axis in the game overlay
			 */
			public static final int OFFSET_SUGAR_Y = 40;
			/**
			 * Offset of the sugar amount label in x-axis in the game overlay
			 */
			public static final int OFFSET_SUGAR_AMOUNT_X = 170;
			/**
			 * Offset of the inventory in y-axis in the game overlay
			 */
			public static final int OFFSET_INVENTORY_Y = 80;
			/**
			 * Item size in the inventory in the game overlay
			 */
			public static final int ITEM_SIZE = (int) Math.min(36 * SCALE, 54);
			/**
			 * Gap between items in the inventory in the game overlay
			 */
			public static final int INVENTORY_GAP = (int) Math.min(50 * SCALE, 75);
			/**
			 * Padding of the inventory in the game overlay
			 */
			public static final int INVENTORY_PADDING = (int) Math.min(2 * SCALE, 3);
			/**
			 * Offset of the current item name in y-axis in the game overlay
			 */
			public static final int CURRENT_ITEM_NAME_OFFSET_Y = OFFSET_INVENTORY_Y + ITEM_SIZE + INVENTORY_PADDING * 2;
		}
	}

	/**
	 * BlockConstants Contains all the block constants
	 * 
	 * @see maps.Map
	 */
	public static class BlockConstants {
		/**
		 * Private constructor to prevent instantiation
		 */
		private BlockConstants() {
		}

		/**
		 * Default size of the block
		 */
		public static final int DEFAULT_SIZE = 32;
		/**
		 * Size of the block after scaling
		 */
		public static final int SIZE = (int) (DEFAULT_SIZE * SCALE);
	}

	/**
	 * Directions Contains all the direction constants
	 * 
	 * @see entity.base.Entity
	 */
	public static class Directions {
		/**
		 * Private constructor to prevent instantiation
		 */
		private Directions() {
		}

		/**
		 * Left direction
		 */
		public static final int LEFT = 0;
		/**
		 * Up direction
		 */
		public static final int UP = 1;
		/**
		 * Right direction
		 */
		public static final int RIGHT = 2;
		/**
		 * Down direction
		 */
		public static final int DOWN = 3;
	}

	/**
	 * AttackState Contains all the attack state constants
	 * 
	 * @see entity.base.Enemy
	 * @see entity.Player
	 */
	public static class AttackState {
		/**
		 * Private constructor to prevent instantiation
		 */
		private AttackState() {
		}

		/**
		 * Ready state
		 */
		public static final int READY = 0;
		/**
		 * Attacking in progress state
		 */
		public static final int IN_PROGRESS = 1;
		/**
		 * Attacking on cooldown state
		 */
		public static final int ON_COOLDOWN = 2;
		/**
		 * Reloading state
		 */
		public static final int ON_RELOAD = 3;
	}

	/**
	 * Weapon Contains all the weapon constants
	 * 
	 * @see item.Weapon
	 */
	public static class Weapon {
		/**
		 * Private constructor to prevent instantiation
		 */
		private Weapon() {
		}

		/**
		 * SpearConstants Contains all the spear constants
		 * 
		 * @see item.derived.Spear
		 */
		public static class SpearConstants {
			/**
			 * Private constructor to prevent instantiation
			 */
			private SpearConstants() {
			}

			/**
			 * Spear's base attack range
			 */
			public static final int BASE_ATTACK_RANGE = (int) (14 * SCALE);
			/**
			 * Spear's base attack damage
			 */
			public static final int BASE_DAMAGE = 20;
			/**
			 * Spear's base attack speed
			 */
			public static final int BASE_ATTACK_SPEED = (int) (2 * SCALE);
			/**
			 * Spear's attack delay
			 */
			public static final int ATTACK_DELAY = 50;
			/**
			 * Spear's base attack box height
			 */
			public static final int ATTACK_BOX_HEIGHT = (int) (5 * SCALE);
			/**
			 * Spear's base x-axis speed multiplier
			 */
			public static final double BASE_X_SPEED_MULTIPLIER = 0.82;
			/**
			 * Spear's base y-axis speed multiplier
			 */
			public static final double BASE_Y_SPEED_MULTIPLIER = 0.987;
			/**
			 * Spear's ultimate attack range
			 */
			public static final int ULTIMATE_ATTACK_RANGE = (int) (24 * SCALE);
			/**
			 * Spear's ultimate attack damage
			 */
			public static final int ULTIMATE_DAMAGE = 35;
			/**
			 * Spear's ultimate x-axis speed multiplier
			 */
			public static final double ULTIMATE_X_SPEED_MULTIPLIER = 1.1;
			/**
			 * Spear's ultimate y-axis speed multiplier
			 */
			public static final double ULTIMATE_Y_SPEED_MULTIPLIER = 1;
			/**
			 * Spear's ultimate attack speed
			 */
			public static final int ULTIMATE_ATTACK_SPEED = (int) (4 * SCALE);
			/**
			 * Spear's ultimate duration
			 */
			public static final int ULTIMATE_DURATION = 20000;
			/**
			 * Spear's ultimate cost
			 */
			public static final int ULTIMATE_COST = 50;
			/**
			 * Spear's ultimate animation left x-axis offset
			 */
			public static final int ULTIMATE_ANIMATION_LEFT_X_OFFSET = 5;
			/**
			 * Spear's ultimate animation right x-axis offset
			 */
			public static final int ULTIMATE_ANIMATION_RIGHT_X_OFFSET = 10;
		}

		/**
		 * SwordConstants Contains all the sword constants
		 * 
		 * @see item.derived.Sword
		 */
		public static class SwordConstants {
			/**
			 * Private constructor to prevent instantiation
			 */
			private SwordConstants() {
			}

			/**
			 * Sword's base attack range
			 */
			public static final int BASE_ATTACK_RANGE = (int) (8 * SCALE);
			/**
			 * Sword's base attack damage
			 */
			public static final int BASE_DAMAGE = 5;
			/**
			 * Sword's maximum swing
			 */
			public static final int MAXIMUM_SWING = 180;
			/**
			 * Sword's swing speed
			 */
			public static final int SWING_SPEED = 30;
			/**
			 * Sword's attack delay
			 */
			public static final int ATTACK_DELAY = 30;
			/**
			 * Sword's base attack box height
			 */
			public static final int ATTACK_BOX_HEIGHT = (int) (4 * SCALE);
			/**
			 * Sword's base x-axis speed multiplier
			 */
			public static final double BASE_X_SPEED_MULTIPLIER = 1;
			/**
			 * Sword's base y-axis speed multiplier
			 */
			public static final double BASE_Y_SPEED_MULTIPLIER = 1;
			/**
			 * Sword's ultimate x-axis speed multiplier
			 */
			public static final double ULTIMATE_X_SPEED_MULTIPLIER = 1.3;
			/**
			 * Sword's ultimate y-axis speed multiplier
			 */
			public static final double ULTIMATE_Y_SPEED_MULTIPLIER = 1.005;
			/**
			 * Sword's ultimate attack damage
			 */
			public static final int ULTIMATE_DAMAGE = 10;
			/**
			 * Sword's ultimate attack range
			 */
			public static final int ULTIMATE_ATTACK_RANGE = (int) (16 * SCALE);
			/**
			 * Sword's ultimate attack delay
			 */
			public static final int AFTER_ATTACK_DELAY = 150;
			/**
			 * Sword's ultimate duration
			 */
			public static final int ULTIMATE_DURATION = 15000;
			/**
			 * Sword's ultimate cost
			 */
			public static final int ULTIMATE_COST = 30;

			/**
			 * Animations Contains all the sword animation constants
			 */
			public static class Animations {
				/**
				 * Private constructor to prevent instantiation
				 */
				private Animations() {
				}

				/**
				 * Sword's amount of animation states
				 */
				public static final int ANIMATION_STATE_COUNT = 3;
				/**
				 * Sword's sprite size
				 */
				public static final int SPRITE_SIZE = 32;
				/**
				 * Sword's animation speed
				 */
				public static final int ANIMATION_SPEED = 2;
				/**
				 * Sword's animation offset y-axis
				 */
				public static final int ANIMATION_OFFSET_Y = 10;
				/**
				 * Sword's animation height offset
				 */
				public static final int ANIMATION_HEIGHT_OFFSET = 20;

				/**
				 * Sword's animation idle state
				 */
				public static final int IDLE = 0;
				/**
				 * Sword's animation swing state
				 */
				public static final int SWING_ANIMATION = 1;
				/**
				 * Sword's animation swing done state
				 */
				public static final int SWING_DONE = 2;
			}
		}

		/**
		 * GunConstants Contains all the gun constants
		 * 
		 * @see item.derived.Gun
		 */
		public static class GunConstants {
			/**
			 * Private constructor to prevent instantiation
			 */
			private GunConstants() {
			}

			/**
			 * Gun's base attack delay
			 */
			public static final int BASE_ATTACK_DELAY = 300;
			/**
			 * Gun's maximum ammo
			 */
			public static final int BASE_MAX_AMMO = 6;
			/**
			 * Gun's base reload delay
			 */
			public static final int BASE_RELOAD_DELAY = 1000;
			/**
			 * Gun's base x-axis speed multiplier
			 */
			public static final double BASE_X_SPEED_MULTIPLIER = 1;
			/**
			 * Gun's base y-axis speed multiplier
			 */
			public static final double BASE_Y_SPEED_MULTIPLIER = 1;
			/**
			 * Gun's ultimate attack delay
			 */
			public static final int ULTIMATE_ATTACK_DELAY = 100;
			/**
			 * Gun's ultimate reload delay
			 */
			public static final int ULTIMATE_RELOAD_DELAY = 400;
			/**
			 * Gun's ultimate maximum ammo
			 */
			public static final int ULTIMATE_MAX_AMMO = 15;
			/**
			 * Gun's ultimate duration
			 */
			public static final int ULTIMATE_DURATION = 10000;
			/**
			 * Gun's ultimate cost
			 */
			public static final int ULTIMATE_COST = 40;
		}

		/**
		 * BulletConstants Contains all the bullet constants
		 * 
		 * @see entity.Bullet
		 */
		public static class BulletConstants {
			/**
			 * Private constructor to prevent instantiation
			 */
			private BulletConstants() {
			}

			/**
			 * Bullet's width
			 */
			public static final int WIDTH = (int) (16 * SCALE);
			/**
			 * Bullet's height
			 */
			public static final int HEIGHT = (int) (16 * SCALE);
			/**
			 * Bullet's speed
			 */
			public static final double SPEED = 3 * SCALE;
			/**
			 * Bullet's damage
			 */
			public static final int DAMAGE = 10;
		}
	}

	/**
	 * PlayerConstants Contains all the player constants
	 * 
	 * @see entity.Player
	 */
	public static class PlayerConstants {
		/**
		 * Private constructor to prevent instantiation
		 */
		private PlayerConstants() {
		}

		/**
		 * Player's width
		 */
		public static final int WIDTH = (int) (32 * SCALE);
		/**
		 * Player's height
		 */
		public static final int HEIGHT = (int) (32 * SCALE);
		/**
		 * Player's maximum y-axis speed
		 */
		public static final double MAX_Y_SPEED = 5 * SCALE;
		/**
		 * Player's base x-axis speed
		 */
		public static final double BASE_X_SPEED = 2.5 * SCALE;
		/**
		 * Player's initial x-axis speed
		 */
		public static final double INITIAL_X_SPEED = 0 * SCALE;
		/**
		 * Player's initial y-axis speed
		 */
		public static final double INITIAL_Y_SPEED = 4 * SCALE;
		/**
		 * Player's weight
		 */
		public static final double WEIGHT = 0.12 * SCALE;
		/**
		 * Player's inventory size
		 */
		public static final int INVENTORY_SIZE = 10;
		/**
		 * Player's initial health
		 */
		public static final int INITIAL_MAX_HEALTH = 999;
		/**
		 * Player's initial power
		 */
		public static final int INITIAL_POWER = 0;
		/**
		 * Player's hypoglycemia sugar level threshold
		 */
		public static final int HYPOGLYCEMIA_SUGAR_LEVEL = 0;
		/**
		 * Player's initial sugar level
		 */
		public static final int INITIAL_SUGAR_LEVEL = 55;
		/**
		 * Player's hyperglycemia sugar level threshold
		 */
		public static final int HYPERGLYCEMIA_SUGAR_LEVEL = 80;
		/**
		 * Player's sugar level decrease rate when being hit
		 */
		public static final int HIT_SUGAR_DECREASED_AMOUNT = 2;
		/**
		 * Player's action delay when being in hyperglycemia state
		 */
		public static final int HYPERGLYCEMIA_DELAY = 1000;
		/**
		 * Player's attack damage when being in hyperglycemia state
		 */
		public static final int HYPERGLYCEMIA_DAMAGE = 3;
		/**
		 * Player's x-axis speed multiplier when being in hypoglycemia state
		 */
		public static final double HYPOGLYCEMIA_X_SPEED_MULTIPLIER = 0.8;
		/**
		 * Player's y-axis speed multiplier when being in hypoglycemia state
		 */
		public static final double HYPOGLYCEMIA_Y_SPEED_MULTIPLIER = 0.98;

		/**
		 * HealthState Contains all the health states of the player
		 * 
		 * @see entity.Player
		 */
		public static class HealthState {
			/**
			 * Private constructor to prevent instantiation
			 */
			private HealthState() {
			}

			/**
			 * Hypoglycemia state
			 */
			public static final int HYPOGLYCEMIA = 0;
			/**
			 * Healthy state
			 */
			public static final int HEALTHY = 1;
			/**
			 * Hyperglycemia state
			 */
			public static final int HYPERGLYCEMIA = 2;
		}

		/**
		 * Animations Contains all the player animations constants
		 * 
		 * @see entity.Player
		 */
		public static class Animations {
			/**
			 * Private constructor to prevent instantiation
			 */
			private Animations() {
			}

			/**
			 * Player's weapon offset x-axis
			 */
			public static final int WEAPON_OFFSET_X = (int) (12 * SCALE);
			/**
			 * Player's weapon offset y-axis
			 */
			public static final int WEAPON_OFFSET_Y = (int) (12 * SCALE);

			/**
			 * Player's amount of animation states
			 */
			public static final int ANIMATION_STATE_COUNT = 3;
			/**
			 * Player's sprite size
			 */
			public static final int SPRITE_SIZE = 32;
			/**
			 * Player's animation speed
			 */
			public static final int ANIMATION_SPEED = 6;

			/**
			 * Player's idle state
			 */
			public static final int IDLE = 0;
			/**
			 * Player's running state
			 */
			public static final int RUNNING = 1;
			/**
			 * Player's jumping state
			 */
			public static final int JUMPING = 2;

			/**
			 * Player's idle state frames amount
			 */
			public static final int IDLE_FRAMES_COUNT = 4;
			/**
			 * Player's running state frames amount
			 */
			public static final int RUNNING_FRAMES_COUNT = 4;
			/**
			 * Player's jumping state frames amount
			 */
			public static final int JUMPING_FRAMES_COUNT = 4;
		}
	}

	/**
	 * EnemyConstants Contains all the enemy constants
	 * 
	 * @see entity.base.Enemy
	 * @see entity.base.MeleeEnemy
	 * @see entity.base.RangedEnemy
	 */
	public static class EnemyConstants {
		/**
		 * private constructor to prevent instantiation
		 */
		private EnemyConstants() {
		}

		/**
		 * SwordmenConstants Contains all the swordmen constants
		 * 
		 * @see entity.Swordman
		 */
		public static class SwordmenConstants {
			/**
			 * private constructor to prevent instantiation
			 */
			private SwordmenConstants() {
			}

			/**
			 * Swordman's width
			 */
			public static final int WIDTH = (int) (32 * SCALE);
			/**
			 * Swordman's height
			 */
			public static final int HEIGHT = (int) (32 * SCALE);
			/**
			 * Swordman's max y-axis speed
			 */
			public static final double MAX_Y_SPEED = 5 * SCALE;
			/**
			 * Swordman's base x-axis speed
			 */
			public static final double BASE_X_SPEED = 1 * SCALE;
			/**
			 * Swordman's initial x-axis speed
			 */
			public static final double INITIAL_X_SPEED = 0 * SCALE;
			/**
			 * Swordman's initial y-axis speed
			 */
			public static final double INITIAL_Y_SPEED = 5 * SCALE;
			/**
			 * Swordman's weight
			 */
			public static final double WEIGHT = 1 * SCALE;
			/**
			 * Swordman's sight size
			 */
			public static final int SIGHT_SIZE = (int) (400 * SCALE);
			/**
			 * Swordman's initial max health
			 */
			public static final int INITIAL_MAX_HEALTH = 25;

			/**
			 * Animations Contains all the swordmen animations constants
			 * 
			 * @see entity.Swordman
			 */
			public static class Animations {
				/**
				 * Private constructor to prevent instantiation
				 */
				private Animations() {
				}

				/**
				 * Swordman's amount of animation states
				 */
				public static final int ANIMATION_STATE_COUNT = 1;
				/**
				 * Swordman's sprite size
				 */
				public static final int SPRITE_SIZE = 32;
				/**
				 * Swordman's animation speed
				 */
				public static final int ANIMATION_SPEED = 6;

				/**
				 * Swordman's idle state
				 */
				public static final int IDLE = 0;

				/**
				 * Swordman's idle state frames amount
				 */
				public static final int IDLE_FRAMES_COUNT = 4;
			}
		}

		/**
		 * SpearmenConstants Contains all the spearmen constants
		 * 
		 * @see entity.Spearman
		 */
		public static class SpearmenConstants {
			/**
			 * private constructor to prevent instantiation
			 */
			private SpearmenConstants() {
			}

			/**
			 * Spearmen's width
			 */
			public static final int WIDTH = (int) (32 * SCALE);
			/**
			 * Spearmen's height
			 */
			public static final int HEIGHT = (int) (32 * SCALE);
			/**
			 * Spearmen's max y-axis speed
			 */
			public static final double MAX_Y_SPEED = 5 * SCALE;
			/**
			 * Spearmen's base x-axis speed
			 */
			public static final double BASE_X_SPEED = 1 * SCALE;
			/**
			 * Spearmen's initial x-axis speed
			 */
			public static final double INITIAL_X_SPEED = 0 * SCALE;
			/**
			 * Spearmen's initial y-axis speed
			 */
			public static final double INITIAL_Y_SPEED = 5 * SCALE;
			/**
			 * Spearmen's weight
			 */
			public static final double WEIGHT = 1 * SCALE;
			/**
			 * Spearmen's sight size
			 */
			public static final int SIGHT_SIZE = (int) (400 * SCALE);
			/**
			 * Spearmen's initial max health
			 */
			public static final int INITIAL_MAX_HEALTH = 50;

			/**
			 * Animations Contains all the spearmen animations constants
			 * 
			 * @see entity.Spearman
			 */
			public static class Animations {
				/**
				 * Private constructor to prevent instantiation
				 */
				private Animations() {
				}

				/**
				 * Spearmen's amount of animation states
				 */
				public static final int ANIMATION_STATE_COUNT = 1;
				/**
				 * Spearmen's sprite size
				 */
				public static final int SPRITE_SIZE = 32;
				/**
				 * Spearmen's animation speed
				 */
				public static final int ANIMATION_SPEED = 6;

				/**
				 * Spearmen's idle state
				 */
				public static final int IDLE = 0;

				/**
				 * Spearmen's idle state frames amount
				 */
				public static final int IDLE_FRAMES_COUNT = 4;
			}
		}

		/**
		 * GunnerConstants Contains all the gunner constants
		 * 
		 * @see entity.Gunner
		 */
		public static class GunnerConstants {
			/**
			 * private constructor to prevent instantiation
			 */
			private GunnerConstants() {
			}

			/**
			 * Gunner's width
			 */
			public static final int WIDTH = (int) (32 * SCALE);
			/**
			 * Gunner's height
			 */
			public static final int HEIGHT = (int) (32 * SCALE);
			/**
			 * Gunner's max y-axis speed
			 */
			public static final double MAX_Y_SPEED = 5 * SCALE;
			/**
			 * Gunner's base x-axis speed
			 */
			public static final double BASE_X_SPEED = 1 * SCALE;
			/**
			 * Gunner's initial x-axis speed
			 */
			public static final double INITIAL_X_SPEED = 0 * SCALE;
			/**
			 * Gunner's initial y-axis speed
			 */
			public static final double INITIAL_Y_SPEED = 5 * SCALE;
			/**
			 * Gunner's weight
			 */
			public static final double WEIGHT = 1 * SCALE;
			/**
			 * Gunner's sight size
			 */
			public static final int SIGHT_SIZE = (int) (1000 * SCALE);
			/**
			 * Gunner's initial max health
			 */
			public static final int INITIAL_MAX_HEALTH = 20;
			/**
			 * Gunner's attack range
			 */
			public static final int ATTACK_RANGE = (int) (400 * SCALE);

			/**
			 * Animations Contains all the gunner animations constants
			 * 
			 * @see entity.Gunner
			 */
			public static class Animations {
				/**
				 * Private constructor to prevent instantiation
				 */
				private Animations() {
				}

				/**
				 * Gunner's amount of animation states
				 */
				public static final int ANIMATION_STATE_COUNT = 2;
				/**
				 * Gunner's sprite size
				 */
				public static final int SPRITE_SIZE = 32;
				/**
				 * Gunner's animation speed
				 */
				public static final int ANIMATION_SPEED = 6;

				/**
				 * Gunner's idle state
				 */
				public static final int IDLE = 0;
				/**
				 * Gunner's attack on cooldown state
				 */
				public static final int ATTACK_COOLDOWN = 1;

				/**
				 * Gunner's idle state frames amount
				 */
				public static final int IDLE_FRAMES_COUNT = 1;
				/**
				 * Gunner's attack on cooldown state frames amount
				 */
				public static final int ATTACK_COOLDOWN_FRAMES_COUNT = 1;
			}
		}
	}

	/**
	 * DoorConstants Contains all the door constants
	 * 
	 * @see entity.Door
	 */
	public static class DoorConstants {
		/**
		 * private constructor to prevent instantiation
		 */
		private DoorConstants() {
		}

		/**
		 * Door's width
		 */
		public static final int WIDTH = (int) (32 * SCALE);
		/**
		 * Door's height
		 */
		public static final int HEIGHT = (int) (32 * SCALE);

		/**
		 * Animations Contains all the door animations constants
		 * 
		 * @see entity.Door
		 */
		public static class Animations {
			/**
			 * Private constructor to prevent instantiation
			 */
			private Animations() {
			}

			/**
			 * Door's amount of animation states
			 */
			public static final int ANIMATION_STATE_COUNT = 2;
			/**
			 * Door's sprite size
			 */
			public static final int SPRITE_SIZE = 32;
			/**
			 * Door's animation speed
			 */
			public static final int ANIMATION_SPEED = 7;
			/**
			 * Door's animation frames count
			 */
			public static final int FRAMES_COUNT = 3;
		}
	}

	/**
	 * SugarConstants Contains all the sugar constants
	 * 
	 * @see item.derived.Sugar
	 */
	public static class SugarConstants {
		/**
		 * private constructor to prevent instantiation
		 */
		private SugarConstants() {
		}

		/**
		 * Sugar's sugar level increase amount
		 */
		public static final int SUGAR_AMOUNT = 10;
		/**
		 * Sugar's power increase amount
		 */
		public static final int POWER_AMOUNT = 10;
	}

	/**
	 * InsulinConstants Contains all the insulin constants
	 * 
	 * @see item.derived.Insulin
	 */
	public static class InsulinConstants {
		/**
		 * private constructor to prevent instantiation
		 */
		private InsulinConstants() {
		}

		/**
		 * Insulin's sugar level decrease amount
		 */
		public static final int SUGAR_AMOUNT = -15;
		/**
		 * Insulin's power decrease amount
		 */
		public static final int POWER_AMOUNT = -5;
	}

	/**
	 * DroppedItemConstants Contains all the dropped item constants
	 * 
	 * @see entity.DroppedItem
	 */
	public static class DroppedItemConstants {
		/**
		 * private constructor to prevent instantiation
		 */
		private DroppedItemConstants() {
		}

		/**
		 * Dropped item's default size
		 */
		public static final int DEFAULT_SIZE = 24;
		/**
		 * Dropped item's size
		 */
		public static final int SIZE = (int) (DEFAULT_SIZE * SCALE);
	}

	/**
	 * InputConstants Contains all the input constants
	 * 
	 * @see input.InputUtility
	 */
	public static class InputConstants {
		/**
		 * private constructor to prevent instantiation
		 */
		private InputConstants() {
		}

		/**
		 * Cursor's offset x-axis
		 */
		public static final int CURSOR_OFFSET_X = 8;
		/**
		 * Cursor's offset y-axis
		 */
		public static final int CURSOR_OFFSET_Y = 30;
	}

	/**
	 * Maps Contains all the maps constants
	 * 
	 * @see maps.Map
	 * @see maps.MapManager
	 */
	public static class Maps {
		/**
		 * private constructor to prevent instantiation
		 */
		private Maps() {
		}

		/**
		 * Map's amount of tiles variants
		 */
		public static final int TILES_AMOUNT = 21;
	}
}
