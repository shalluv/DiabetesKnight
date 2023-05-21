package application;

import static utils.Constants.FPS;
import static utils.Constants.UPS;

import drawing.GameScreen;
import entity.base.Enemy;
import entity.base.Entity;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.GameLogic;
import maps.MapManager;
import ui.MenuOverlay;
import ui.PauseOverlay;
import utils.Constants.GameState;
import utils.Constants.Resolution;

/**
 * Main
 * The main class of the game
 */
public class Main extends Application {
	/**
	 * private constructor to prevent instantiation
	 */
	private Main() { }

	/**
	 * static instance of the game screen
	 * @see drawing.GameScreen
	 */
	public static GameScreen gameScreen;
	/**
	 * static instance of the game logic
	 * @see logic.GameLogic
	 */
	public static GameLogic gameLogic;
	/**
	 * static instance of the map manager
	 * @see maps.MapManager
	 */
	public static MapManager mapManager;
	/**
	 * game state
	 * @see utils.Constants.GameState
	 */
	public static int gameState;
	/**
	 * static instance of the game stage
	 */
	public static Stage gameStage;
	/**
	 *  game thread
	 * @see java.lang.Thread
	 */
	private Thread gameThread;

	/**
	 * Main
	 * The main method of the application
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Start the game
	 * @param stage the stage
	 * @see javafx.application.Application
	 * @see javafx.stage.Stage
	 */
	@Override
	public void start(Stage stage) {
		try {
			// Initialize the game
			gameState = GameState.MENU;
			mapManager = new MapManager();
			gameLogic = new GameLogic();
			gameScreen = new GameScreen();
			gameStage = stage;

			Scene scene = new Scene(gameScreen, Resolution.WIDTH, Resolution.HEIGHT);
			stage.setTitle("Diabetes Knight");
			stage.setScene(scene);
			stage.setResizable(false);

			gameScreen.getCanvas().requestFocus();

			stage.show();

			// Start the game thread
			gameThread = new Thread(new Runnable() {
				public void run() {
					double timePerFrame = 1_000_000_000 / FPS;
					double timePerUpdate = 1_000_000_000 / UPS;

					long previousTime = System.nanoTime();

					long lastCheck = System.currentTimeMillis();

					double deltaU = 0;
					double deltaF = 0;

					while (true) {
						if (Thread.interrupted())
							break;
						long currentTime = System.nanoTime();

						deltaU += (currentTime - previousTime) / timePerUpdate;
						deltaF += (currentTime - previousTime) / timePerFrame;
						previousTime = currentTime;

						// Update the game
						if (deltaU >= 1) {
							update();
							deltaU--;
						}

						// Draw the game
						if (deltaF >= 1) {
							gameScreen.drawComponent();
							deltaF--;
						}

						if (System.currentTimeMillis() - lastCheck >= 1000) {
							lastCheck = System.currentTimeMillis();
						}
					}
				}
			});

			gameThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update the game
	 */
	public void update() {
		switch (gameState) {
		case GameState.MENU:
			MenuOverlay.update();
			break;
		case GameState.PLAYING:
			gameLogic.update();
			break;
		case GameState.CHANGING_LEVEL:
			gameLogic.update();
			break;
		case GameState.PAUSE:
			PauseOverlay.update();
		default:
			break;
		}
	}

	/**
	 * Stop the game
	 * @throws Exception the exception
	 */
	@Override
	public void stop() throws Exception {
		if (GameLogic.getPlayer().getCurrentWeapon() != null)
			GameLogic.getPlayer().getCurrentWeapon().clearThread();
		GameLogic.getPlayer().clearHyperglycemiaThread();
		for (Entity entity : GameLogic.getGameObjectContainer())
			if (entity instanceof Enemy)
				((Enemy) entity).getWeapon().clearThread();
		gameThread.interrupt();
	}
}
