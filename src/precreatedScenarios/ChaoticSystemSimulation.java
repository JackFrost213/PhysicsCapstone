package precreatedScenarios;

import java.awt.Color;
import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import guiStuff.TopGUI;
import main.DifferentialEquationSolvers;
import main.Support3DOther;
import main.TesterMethods;
import main.runnable.SimulationMain;
import net.wcomohundro.jme3.math.Vector3d;
import precreatedObjects.Earth;
import precreatedObjects.JamesWebbSpaceTelescope;
import precreatedObjects.MasslessProbe;
import precreatedObjects.SpaceObject;
import precreatedObjects.Sun;
import precreatedObjects.Sun_EarthSize;
import shapes3D.Geometry3D;

public class ChaoticSystemSimulation extends Simulation{
	public ChaoticSystemSimulation(SimpleApplication app) {
		super(app);
		spaceObjects = new ArrayList<SpaceObject>();
	}

	@Override
	public void createInitialItems() {
		super.createInitialItems();
		
		Sun_EarthSize sun = new Sun_EarthSize();
		ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(0, 0, 0));
		temp.add(new Vector3d(0, 0, (float)3));
		sun.setInitialConditions(temp);
		spaceObjects.add(sun);
		
		SpaceObject earth2 = new MasslessProbe();
		ArrayList<Vector3d> temp2 = new ArrayList<Vector3d>();
		//temp2.add(new Vector3d(28750, 0, 7000));
		temp2.add(new Vector3d(500, 0, 7300));
		//temp2.add(new Vector3d(10000, 0, -50000));
		temp2.add(new Vector3d(2, 0, (float)2));
		earth2.setInitialConditions(temp2);
		earth2.setName("Telescope2");
		spaceObjects.add(earth2);
		
		SpaceObject earth3 = new Earth();
		ArrayList<Vector3d> temp3 = new ArrayList<Vector3d>();
		temp3.add(new Vector3d(0, 0, (float) -50000));
		//temp2.add(new Vector3d(10000, 0, -50000));
		temp3.add(new Vector3d(-3, 0, (float)5));
		earth3.setInitialConditions(temp3);
		earth3.setName("Earth3");
		spaceObjects.add(earth3);
		

		Sun_EarthSize sun2 = new Sun_EarthSize();
		temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(50000, 0, 0));
		temp.add(new Vector3d(0, 0, (float) -2));
		sun2.setInitialConditions(temp);
		sun2.setName("Sun2");
		spaceObjects.add(sun2);
		
		if(app != null) {
			for(SpaceObject s : spaceObjects) {
				s.attachToNode(scalableNode);
			}
			SimulationMain main = (SimulationMain) app;
			main.getChaseCamera().setSpatial(earth2);
			TopGUI.setSpeed(5000);
		}

	}	

	float timeDilation = 5000f; // 100X the speed of the simulation
	float timeElapsed = 0;
	float timeStep = 500f;// steps through time every 0.1 seconds of real-time
	float tpfTot = 0;
	float graphUpdate = 0;

	@Override
	public void simpleUpdate(float tpf) {
		tpfTot += tpf;
		graphUpdate += tpf*(timeDilation/5000);
		timeElapsed += tpf*timeDilation;

		timeDilation = (float) TopGUI.getSpeed();
		for (SpaceObject spaceObject : spaceObjects) {
			spaceObject.update(tpf * timeDilation);
		}
		DifferentialEquationSolvers.eulerApproximation(spaceObjects, timeDilation * tpf, timeStep);
		//TesterMethods.compareToAnalyticalSolution(spaceObjects, tpfTot*timeDilation, tpf);
	
		if (graphUpdate >= 0.1) {
			posGraph.update();
			TopGUI.updateTimeElapsed(timeElapsed/86400);
			graphUpdate = 0;
		}
	}
	
}
