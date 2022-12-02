package precreatedObjects;

import java.util.ArrayList;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

import main.SimulationMain;
import shapes3D.Geometry3D;
import shapes3D.QuadShape;
import shapes3D.ScalableLightControl;
import shapes3D.Scene;
import shapes3D.SpaceObject;
import shapes3D.Trail;

public class Sun extends SpaceObject{

	private ArrayList<PointLight> point_lights;
	private AmbientLight ambient;
	
	public Sun() {
		super("Sphere", new Sphere(32, 32, 1));
		this.mass = 1.9891E30; //kg
		this.radius = 696347.055; //km
		this.setName("Sun");
		Quaternion rotation4 = new Quaternion();
		rotation4.fromAngleAxis((float) (-90 * (Math.PI / 180)), new Vector3f(1, 0, 0));
		this.setLocalRotation(rotation4);
		
		this.setOrbitalAxisRotationSpeed(-2.972E-6);
		trail = new Trail(ColorRGBA.Red);
		point_lights = new ArrayList<PointLight>();
		initalizeShape();
	}
	
	@Override
	public void initalizeShape() {
		AssetManager assetManager = SimulationMain.assetManagerExternal;
		this.setLocalScale((float)this.radius);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		this.setMaterial(mat);
		this.updateModelBound();
		
		this.getMaterial().setColor("GlowColor", ColorRGBA.White);
		//this.getMaterial().setBoolean("UseMaterialColors",true);
		Texture texture2 = assetManager.loadTexture("Textures/Sun8k.jpg");
		((Sphere) this.getMesh()).setTextureMode(Sphere.TextureMode.Projected);
		this.getMaterial().setTexture("ColorMap", texture2);
		mat.setTexture("GlowMap", assetManager.loadTexture("Textures/Sun8k.jpg"));
		//this.getMaterial().setTexture("NormalMap", texture2);
		Quaternion rotation5 = new Quaternion();
		rotation5.fromAngleAxis((float) (0), new Vector3f(0, 0, 1));
		this.setLocalRotation(this.getLocalRotation().mult(rotation5));
		
	    QuadShape quad = new QuadShape();
	    Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	    mat2.setTexture("ColorMap",assetManager.loadTexture("Textures/SunHalo2.png"));
	    Geometry3D geo = new Geometry3D("quad", quad);
	    geo.setMaterial(mat2);
	    geo.setName("Outline");
	    mat2.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		geo.setQueueBucket(Bucket.Transparent);
	    geo.setLocalScale((float)(1.75*this.radius));
	    geo.updateModelBound();
	    this.setOutline(geo);
	    BillboardControl bill = new BillboardControl();
	    bill.setAlignment(BillboardControl.Alignment.Camera);
	    geo.addControl(bill);
		
		
		/*Geometry3D sphereOutline = new Geometry3D("Sphere",new Sphere(32,32,1));
		sphereOutline.setName("Outline");
		Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		ColorRGBA yellow = ColorRGBA.Yellow;
		sphereOutline.setMaterial(mat2);
		sphereOutline.setLocalScale((float)(this.radius+0.16*this.radius));
		mat2.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		sphereOutline.setQueueBucket(Bucket.Transparent);
		yellow.setAlpha(0.25f);
		mat2.setColor("Color", yellow);
		this.setOutline(sphereOutline);
		*/
		
	    PointLight pointLightCenter = new PointLight();
	    pointLightCenter.setColor(ColorRGBA.White.mult(2.8f));
	    pointLightCenter.setRadius(10000000000000f);
	    pointLightCenter.setPosition(this.getToScalePosition());
	    pointLightCenter.setName("Lamp");
	    point_lights.add(pointLightCenter);
	    PointLight pointLightTop = new PointLight();
	    pointLightTop.setColor(ColorRGBA.White.mult(2.8f));
	    pointLightTop.setRadius(10000000000000f);
	    pointLightTop.setPosition(this.getToScalePosition().add(new Vector3f(0,(float) this.radius,0)));
	    pointLightTop.setName("Lamp");
	    point_lights.add(pointLightTop);
	    
	    for(PointLight pointLight : point_lights) {
	    	ScalableLightControl lightControl = new ScalableLightControl(pointLight);
	    	this.addControl(lightControl);
	    }
	    
	    ambient = new AmbientLight();
	    ambient.setColor(ColorRGBA.White.mult(1f));
	}

	public void attachLightEmissions(Node rootNode) {
		for(PointLight pointLight : point_lights) {
			rootNode.addLight(pointLight);
		}
		rootNode.addLight(this.ambient);	
	}
	
	public void update(float tpf) {
		super.update(tpf);
		//System.out.println(point_lights.get(1).getPosition());
	}
	
}
