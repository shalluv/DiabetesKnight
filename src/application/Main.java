package application;

import drawing.GameScreen;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import logic.GameLogic;
import sharedObject.RenderableHolder;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class Main extends Application {

	public static final int CANVAS_WIDTH = 800;
	public static final int CANVAS_HEIGHT = 600;

	@Override
	public void start(Stage stage) {
		try {
			StackPane root = new StackPane();
			Scene scene = new Scene(root);
			stage.setTitle("OurGame");
			stage.setScene(scene);

			GameLogic logic = new GameLogic(MapData.mapData);
			GameScreen gameScreen = new GameScreen(CANVAS_WIDTH, CANVAS_HEIGHT);

			root.getChildren().add(gameScreen);
			gameScreen.requestFocus();

			stage.show();

			AnimationTimer animation = new AnimationTimer() {
				@Override
				public void handle(long arg0) {
					gameScreen.drawComponent();
					logic.update();
					RenderableHolder.getInstance().update();
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
