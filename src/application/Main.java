package application;

import drawing.GameScreen;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.GameLogic;
import sharedObject.RenderableHolder;
import static utils.Constants.*;

public class Main extends Application {

	public static GameScreen gameScreen;
	public static GameLogic gameLogic;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		try {
			gameLogic = new GameLogic();
			gameScreen = new GameScreen();

			Scene scene = new Scene(gameScreen, Resolution.WIDTH, Resolution.HEIGHT);
			stage.setTitle("OurGame");
			stage.setScene(scene);
			stage.setResizable(false);

			gameScreen.getCanvas().requestFocus();

			stage.show();

			Thread gameThread = new Thread(new Runnable() {
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
						long currentTime = System.nanoTime();

						deltaU += (currentTime - previousTime) / timePerUpdate;
						deltaF += (currentTime - previousTime) / timePerFrame;
						previousTime = currentTime;

						if (deltaU >= 1) {
							gameLogic.update();
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
}
