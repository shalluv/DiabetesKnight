package logic;

import java.util.ArrayList;

import sharedObject.RenderableHolder;

public class GameLogic {
	
	private ArrayList<Entity> gameObjectContainer;
	
	public GameLogic(String[] mapData) {
		this.gameObjectContainer=new ArrayList<>();
		Map map=new Map(mapData);
		addAllObject(map.getMap());
	}
	
	protected void addNewObject(Entity entity) {
		gameObjectContainer.add(entity);
		RenderableHolder.getInstance().add(entity);
	}
	
	protected void addAllObject(ArrayList<Entity> entities) {
		for(Entity entity : entities) {
			addNewObject(entity);
		}
	}
	
	public void update() {
		
	}
}
