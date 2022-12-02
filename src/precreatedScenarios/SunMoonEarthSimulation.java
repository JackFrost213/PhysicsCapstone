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
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LightControl;
import com.jme3.texture.Texture;

import main.DifferentialEquationSolvers;
import main.SimulationMain;
import main.Support3DOther;
import main.TesterMethods;
import precreatedObjects.Earth;
import precreatedObjects.Moon;
import precreatedObjects.Sun;
import shapes3D.Geometry3D;
import shapes3D.Simulation;
import shapes3D.SpaceObject;

public class SunMoonEarthSimulation extends Simulation {

	public SunMoonEarthSimulation(SimpleApplication app) {
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
		bloom.setBloomIntensity(0.8f);
		bloom.setBlurScale(8);
		fpp.addFilter(bloom);
		fpp.setNumSamples(8);
		viewPort.addProcessor(fpp);

		SpaceObject earth = new Earth();
		earth.attachToNode(scalableNode);
		ArrayList<Vector3f> temp = new ArrayList<Vector3f>();
		temp.add(new Vector3f(0,0,0));
		temp.add(new Vector3f(0, 0, (float) 30));
		earth.setInitialConditions(temp);
		
		Moon moon = new Moon();
		moon.attachToNode(scalableNode);
		temp = new ArrayList<Vector3f>();
		temp.add(new Vector3f(0,0,384472.282f));
		temp.add(new Vector3f(-0.966f,0,(float)30));
		moon.setInitialConditions(temp);
		
		Sun sun = new Sun();
		sun.attachLightEmissions(rootNode);
		sun.attachToNode(scalableNode);
		temp = new ArrayList<Vector3f>();
		temp.add(new Vector3f(149192626.18f, 0, 0));
		temp.add(new Vector3f(0, 0, (float) 0));
		sun.setInitialConditions(temp);
		
		generateExtraVisuals(rootNode);
		
		for (Spatial s : scalableNode.getChildren()) {
			if(s instanceof SpaceObject) {
				System.out.println(s.getName());
				spaceObjects.add((SpaceObject)s);
			}
		}
		
		SimulationMain main = (SimulationMain)app;
		main.getChaseCamera().setSpatial(moon);
		
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

	float timeDilation = 550000.0f; // 200X the speed of the simulation
	float timeStep = 100f; //steps through time every 0.1 seconds of real-time
	float tpfTot = 0;
	
	@Override
	public void simpleUpdate(float tpf) {
		if(tpf < 0.05) {
		tpfTot += tpf;
		
		Quaternion rotation4 = new Quaternion();
		rotation4.fromAngleAxis((float) (-7.27E-5 * tpf * timeDilation), new Vector3f(0, 0, 1));
		spaceObjects.get(0).setLocalRotation(spaceObjects.get(0).getLocalRotation().mult(rotation4));
		
		DifferentialEquationSolvers.eulerApproximation(spaceObjects, timeDilation * tpf, timeStep);
		ArrayList<SpaceObject> sunAndEarth = new ArrayList<SpaceObject>();
		sunAndEarth.add(spaceObjects.get(0));
		sunAndEarth.add(spaceObjects.get(2));
		TesterMethods.compareToAnalyticalSolution(sunAndEarth, tpfTot*timeDilation, tpf);
		
		}
	}


}
