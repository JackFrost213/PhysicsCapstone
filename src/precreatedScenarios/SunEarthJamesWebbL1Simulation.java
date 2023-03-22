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
import main.ChaosGenerator;
import main.ChaosNumber;
import main.DifferentialEquationSolvers;
import main.GravitationalCalculator;
import main.Support3DOther;
import main.TesterMethods;
import main.runnable.SimulationMain;
import precreatedObjects.Earth;
import precreatedObjects.JamesWebbSpaceTelescope;
import precreatedObjects.Moon;
import precreatedObjects.SpaceObject;
import precreatedObjects.Sun;
import shapes3D.Geometry3D;
import twodplots.PositionGraph;

public class SunEarthJamesWebbL1Simulation extends Simulation {

	private PositionGraph posGraph2;
	
	public SunEarthJamesWebbL1Simulation(SimpleApplication app) {
		super(app);
		SimulationMain main = (SimulationMain)app;
		main.deltaX = 5000000;
		main.deltaY = 5000000;
		main.deltaZ = 5000000;
		spaceObjects = new ArrayList<SpaceObject>();
	}

	@Override
	public void createInitialItems() {
		super.createInitialItems();
		
		SpaceObject earth = new Earth();
		ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(0,0,0)); //starting position
		temp.add(new Vector3d(0, 0, (float) 30.29)); //starting velocity
		earth.setInitialConditions(temp);
		
		JamesWebbSpaceTelescope tele = new JamesWebbSpaceTelescope();
		temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(1471600,0,0f));
		//temp.add(new Vector3d(10000,0,0f));
		//tele.activateBoosters(1471600,earth);
		//Between 30.5 (collapses) and 30.7 (escapes)
		temp.add(new Vector3d(0,0,30));
		tele.setInitialConditions(temp);
		
		Sun sun = new Sun();
		temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(147120163f, 0, 0));
		temp.add(new Vector3d(0, 0, (float) 0));
		sun.setInitialConditions(temp);
		
		spaceObjects.add(earth);
		spaceObjects.add(tele);
		spaceObjects.add(sun);
		
		createInitialItemsPostPhase(5);
		
		if(this.app != null) {
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
		
	}

	float timeDilation = 550.0f; // 200X the speed of the simulation
	float timeStep = 500f; //steps through time every 0.1 seconds of real-time
	float tpfTot = 0;
	float graphUpdate = 0;
	float timeElapsed = 0;
	
	@Override
	public void simpleUpdate(float tpf) {
		tpfTot += tpf;
		graphUpdate += tpf*(Math.abs(timeDilation)/5000);
		timeElapsed += tpf*timeDilation;
		
		timeDilation = (float) TopGUI.getSpeed();
		
		for (SpaceObject spaceObject : spaceObjects) {
			spaceObject.update(tpf * timeDilation);
		}
		
		DifferentialEquationSolvers.eulerApproximation(spaceObjects, timeDilation * tpf, timeStep);
		//DifferentialEquationSolvers.rungeKuttaApproximation(spaceObjects, timeDilation * tpf, timeStep);
		ArrayList<SpaceObject> sunAndEarth = new ArrayList<SpaceObject>();
		sunAndEarth.add(spaceObjects.get(0));
		sunAndEarth.add(spaceObjects.get(2));
		TesterMethods.compareToAnalyticalSolution(sunAndEarth, tpfTot*timeDilation, tpf);
		
		if (graphUpdate >= 0.1) {
			posGraph.update();
			posGraph2.update();
			TopGUI.updateTimeElapsed(timeElapsed/86400);
			graphUpdate = 0;
			}
		
		}

	@Override
	public void analyzeChaosData(ChaosNumber smallestValue, ChaosNumber largestValue, SpaceObject probe) {
		System.out.println("Analysis of Lagrange Point Finder for L1: ");
		System.out.println("Results of Sticky Stability Function: ");
		double theoreticaldist = spaceObjects.get(0).getToScalePosition().distance(spaceObjects.get(2).getToScalePosition());
		double theoreticalL2XPos = theoreticaldist * (1 - Math.pow(spaceObjects.get(0).getMass()/(3*spaceObjects.get(2).getMass()), 1.0/3.0));
		theoreticalL2XPos -= theoreticaldist;
		Vector3d theoreticalL2 = new Vector3d(-theoreticalL2XPos, 0,0);
		System.out.println("Theoretical: " + theoreticalL2);
		System.out.println("Analytical: " + smallestValue.getPosition());
		System.out.println("Percent Error: " + 100*Math.abs(theoreticalL2.x - smallestValue.getPosition().x)/theoreticalL2.x);
		System.out.println("\nAssociated Lyapunov Exponents: ");
		System.out.println("The associated Largest Lyapunov Exponent for " + smallestValue.getPosition() + " is " + smallestValue.getLyapunovExponents().x);
		System.out.println("The associated Largest Lyapunov Exponent for " + largestValue.getPosition() + " is " + largestValue.getLyapunovExponents().x);
	}
	
}
