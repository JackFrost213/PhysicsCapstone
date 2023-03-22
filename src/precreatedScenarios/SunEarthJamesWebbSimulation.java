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
import shapes3D.SpaceObjectFast;
import twodplots.PositionGraph;

public class SunEarthJamesWebbSimulation extends Simulation {

	private PositionGraph posGraph2;
	private ArrayList<SpaceObject> sunAndEarth;
	
	public SunEarthJamesWebbSimulation(SimpleApplication app) {
		super(app);
		SimulationMain main = (SimulationMain)app;
		main.deltaX = 5000000;
		main.deltaY = 5000000;
		main.deltaZ = 5000000;
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
		temp.add(new Vector3d(-1471603.356,0,0));
		tele.activateBoosters(1471603,earth);
		//Between 30.5 (collapses) and 30.7 (escapes)
		temp.add(new Vector3d(0,0,30.6));
		tele.setInitialConditions(temp);
		
		Sun sun = new Sun();
		temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(147120163f, 0, 0));
		temp.add(new Vector3d(0, 0, (float) 0));
		sun.setInitialConditions(temp);
		
		spaceObjects.add(earth);
		spaceObjects.add(tele);
		spaceObjects.add(sun);
		
		sunAndEarth = new ArrayList<SpaceObject>();
		sunAndEarth.add(spaceObjects.get(0));
		sunAndEarth.add(spaceObjects.get(2));
		
		 createInitialItemsPostPhase(500);
		
		if(this.app != null) {
			SimulationMain main = (SimulationMain) app;
			main.getRightSideGUI().setFocusObject(earth);
			initializeSecondPositionGraph();
		}
		
	}

	private void initializeSecondPositionGraph() {
		ArrayList<SpaceObject> tempSpace = new ArrayList<SpaceObject>();
		tempSpace.add(spaceObjects.get(0));
		tempSpace.add(spaceObjects.get(1));
		
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	posGraph2 = new PositionGraph(tempSpace, true);
                posGraph2.setVisible(true);
                extraPosGraphs.add(posGraph2);
            }
        });
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		
		DifferentialEquationSolvers.eulerApproximation(spaceObjects, timeDilation*tpf, timeStep);
		TesterMethods.compareToAnalyticalSolution(sunAndEarth, tpfTot*timeDilation, tpf);
		}
	
	@Override
	public void analyzeChaosData(ChaosNumber smallestValue, ChaosNumber largestValue, SpaceObject probe) {
		System.out.println("Analysis of Lagrange Point Finder for L2: ");
		System.out.println("Results of Sticky Stability Function: ");
		double theoreticaldist = spaceObjects.get(0).getToScalePosition().distance(spaceObjects.get(2).getToScalePosition());
		double theoreticalL2XPos = theoreticaldist * (1 + Math.pow(spaceObjects.get(0).getMass()/(3*spaceObjects.get(2).getMass()), 1.0/3.0));
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
