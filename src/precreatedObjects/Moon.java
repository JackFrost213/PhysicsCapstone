package precreatedObjects;

import java.awt.Color;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

import main.SimulationMain;
import main.Support3DOther;
import shapes3D.Trail;

public class Moon extends SpaceObject{
	
	public Moon() {
		super("Sphere", new Sphere(32, 32, 1));
		this.mass = 7.34767309E22; //kg
		this.radius = 1737.4; //km
		this.setName("Moon");
		trail = new Trail(ColorRGBA.LightGray);
		
		try {
		initalizeShape();
		}catch(Exception e) {
			
		}
	}

	@Override
	public void initalizeShape() {
		AssetManager assetManager = SimulationMain.assetManagerExternal;
		
		Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		this.setLocalScale((float)1);
		this.setMaterial(mat);
		this.updateModelBound();
		
		Texture texture = assetManager.loadTexture("Textures/Moon.jpg");
		texture.setWrap(Texture.WrapMode.EdgeClamp);
		
		this.setLocalScale((float)this.getRadius());
		this.setShadowMode(ShadowMode.CastAndReceive);
		this.getMaterial().setTexture("DiffuseMap", texture);
		this.getMaterial().setBoolean("UseMaterialColors",true);
		this.getMaterial().setColor("Diffuse",ColorRGBA.White);
		this.getMaterial().setColor("Specular",ColorRGBA.White);
		this.getMaterial().setColor("Ambient", ColorRGBA.DarkGray);
		
	}

}
