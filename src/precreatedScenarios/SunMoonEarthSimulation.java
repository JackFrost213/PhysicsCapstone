package precreatedScenarios;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

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
import main.Support3DOther;
import main.TesterMethods;
import precreatedObjects.Earth;
import precreatedObjects.Moon;
import precreatedObjects.SpaceObject;
import precreatedObjects.Sun;
import shapes3D.Geometry3D;
import twodplots.PositionGraph;

public class SunMoonEarthSimulation extends Simulation {

	private PositionGraph posGraph2;
	
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
		ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(0,0,0));
		temp.add(new Vector3d(0, 0, (float)30.29));
		earth.setInitialConditions(temp);
		
		Moon moon = new Moon();
		moon.attachToNode(scalableNode);
		temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(0,0,405402f));
		temp.add(new Vector3d(-0.966f,0,(float)30.29));
		moon.setInitialConditions(temp);
		
		Sun sun = new Sun();
		sun.attachLightEmissions(rootNode);
		sun.attachToNode(scalableNode);
		temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(147120163f, 0, 0));
		temp.add(new Vector3d(0, 0, (float) 0));
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
		
		TopGUI.setSpeed(550000);
		ArrayList<SpaceObject> tempSpace = new ArrayList<SpaceObject>();
		tempSpace.add(spaceObjects.get(0));
		tempSpace.add(spaceObjects.get(1));
		
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	posGraph2 = new PositionGraph(tempSpace, true);
                posGraph2.setVisible(true);
            }
        });
		
	}

	float timeDilation = 550000.0f; // 200X the speed of the simulation
	float timeStep = 50f; //steps through time every 0.1 seconds of real-time
	float tpfTot = 0;
	float graphUpdate = 0;
	float timeElapsed = 0;

	
	double sum = 0;
	int sumTime = 5;
	@Override
	public void simpleUpdate(float tpf) {
		tpfTot += tpf;
		graphUpdate += tpf*(Math.abs(timeDilation)/5000);
		timeElapsed += tpf*timeDilation;
		
		timeDilation = (float) TopGUI.getSpeed();
		//System.out.println(timeDilation);
		for (SpaceObject spaceObject : spaceObjects) {
			spaceObject.update(tpf * timeDilation);
		}
		//DifferentialEquationSolvers.eulerApproximation(spaceObjects, tpf*timeDilation, timeStep);
		//long startTime1 = System.nanoTime();
		DifferentialEquationSolvers.rungeKuttaApproximation(spaceObjects, timeDilation * tpf, timeStep);
		//long endTime1 = System.nanoTime();
		
		//sum += ((double)(endTime1-startTime1))/Math.pow(10, 6);
		
		//if(tpfTot > sumTime) {
			//Sum for Fast SpaceObject = 113.70690000000016
			//Sum for SpaceObject = 2055.923
		//	System.out.println("Execution Time of SpaceObject Class: " + sum);
		//}
		TesterMethods.compareToEarthSunMoonSolution(spaceObjects, timeElapsed, tpf);
		/*ArrayList<SpaceObject> temp = new ArrayList<SpaceObject>();
		temp.add(spaceObjects.get(0));
		temp.add(spaceObjects.get(2));
		TesterMethods.compareToAnalyticalSolution(temp, timeElapsed, tpf);
		*/
		
		if (graphUpdate >= 0.1) {
			posGraph.update();
			posGraph2.update();
			TopGUI.updateTimeElapsed(timeElapsed/86400);
			graphUpdate = 0;
		}
		
	}


}
