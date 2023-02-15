package application;

import drawing.GameScreen;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import logic.GameLogic;
import sharedObject.RenderableHolder;
import javafx.scene.Scene;

public class Main extends Application {

	public static final int FPS = 120;
	public static GameScreen gameScreen = new GameScreen();

	@Override
	public void start(Stage stage) {
		try {
			Scene scene = new Scene(gameScreen, 1280, 960);
			stage.setTitle("OurGame");
			stage.setScene(scene);
			stage.setResizable(false);

			GameLogic logic = new GameLogic(MapData.data);
			gameScreen.getCanvas().requestFocus();
			
			stage.show();

			AnimationTimer animation = new AnimationTimer() {
				private long lastUpdate = 0;

				@Override
				public void handle(long now) {
					if (now - lastUpdate >= 1_000_000_000 / FPS) {
						gameScreen.drawComponent();
						logic.update();
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

	public static void main(String[] args) {
		launch(args);
	}
}
