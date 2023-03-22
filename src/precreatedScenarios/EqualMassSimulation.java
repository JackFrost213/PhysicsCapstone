package precreatedScenarios;

import java.awt.Color;
import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import net.wcomohundro.jme3.math.Vector3d;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LightControl;
import com.jme3.texture.Texture;

import guiStuff.TopGUI;
import main.DifferentialEquationSolvers;
import main.SimulationMain;
import main.ShapeGenerator;
import main.Support3DOther;
import main.TesterMethods;
import precreatedObjects.Earth;
import precreatedObjects.SpaceObject;
import precreatedObjects.Sun_EarthSize;
import shapes3D.Geometry3D;

public class EqualMassSimulation extends Simulation {

	public EqualMassSimulation(SimpleApplication app) {
		super(app);
		spaceObjects = new ArrayList<SpaceObject>();
	}

	@Override
	public void createInitialItems() {
		super.createInitialItems();
		SpaceObject earth = new Earth();
		ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(0, 0, (float) 0));
		temp.add(new Vector3d(0, 0, 3));
		earth.setInitialConditions(temp);
		spaceObjects.add(earth);

		Sun_EarthSize sun = new Sun_EarthSize();
		spaceObjects.add(sun);
		temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(5000 * 8, 0, (float) 0));
		temp.add(new Vector3d(0, 0, -1));
		sun.setInitialConditions(temp);

		if(app != null) {
			for(SpaceObject s : spaceObjects) {
				s.attachToNode(scalableNode);
			}
			SimulationMain main = (SimulationMain) app;
			main.getChaseCamera().setSpatial(earth);
			TopGUI.setSpeed(5000);
		}

	}

	public void generateExtraVisuals(Node scalableNode) {
		int numBigPnt = 4000;
		float pntSize = 400000;
		float coords[] = new float[3 * numBigPnt];

		for (int i = 0; i < numBigPnt; i++) {
			int j = i * 3;
			float x = (float) (-10000000 + 2 * 10000000 * Math.random());
			float y = (float) (-10000000 + 2 * 10000000 * Math.random());
			float z = (float) (-10000000 + 2 * 10000000 * Math.random());
			// measure distance to origin
			while (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) < 2920000) {
				x = (float) (-10000000 + 2 * 10000000 * Math.random());
				y = (float) (-10000000 + 2 * 10000000 * Math.random());
				z = (float) (-10000000 + 2 * 10000000 * Math.random());
			}
			coords[j] = x;
			coords[j + 1] = y;
			coords[j + 2] = z;

			Geometry3D sphere = Support3DOther.solidSphere(scalableNode, x, y, z, pntSize / 100, 6, 6,
					new Color(255, 255, 255, 255));
			sphere.setQueueBucket(Bucket.Sky);
		}
	}

	float timeDilation = 5000f; // 100X the speed of the simulation
	float timeStep = 0.5f;// steps through time every 0.1 seconds of real-time
	float tpfTot = 0;
	float graphUpdate = 0;

	@Override
	public void simpleUpdate(float tpf) {
		tpfTot += tpf;
		graphUpdate += tpf*(timeDilation/5000);

		timeDilation = (float) TopGUI.getSpeed();
		for (SpaceObject spaceObject : spaceObjects) {
			spaceObject.update(tpf * timeDilation);
		}
		DifferentialEquationSolvers.eulerApproximation(spaceObjects, timeDilation * tpf, timeStep);
		TesterMethods.compareToAnalyticalSolution(spaceObjects, tpfTot*timeDilation, tpf);
	
		//System.out.println(graphUpdate);
		if (graphUpdate >= 0.1) {
		posGraph.update();
		TopGUI.updateTimeElapsed(tpfTot*timeDilation/86400);
		graphUpdate = 0;
		}
	}
	
}
