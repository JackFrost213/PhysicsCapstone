package precreatedScenarios;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import guiStuff.TopGUI;
import main.ChaosNumber;
import main.SimulationMain;
import main.Support3DOther;
import net.wcomohundro.jme3.math.Vector3d;
import precreatedObjects.SpaceObject;
import shapes3D.Geometry3D;
import twodplots.PositionGraph;

public abstract class Simulation {

	protected SimpleApplication app;
	protected Node rootNode;
	protected Node scalableNode;
	protected ArrayList<SpaceObject> spaceObjects;
	protected PositionGraph posGraph;
	protected ArrayList<PositionGraph> extraPosGraphs;

	public Simulation(SimpleApplication app) {
		this.app = app;
		if (app != null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					posGraph = new PositionGraph(spaceObjects, true);
					posGraph.setVisible(true);
				}
			});
		}
		spaceObjects = new ArrayList<SpaceObject>();
		extraPosGraphs = new ArrayList<PositionGraph>();
	}

	public void createInitialItems() {
		if (app != null) {
			AssetManager assetManager = SimulationMain.assetManagerExternal;
			rootNode = this.app.getRootNode();
			scalableNode = ((Node) rootNode.getChild("Scalable"));
			ViewPort viewPort = this.app.getViewPort();

			FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
			BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
			bloom.setBloomIntensity(1.8f);
			bloom.setBlurScale(8);
			fpp.addFilter(bloom);
			fpp.setNumSamples(8);
			viewPort.addProcessor(fpp);
			generateExtraVisuals(rootNode);
		}
	}

	public void createInitialItemsPostPhase(double initialSpeed) {
			if(this.app != null) {
			
			for(SpaceObject s : spaceObjects) {
				s.attachToNode(scalableNode);
			}
			
			SimulationMain main = (SimulationMain)app;
			main.getChaseCamera().setSpatial((Spatial)((Node) rootNode.getChild("Scalable")).getChild("LockPoint"));
			
			TopGUI.setSpeed(initialSpeed);
		}
	}
	
	
	protected float timeDilation = 100.0f; // 100X the speed of the simulation
	protected float timeStep = 500f; //steps through time every 500 seconds of real-time
	protected float tpfTot = 0;
	protected float graphUpdate = 0;
	protected float timeElapsed = 0;
	public void simpleUpdate(float tpf) {
		tpfTot += tpf;
		graphUpdate += tpf*(Math.abs(timeDilation)/5000);
		timeElapsed += tpf*timeDilation;
		
		timeDilation = (float) TopGUI.getSpeed();
		
		for (SpaceObject spaceObject : spaceObjects) {
			spaceObject.update(tpf * timeDilation);
		}
		
		if (graphUpdate >= 0.1) {
			posGraph.update();
			for(PositionGraph pG : extraPosGraphs) {
				if(pG != null)
					pG.update();
			}
			
			TopGUI.updateTimeElapsed(timeElapsed/86400);
			graphUpdate = 0;
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

	public ArrayList<SpaceObject> getSpaceObjects() {
		return spaceObjects;
	}
	
	public void analyzeChaosData(ChaosNumber smallestValue, ChaosNumber largestValue) {
		return;
	}
}
