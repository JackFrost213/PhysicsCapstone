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

import main.DifferentialEquationSolvers;
import main.SimulationMain;
import main.ShapeGenerator;
import main.Support3DOther;
import main.TesterMethods;
import precreatedObjects.Earth;
import precreatedObjects.SpaceObject;
import precreatedObjects.Sun_EarthSize;
import shapes3D.Geometry3D;

public class FourBodyEqualMassSimulation extends Simulation {

	public FourBodyEqualMassSimulation(SimpleApplication app) {
		super(app);
		spaceObjects = new ArrayList<SpaceObject>();
	}

	float distScale = 15;
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
		temp.add(new Vector3d(0, 0, (float) 0));
		temp.add(new Vector3d(0, 0, -3));
		earth.setInitialConditions(temp);
		
		SpaceObject earth2 = new Earth();
		earth2.attachToNode(scalableNode);
		temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(5000 * distScale, 0, (float) 5000 * distScale));
		temp.add(new Vector3d(0, 0, 3));
		earth2.setInitialConditions(temp);

		SpaceObject earth3 = new Earth();
		earth3.attachToNode(scalableNode);
		temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(0, 0, (float) 5000 * distScale));
		temp.add(new Vector3d(-3, 0, 0));
		earth3.setInitialConditions(temp);
		
		Sun_EarthSize sun = new Sun_EarthSize();
		sun.attachLightEmissions(rootNode);
		sun.attachToNode(scalableNode);
		temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(5000 * distScale, 0, (float) 0));
		temp.add(new Vector3d(3, 0, 0));
		sun.setInitialConditions(temp);

		for (Spatial s : scalableNode.getChildren()) {
			if (s instanceof SpaceObject) {
				System.out.println(s.getName());
				spaceObjects.add((SpaceObject) s);
			}
		}

		generateExtraVisuals(rootNode);

		SimulationMain main = (SimulationMain) app;
		main.getChaseCamera().setSpatial(earth);

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

	float timeDilation = 20000f; // 100X the speed of the simulation
	float timeStep = 1f; // steps through time every 0.1 seconds of real-time
	float tpfTot = 0;
	float tpfTimer = 0;

	@Override
	public void simpleUpdate(float tpf) {
		tpfTot += tpf;
		tpfTimer += tpf*timeDilation;

		for (SpaceObject spaceObject : spaceObjects) {
			spaceObject.update(tpf * timeDilation);
		}
		
		/*if(spaceObjects.get(0).getToScalePosition().distance(spaceObjects.get(0).getInitialConditions().get(0)) < 2000) {
			System.out.println("TpfTimer for orbit: " + tpfTimer);
			System.out.println("Distance: " + spaceObjects.get(0).getToScalePosition().distance(spaceObjects.get(0).getInitialConditions().get(0)));
		}*/
		
		if(tpfTimer >= 192090.6) {
			spaceObjects.get(0).setToScalePosition(new Vector3d(0, 0, (float) 0));
			spaceObjects.get(0).setVelocity(new Vector3d(0, 0, -3));
			spaceObjects.get(1).setToScalePosition(new Vector3d(5000*distScale, 0, (float) 5000*distScale));
			spaceObjects.get(1).setVelocity(new Vector3d(0, 0, 3));
			spaceObjects.get(2).setToScalePosition(new Vector3d(0, 0, (float) 5000*distScale));
			spaceObjects.get(2).setVelocity(new Vector3d(-3, 0, 0));
			spaceObjects.get(3).setToScalePosition(new Vector3d(5000*distScale, 0, (float) 0));
			spaceObjects.get(3).setVelocity(new Vector3d(3, 0, 0));
			tpfTimer = 0;
		}
		
		DifferentialEquationSolvers.eulerApproximation(spaceObjects, timeDilation * tpf, timeStep);
		//TesterMethods.compareToAnalyticalSolution(spaceObjects, tpfTot*timeDilation);
	}

}