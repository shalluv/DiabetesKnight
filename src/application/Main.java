package application;

import drawing.GameScreen;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import logic.GameLogic;
import sharedObject.RenderableHolder;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

public class Main extends Application {

	private int board_width;
	private int board_height;

	@Override
	public void start(Stage stage) {
		try {
			StackPane root = new StackPane();
			Scene scene = new Scene(root);
			stage.setTitle("OurGame");
			stage.setScene(scene);

			board_width = MapData.mapData[0].length() * 40;
			board_height = MapData.mapData.length * 40;
			
			GameLogic logic = new GameLogic(MapData.mapData);
			GameScreen gameScreen = new GameScreen(board_width, board_height);
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
