package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import guiStuff.RightSideGUI;
import precreatedObjects.Earth;
import precreatedObjects.SpaceObject;
import shapes3D.Geometry3D;

public class Main extends SimpleApplication{
	public static void main(String[] args)
	{
	    Main app = new Main();
	    
	    Frame _frame = new JFrame();
	    _frame.setLocationRelativeTo(null);
	    _frame.setVisible(true);
	    
	    AppSettings settings = new AppSettings(true);
	    app.setShowSettings(false);
		//settings.setResolution(1440, 920);
		settings.setWidth(1280);
	    settings.setHeight(720);
	    settings.setUseInput(false);
		settings.setFullscreen(false);
		settings.setVSync(true);
		settings.setSamples(8);
		//settings.setRenderer(AppSettings.LWJGL_OPENGL45);
		settings.setGammaCorrection(true);
		settings.setTitle("Physics Capstone Chaos Simulation");
		app.setSettings(settings);
		app.setPauseOnLostFocus(false);
	    
	    _frame.setSize(new Dimension(settings.getWidth(), settings.getHeight()));
	    _frame.setResizable(true);
	    
	    app.createCanvas();
	    
	    JmeCanvasContext _canvasCtx = (JmeCanvasContext) app.getContext();
	    _canvasCtx.setSystemListener(app);
	    Canvas _canvas = _canvasCtx.getCanvas();
	    _canvas.setSize(new Dimension(settings.getWidth() + settings.getWidth() / 4, settings.getHeight() + settings.getHeight()/4));
	    _frame.setLayout(null);
	    //_frame.add(_canvas);
	    
		//GUI.setOpaque(false);
		//GUI.setBackground(new Color(150,150,150,100));
		JInternalFrame testFrame = new JInternalFrame();
		//GUI.setLocation(frame.getWidth() - GUI.getWidth() - 10, 0);
		// frame.getContentPane().add(panel);
		//GUI = new Panel3DGUI();
		testFrame.setSize(200, 200 + 45);
		testFrame.setResizable(true);
		testFrame.setVisible(true);
	    
		_frame.add(_canvas);
	    _frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	
               // int canvasWidth2 = (int) (_frame.getWidth() + _frame.getWidth() / 4);
    			//int canvasHeight2 = _frame.getHeight() + _frame.getHeight() / 4;
            	int canvasWidth2 = _frame.getWidth();
            	int canvasHeight2 = _frame.getHeight();
    			Dimension dim3 = new Dimension(canvasWidth2, canvasHeight2);
    			_canvas.setSize(dim3);
            }
        });
	    _frame.pack();
	    
	    app.startCanvas();
	}

	@Override
	public void simpleInitApp()
	{
		ColorRGBA colorRGBA = ColorRGBA.Red;
		Sphere s = new Sphere(16, 16, 1);
		Geometry3D geom = new Geometry3D("Sphere", s);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", colorRGBA);
		geom.setLocalScale(3);
		geom.setLocalTranslation(new Vector3f(0, 0, 0));

		geom.setMaterial(mat);
		geom.updateModelBound();

		rootNode.attachChild(geom);
	}

	@Override
	public void simpleUpdate(float tpf)
	{
	    
	}

	@Override
	public void simpleRender(RenderManager rm)
	{
	    
	}
}
