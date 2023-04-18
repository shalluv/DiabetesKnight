package logic;

import java.util.ArrayList;

import application.CSVParser;
import application.Main;
import entity.Player;
import entity.base.Entity;
import sharedObject.RenderableHolder;

public class GameLogic {

	private ArrayList<Entity> gameObjectContainer;

	private Player player;

	public GameLogic() {
		this.gameObjectContainer = new ArrayList<>();

		int[][] mapData = CSVParser.readCSV("res/csv/Level_1.csv");
		Map map = new Map(mapData);
		addAllObject(map.getMap());

		setPlayer(new Player(50, 600));
		addNewObject(getPlayer());
	}

	protected void addAllObject(ArrayList<Entity> entities) {
		for (Entity entity : entities) {
			addNewObject(entity);
		}
	}

	protected void addNewObject(Entity entity) {
		getGameObjectContainer().add(entity);
		RenderableHolder.getInstance().add(entity);
	}

	public void update() {
		getPlayer().update();
		updateScreen();
	}

	private void updateScreen() {
		if (getPlayer().getX() > 640 && getPlayer().getX() + 640 < Map.getWidth()) {
			Main.gameScreen.setX(-(getPlayer().getX() - 640));
		}
		if (getPlayer().getY() > 480 && getPlayer().getY() + 480 < Map.getHeight()) {
			Main.gameScreen.setY(-(getPlayer().getY() - 480));
		}
	}
	
	public ArrayList<Entity> getGameObjectContainer() {
		return gameObjectContainer;
	}

	public Player getPlayer() {
		return player;
	}

	public void setGameObjectContainer(ArrayList<Entity> gameObjectContainer) {
		this.gameObjectContainer = gameObjectContainer;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
