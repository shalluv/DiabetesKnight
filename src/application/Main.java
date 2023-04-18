package application;

import drawing.GameScreen;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.GameLogic;
import sharedObject.RenderableHolder;

public class Main extends Application {

	public static final int FPS = 120;
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

			Scene scene = new Scene(gameScreen, 1280, 960);
			stage.setTitle("OurGame");
			stage.setScene(scene);
			stage.setResizable(false);

			gameScreen.getCanvas().requestFocus();

			stage.show();

			AnimationTimer animation = new AnimationTimer() {
				private long lastUpdate = 0;

				@Override
				public void handle(long now) {
					if (now - lastUpdate >= 1_000_000_000 / FPS) {
						gameScreen.drawComponent();
						gameLogic.update();
						RenderableHolder.getInstance().update();
						lastUpdate = now;
					}
				}
			};
			animation.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
