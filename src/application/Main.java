package application;

import static utils.Constants.FPS;
import static utils.Constants.UPS;

import drawing.GameScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.GameLogic;
import maps.MapManager;
import ui.Menu;
import ui.Pause;
import utils.Constants.GameState;
import utils.Constants.Resolution;

public class Main extends Application {

	public static GameScreen gameScreen;
	public static GameLogic gameLogic;
	public static MapManager mapManager;
	public static int gameState;
	private Thread gameThread;
	public static Menu menu;
	public static Pause pause;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		try {
			gameState = GameState.MENU;
			mapManager = new MapManager();
			gameLogic = new GameLogic();
			gameScreen = new GameScreen();
			menu = new Menu();
			pause = new Pause();

			Scene scene = new Scene(gameScreen, Resolution.WIDTH, Resolution.HEIGHT);
			stage.setTitle("OurGame");
			stage.setScene(scene);
			stage.setResizable(false);

			gameScreen.getCanvas().requestFocus();

			stage.show();

			gameThread = new Thread(new Runnable() {
				public void run() {
					double timePerFrame = 1_000_000_000 / FPS;
					double timePerUpdate = 1_000_000_000 / UPS;

					long previousTime = System.nanoTime();

					int frames = 0;
					int updates = 0;
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

						if (deltaU >= 1) {
							update();
							updates++;
							deltaU--;
						}

						if (deltaF >= 1) {
							gameScreen.drawComponent();
							frames++;
							deltaF--;
						}

						if (System.currentTimeMillis() - lastCheck >= 1000) {
							lastCheck = System.currentTimeMillis();
							System.out.println("FPS: " + frames + " | UPS: " + updates);
							frames = 0;
							updates = 0;
						}
					}
				}
			});

			gameThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
		switch (gameState) {
		case GameState.MENU:
			menu.update();
			break;
		case GameState.PLAYING:
			gameLogic.update();
			break;
		case GameState.PAUSE:
			pause.update();
		default:
			break;
		}
	}

	@Override
	public void stop() throws Exception {
		gameThread.interrupt();
	}
}
