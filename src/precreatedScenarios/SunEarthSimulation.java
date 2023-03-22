package precreatedScenarios;

import java.awt.Color;
import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

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
import main.Support3DOther;
import main.TesterMethods;
import precreatedObjects.Earth;
import precreatedObjects.SpaceObject;
import precreatedObjects.Sun;
import shapes3D.Geometry3D;

public class SunEarthSimulation extends Simulation {

	public SunEarthSimulation(SimpleApplication app) {
		super(app);
		SimulationMain main = (SimulationMain)app;
		main.deltaX = 5000000;
		main.deltaY = 5000000;
		main.deltaZ = 5000000;
		spaceObjects = new ArrayList<SpaceObject>();
	}

	@Override
	public void createInitialItems() {
		AssetManager assetManager = SimulationMain.assetManagerExternal;
		Node rootNode = this.app.getRootNode();
		Node scalableNode = ((Node) rootNode.getChild("Scalable"));
		ViewPort viewPort = this.app.getViewPort();

		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
		bloom.setBloomIntensity(1.8f);
		bloom.setBlurScale(8);
		fpp.addFilter(bloom);
		fpp.setNumSamples(8);
		viewPort.addProcessor(fpp);

		SpaceObject earth = new Earth();
		earth.attachToNode(scalableNode);
		ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
		//temp.add(new Vector3d((float)(149192626.18f),0,(float)(149192626.18f)));
		temp.add(new Vector3d(0,0,0));
		temp.add(new Vector3d(0, 0, (float) 29.7877));
		//temp.add(new Vector3d(30f,0,0));
		earth.setInitialConditions(temp);
		earth.setGlobalScale(new Vector3f(1f/(4000f),1f/4000f,1f/4000f));
		earth.lockSize(true);
		
		Sun sun = new Sun();
		sun.attachLightEmissions(rootNode);
		sun.attachToNode(scalableNode);
		//scalableNode.attachChild(sun.getOutline());
		//scalableNode.attachChild(sun.getTrail());
		temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(147120163f, 0, 0));
		temp.add(new Vector3d(0, 0, (float) 0));
		sun.setInitialConditions(temp);
		sun.setGlobalScale(new Vector3f(1f/(160000f),1f/160000f,1f/160000f));
		sun.lockSize(true);
		
		for (Spatial s : scalableNode.getChildren()) {
			if(s instanceof SpaceObject) {
				System.out.println(s.getName());
				spaceObjects.add((SpaceObject)s);
			}
		}
		
		generateExtraVisuals(rootNode);
		
		
		SimulationMain main = (SimulationMain)app;
		main.getChaseCamera().setSpatial(earth);
		
		TopGUI.setSpeed(5);

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

	float timeDilation = 500000.0f; // timeDilationX the speed of the simulation
	float timeStep = 500f; //steps through time every x seconds of real-time
	float graphUpdate = 0;
	float timeElapsed = 0;
	float tpfTot = 0;

	@Override
	public void simpleUpdate(float tpf) {
		tpfTot += tpf;
		graphUpdate += tpf*(Math.abs(timeDilation)/5000);
		timeElapsed += tpf*timeDilation;
		
		timeDilation = (float) TopGUI.getSpeed();
		for (SpaceObject spaceObject : spaceObjects) {
			spaceObject.update(tpf * timeDilation);
		}
		DifferentialEquationSolvers.rungeKuttaApproximation(spaceObjects, timeDilation * tpf, timeStep);
		//DifferentialEquationSolvers.eulerApproximation(spaceObjects, timeDilation * tpf, timeStep);
		TesterMethods.compareToAnalyticalSolution(spaceObjects, timeElapsed, tpf);
		
		if (graphUpdate >= 0.1) {
			posGraph.update();
			TopGUI.updateTimeElapsed(timeElapsed/86400);
			graphUpdate = 0;
			}
	}
}
