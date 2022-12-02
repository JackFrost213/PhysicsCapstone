package shapes3D;

import java.util.ArrayList;

import com.jme3.app.SimpleApplication;

public abstract class Simulation {

	protected SimpleApplication app;
	protected ArrayList<SpaceObject> spaceObjects;
	public Simulation(SimpleApplication app) {
		this.app = app;
	}
	
	public abstract void createInitialItems();
	
	public abstract void simpleUpdate(float tpf);
	
	public ArrayList<SpaceObject> getSpaceObjects(){
		return spaceObjects;
	}
}
