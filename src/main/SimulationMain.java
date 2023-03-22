package main;

import item3D.Item3D;
import precreatedObjects.SpaceObject;
import precreatedScenarios.ChaoticSystemSimulation;
import precreatedScenarios.ChaoticSystemSimulation2;
import precreatedScenarios.EqualMassSimulation;
import precreatedScenarios.FourBodyEqualMassSimulation;
import precreatedScenarios.SunEarthJamesWebbL1Simulation;
import precreatedScenarios.SunEarthJamesWebbL4Simulation;
import precreatedScenarios.SunEarthJamesWebbSimulation;
import precreatedScenarios.SunEarthSimulation;
import precreatedScenarios.SunMoonEarthSimulation;
import shapes3D.Geometry3D;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.input.ChaseCamera;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import guiStuff.GuiPanel;
import guiStuff.JFrameResizable;
import guiStuff.LeftSideGUI;
import guiStuff.RightSideGUI;
import guiStuff.TopGUI;
import shapes3D.Axis;

public class SimulationMain extends SimpleApplication {
	
	protected float _xscale = 1.0f;
	protected float _yscale = 1.0f;
	protected float _zscale = 1.0f;

	// view rotation angles (degrees)
	private float _view_rotx;
	private float _view_roty;
	private float _view_rotz;

	private ChaseCamera camera;

	// distance in front of the screen
	private float _zdist;

	// x and y translation
	private float _xdist;
	private float _ydist;

	// the list of 3D items to be drawn
	protected Vector<Item3D> _itemList = new Vector<Item3D>();

	protected String _rendererStr;

	private static Vector3f coordinateSize;
	private Vector3f prevCoordinateSize;
	private Vector3f prevNumOfSegments;

	public static RightSideGUI GUI;
	private static LeftSideGUI analyticGUI;
	private static TopGUI topGUI;
	private Geometry3D lockPoint;

	public static float numOfSegmentsX = 0.5f;
	public static float numOfSegmentsY = 0.5f;
	public static float numOfSegmentsZ = 0.5f;
	public static float deltaX = 5000f;
	public static float deltaY = 5000f;
	public static float deltaZ = 5000f;
	private static float rotX = 0;
	private static float rotY = 0;
	private static float rotZ = 0;

	public static AssetManager assetManagerExternal;
	
	//SunMoonEarthSimulation simulation1 = new SunMoonEarthSimulation((SimpleApplication)this);
	//SunEarthSimulation simulation1 = new SunEarthSimulation((SimpleApplication)this);
	SunEarthJamesWebbSimulation simulation1 = new SunEarthJamesWebbSimulation((SimpleApplication)this);
	//SunEarthJamesWebbL1Simulation simulation1 = new SunEarthJamesWebbL1Simulation((SimpleApplication)this);
	//FourBodyEqualMassSimulation simulation1 = new FourBodyEqualMassSimulation((SimpleApplication)this);
	//EqualMassSimulation simulation1 = new EqualMassSimulation((SimpleApplication)this);
	//ChaoticSystemSimulation2 simulation1 = new ChaoticSystemSimulation2((SimpleApplication)this);
	/*
	 * The panel that holds the 3D objects
	 * 
	 * @param angleX the initial x rotation angle in degrees
	 * 
	 * @param angleY the initial y rotation angle in degrees
	 * 
	 * @param angleZ the initial z rotation angle in degrees
	 * 
	 * @param xdist move viewpoint left/right
	 * 
	 * @param ydist move viewpoint up/down
	 * 
	 * @param zdist the initial viewer z distance should be negative
	 */
	public SimulationMain(float angleX, float angleY, float angleZ, float xDist, float yDist, float zDist) {
		_xdist = xDist;
		_ydist = yDist;
		_zdist = zDist;
		_view_rotx = angleX % 180;
		_view_roty = angleY % 180;
		_view_rotz = angleZ;
	}

	/*
	 * The panel that holds the 3D objects
	 * 
	 * @param angleX the initial x rotation angle in degrees
	 * 
	 * @param angleY the initial y rotation angle in degrees
	 * 
	 * @param angleZ the initial z rotation angle in degrees
	 * 
	 * @param xdist move viewpoint left/right
	 * 
	 * @param ydist move viewpoint up/down
	 * 
	 * @param zdist the initial viewer z distance should be negative
	 */
	public SimulationMain(float angleX, float angleY, float angleZ, float xDist, float yDist, float zDist, float bgRed,
			float bgGreen, float bgBlue) {

	}

	// the openGL version and renderer strings
	protected String _versionStr;

	/**
	 * Create the initial items
	 */
	public void createInitialItems() {

		// Add Coordinate System

		
		simulation1.createInitialItems();
		ArrayList<Geometry3D> objsToAdd = new ArrayList<Geometry3D>();
		for(SpaceObject s : simulation1.getSpaceObjects()) {
			objsToAdd.add(s);
		}
		objsToAdd.add(lockPoint);
		GUI.attachSimulationObjects(objsToAdd);
		Node axes = new Node("Axes");
		rootNode.attachChild(axes);
		int length = 60;

		// Line from 0,0,0 to 0,1,0
		Axis yAxisTemp = new Axis(new Vector3f(0, 1, 0), 0.5f, 1, length);
		yAxisTemp.setColor(Color.white, Color.white);
		yAxisTemp.setWordColor(Color.white);
		axes.attachChild(yAxisTemp);

		// Line from 0,0,0 to 0,0,1
		Axis zAxisTemp = new Axis(new Vector3f(0, 0, 1), 0.5f, 1, length);
		zAxisTemp.setColor(Color.white, Color.white);
		zAxisTemp.setWordColor(Color.white);
		axes.attachChild(zAxisTemp);

		// Line from 0,0,0 to 1,0,0
		Axis xAxisTemp = new Axis(new Vector3f(1, 0, 0), 0.5f, 1, length);
		xAxisTemp.setColor(Color.white, Color.white);
		xAxisTemp.setWordColor(Color.white);
		axes.attachChild(xAxisTemp);
	}

	public void setScale(float xscale, float yscale, float zscale) {
		_xscale = xscale;
		_yscale = yscale;
		_zscale = zscale;
	}

	@Override
	public void simpleInitApp() {

		viewPort.setBackgroundColor(new ColorRGBA(0, 0, 0, 1f));
		assetManagerExternal = assetManager;

		coordinateSize = new Vector3f(deltaX, deltaY, deltaZ);
		prevCoordinateSize = new Vector3f(1, 1, 1);
		prevNumOfSegments = new Vector3f(0.5f, 0.5f, 0.5f);
		Node scalableNode = new Node("Scalable");
		rootNode.attachChild(scalableNode);

		lockPoint = ShapeGenerator.createCube(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Quaternion(),
				new ColorRGBA(0, 0, 0, 0f));
		lockPoint.setName("LockPoint");
		((Node) rootNode.getChild("Scalable")).attachChild(lockPoint);

		setupCamera(lockPoint);
		createInitialItems();
		//setupCamera((SpaceObject) rootNode.getChild("Earth"));
		// setupHUD();
	}

	private void setupCamera(Geometry lockPoint) {
		flyCam.setEnabled(false);
		cam.setLocation(new Vector3f(_xdist, _ydist, _zdist));
		cam.setFrustumFar(100000000);
		cam.onFrameChange();
		camera = new ChaseCamera(cam, lockPoint, inputManager);
		getChaseCamera().setRotationSpeed(5);
		getChaseCamera().setInvertVerticalAxis(true);
		getChaseCamera().setDefaultHorizontalRotation((float) (_view_rotx * (Math.PI / 180)));
		getChaseCamera().setDefaultVerticalRotation((float) (_view_roty * (Math.PI / 180)));
		getChaseCamera().setMaxVerticalRotation((float) (90f * (Math.PI / 180)));
		getChaseCamera().setMinVerticalRotation((float) (-89f * (Math.PI / 180)));
	}

	private void setupHUD() {
		setDisplayStatView(false);
		setDisplayFps(false);
		GUI.scaleX.setText(String.valueOf(deltaX));
		GUI.scaleY.setText(String.valueOf(deltaY));
		GUI.scaleZ.setText(String.valueOf(deltaZ));
		GUI.rotX.setText(String.valueOf(rotX));
		GUI.rotY.setText(String.valueOf(rotY));
		GUI.rotZ.setText(String.valueOf(rotZ));
		GUI.tickX.setText(String.valueOf(numOfSegmentsX));
		GUI.tickY.setText(String.valueOf(numOfSegmentsY));
		GUI.tickZ.setText(String.valueOf(numOfSegmentsZ));
		
		
		
	}

	boolean isStarting = true;
	float timePassed = 0;
	float startTimer = 10;
	@Override
	public void simpleUpdate(float tpf) {
		if(isStarting) {
			timePassed+=tpf;
			if(timePassed>=startTimer && tpf <= 0.02) {
				isStarting = false;
			}
		}
		
		adjustScale();
		if(!isStarting) {
			simulation1.simpleUpdate(tpf);
			GUI.update(tpf);
			}
		}	

	public void adjustScale() {

		for (Spatial s : ((Node) rootNode.getChild("Axes")).getChildren()) {
			Axis axis = (Axis) s;
			axis.update(cam);
		}
		coordinateSize = new Vector3f(deltaX, deltaY, deltaZ);
		if (!lockPoint.getLocalTranslation().equals(new Vector3f(rotX, rotY, rotZ))) {
			lockPoint.setLocalTranslation(rotX, rotY, rotZ);
		}
		if (!prevNumOfSegments.equals(new Vector3f(numOfSegmentsX, numOfSegmentsY, numOfSegmentsZ))) {
			prevNumOfSegments = new Vector3f(numOfSegmentsX, numOfSegmentsY, numOfSegmentsZ);
			for (Spatial s : ((Node) rootNode.getChild("Axes")).getChildren()) {
				Axis axis = (Axis) s;
				axis.setSegments(prevNumOfSegments);
			}
		}

		if (!coordinateSize.equals(prevCoordinateSize)) {

			rotX = rotX * prevCoordinateSize.x;
			rotY = rotY * prevCoordinateSize.y;
			rotZ = rotZ * prevCoordinateSize.z;

			rotX = rotX / coordinateSize.x;
			rotY = rotY / coordinateSize.y;
			rotZ = rotZ / coordinateSize.z;

			for (Spatial s : ((Node) rootNode.getChild("Axes")).getChildren()) {
				Axis axis = (Axis) s;
				axis.setDelta(coordinateSize);
			}

			scaleGeometries((Node) rootNode.getChild("Scalable"));
			prevCoordinateSize = coordinateSize;
		}
	}

	private void scaleGeometries(Node parent) {
		for (Spatial g : parent.getChildren()) {
			if (g instanceof Node) {
				scaleGeometries((Node) g);
			} else {
				if (!g.getName().equals("Outline")) {
					if (g instanceof Geometry3D) {
						Geometry3D geom = (Geometry3D) g;
						//geom.setGlobalScale(geom.getGlobalScale().mult(prevCoordinateSize));
						geom.setGlobalScale(geom.getBaseScale().divide(coordinateSize));
					} else {
						g.setLocalScale(g.getLocalScale().mult(prevCoordinateSize));
						g.setLocalScale(g.getLocalScale().divide(coordinateSize));
					}
					g.setLocalTranslation(g.getLocalTranslation().mult(prevCoordinateSize));
					g.setLocalTranslation(g.getLocalTranslation().divide(coordinateSize));
				}
			}
		}
	}

	/**
	 * Set rotation angle about x
	 * 
	 * @param angle about x (degrees)
	 */
	public void setRotationX(float angle) {
		_view_rotx = angle;
	}

	/**
	 * Set rotation angle about y
	 * 
	 * @param angle about y (degrees)
	 */
	public void setRotationY(float angle) {
		_view_roty = angle;
	}

	/**
	 * Set rotation angle about z
	 * 
	 * @param angle about z (degrees)
	 */
	public void setRotationZ(float angle) {
		_view_rotz = angle;
	}

	/**
	 * Get the rotation about x
	 * 
	 * @return the rotation about x (degrees)
	 */
	public float getRotationX() {
		return _view_rotx;
	}

	/**
	 * Get the rotation about y
	 * 
	 * @return the rotation about y (degrees)
	 */
	public float getRotationY() {
		return _view_roty;
	}

	/**
	 * Get the rotation about z
	 * 
	 * @return the rotation about z (degrees)
	 */
	public float getRotationZ() {
		return _view_rotz;
	}

	/**
	 * Change the x distance to move in or out
	 * 
	 * @param dx the change in x
	 */
	public void deltaX(float dx) {
		_xdist += dx;
		// refresh();
	}

	/**
	 * Change the y distance to move in or out
	 * 
	 * @param dy the change in y
	 */
	public void deltaY(float dy) {
		_ydist += dy;
		// refresh();
	}

	/**
	 * Change the z distance to move in or out
	 * 
	 * @param dz the change in z
	 */
	public void deltaZ(float dz) {
		_zdist += dz;
		// refresh();
	}

	/**
	 * Add an item to the list. Note that this does not initiate a redraw.
	 * 
	 * @param item the item to add.
	 */
	public void addItem(Geometry item) {
		rootNode.attachChild(item);
	}

	/**
	 * Remove an item from the list. Note that this does not initiate a redraw.
	 * 
	 * @param item the item to remove.
	 */
	public void removeItem(Geometry item) {
		item.removeFromParent();
	}

	/**
	 * This gets the z step used by the mouse and key adapters, to see how fast we
	 * move in or in in response to mouse wheel or up/down arrows. It should be
	 * overridden to give something sensible. like the scale/100;
	 * 
	 * @return the z step (changes to zDist) for moving in and out
	 */
	public float getZStep() {
		return 0.1f;
	}

	/**
	 * Main program for testing. Put the panel on JFrame,
	 * 
	 * @param arg
	 */
	public static void main(String arg[]) {
		// final JFrame testFrame = new JFrame("bCNU 3D Panel Test using
		// JMonkeyLibrary");
		SimulationMain app = createPanel3D();
		app.setShowSettings(false);
		AppSettings settings = new AppSettings(true);
		//settings.setResolution(1440, 920);
		settings.setWidth(1280);
	    settings.setHeight(720);
		settings.setFullscreen(false);
		settings.setVSync(true);
		settings.setSamples(8);
		//settings.setRenderer(AppSettings.LWJGL_OPENGL45);
		settings.setGammaCorrection(true);
		settings.setTitle("Physics Capstone Chaos Simulation");
		app.setSettings(settings);
		app.setPauseOnLostFocus(false);
		app.createCanvas();

		//int canvasWidth = (int) (settings.getWidth() - settings.getWidth() / 5.25);
		//int canvasHeight = settings.getHeight() - settings.getHeight() / 5;
		int canvasWidth = settings.getWidth();
		int canvasHeight = settings.getHeight();
		JmeCanvasContext context = (JmeCanvasContext) app.getContext();
		context.setSystemListener(app);
		Canvas canvas = context.getCanvas();
		Dimension dim = new Dimension(settings.getWidth(), (int) (settings.getHeight()));
		canvas.setSize(dim);
		canvas.setBackground(Color.BLACK);

		JFrameResizable frame = new JFrameResizable();
		frame.setTitle("Physics Capstone Chaos Simulation");
		Dimension dim2 = new Dimension(canvasWidth, canvasHeight + 35);
		frame.setPreferredSize(dim2);
		frame.setSize(dim2);

		// JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				app.stop();
			}
		});

		GUI = new RightSideGUI(app);
		topGUI = new TopGUI();
		LeftSideGUI leftSideGUI = new LeftSideGUI();
		leftSideGUI.setSize(200,200);
		analyticGUI = leftSideGUI;
		TesterMethods.setAnalyticalField(analyticGUI.jTextArea1);
		GUI.setSize(200,200);
		topGUI.setSize(400,85);
		//GUI.setOpaque(false);
		//GUI.setBackground(new Color(150,150,150,100));
		JInternalFrame testFrame = new JInternalFrame();
		JInternalFrame leftSideGUIFrame = new JInternalFrame();
		//GUI.setLocation(frame.getWidth() - GUI.getWidth() - 10, 0);
		frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            
                //int canvasWidth = (int) (frame.getWidth() + frame.getWidth() / 4);
    			//int canvasHeight = frame.getHeight() + frame.getHeight() / 4 - 45;
    			//Dimension dim = new Dimension(canvasWidth, canvasHeight);
    			canvas.setSize(new Dimension(frame.getWidth(), frame.getHeight()));
    			//settings.setResolution(frame.getWidth(), frame.getHeight());
    			//app.setSettings(settings);
    			//app.restart();
    			//test.setSize(dim);
    			//test.setPreferredSize(dim);
    			//settings.setResolution(canvasWidth, canvasHeight);
    			//GUI.setSize(frame.getSize());
    			testFrame.setLocation(frame.getWidth() - GUI.getWidth() - 20, 0);
    			topGUI.setLocation(frame.getWidth()/2 - topGUI.getWidth()/2,0);
    			leftSideGUIFrame.setLocation(0,frame.getHeight()/2);
            }
        });
		// frame.getContentPane().add(panel);
		//GUI = new Panel3DGUI();
		app.setupHUD();
		testFrame.add(GUI);
		leftSideGUIFrame.add(leftSideGUI);
		testFrame.setSize(GUI.getWidth(), GUI.getHeight() + 45);
		leftSideGUIFrame.setSize(leftSideGUI.getSize());
		testFrame.setResizable(true);
		leftSideGUIFrame.setResizable(true);
		leftSideGUIFrame.setVisible(true);
		testFrame.setVisible(true);
		frame.add(testFrame);
		frame.add(leftSideGUIFrame);
		frame.add(topGUI);
		frame.add(canvas);
		frame.setLayout(null);
		frame.pack();
		frame.getContentPane().setBackground(Color.BLUE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//frame.setResizable(false);
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				app.startCanvas(true);
			}
		});
	}

	private static SimulationMain createPanel3D() {

		final float xdist = -100f;
		final float ydist = 0f;
		final float zdist = -1600f;

		final float thetax = 45f;
		final float thetay = 45f;
		final float thetaz = 45f;
		return new SimulationMain(thetax, thetay, thetaz, xdist, ydist, zdist);
	}

	// Getters and Setters
	public static void setRotX(float x) {
		rotX = x / coordinateSize.x;
	}

	public static void setRotY(float y) {
		rotY = y / coordinateSize.y;
	}

	public static void setRotZ(float z) {
		rotZ = z / coordinateSize.z;
	}

	/**
	 * Print the panel. No default implementation.
	 */
	public void print() {
	}

	/**
	 * Snapshot of the panel. No default implementation.
	 */
	public void snapshot() {
	}

	public ChaseCamera getChaseCamera() {
		return camera;
	}

	public RightSideGUI getRightSideGUI() {
		return GUI;
	}

}
