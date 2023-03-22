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

public class SunEarthJamesWebbL5Simulation extends Simulation {

	private PositionGraph posGraph2;
	
	public SunEarthJamesWebbL5Simulation(SimpleApplication app) {
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
		float au = 147120163f;
		temp.add(new Vector3d(0.5f*au,0,-(Math.sqrt(Math.pow(au, 2) - Math.pow(0.5f*au, 2)))));
		temp.add(new Vector3d(-26.231,0,15.154));
		tele.setInitialConditions(temp);
		
		Sun sun = new Sun();
		temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(147120163f, 0, 0));
		temp.add(new Vector3d(0, 0, (float) 0));
		sun.setInitialConditions(temp);
		spaceObjects.add(earth);
		spaceObjects.add(tele);
		spaceObjects.add(sun);
		
		
		if(this.app != null) {
			for(SpaceObject s : spaceObjects) {
				s.attachToNode(scalableNode);
			}
			SimulationMain main = (SimulationMain) app;
			main.getChaseCamera().setSpatial(earth);
			TopGUI.setSpeed(5);
			
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
		
		if (graphUpdate >= 0.1) {
			posGraph.update();
			posGraph2.update();
			TopGUI.updateTimeElapsed(timeElapsed/86400);
			graphUpdate = 0;
			}
		
	}

}
