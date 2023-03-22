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
import shapes3D.Geometry3D;
import shapes3D.Trail;

public class Earth extends SpaceObject{
	
	public Earth() {
		super("Sphere", new Sphere(32, 32, 1));
		this.mass = 5.97219E24; //kg
		this.radius = 6371.07103; //km
		this.setName("Earth");
		//rotates this object upwards
		Quaternion rotation4 = new Quaternion();
		rotation4.fromAngleAxis((float) (-90 * (Math.PI / 180)), new Vector3f(1, 0, 0));
		Quaternion rotation5 = new Quaternion();
		rotation5.fromAngleAxis((float) (-23.5 * (Math.PI / 180)), new Vector3f(0, 1, 0));
		rotation4 = rotation4.mult(rotation5);
		this.setLocalRotation(rotation4);
		
		this.setOrbitalAxisRotationSpeed(7.27E-5);
		//23.5 degrees
		
		//this.rotate(rotation4);
		trail = new Trail(ColorRGBA.Blue);
		
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
		
		Texture texture = assetManager.loadTexture("Textures/Earth8kDay.jpg");
		texture.setWrap(Texture.WrapMode.EdgeClamp);
		
		this.setLocalScale((float)this.getRadius());
		this.setShadowMode(ShadowMode.CastAndReceive);
		this.getMaterial().setTexture("DiffuseMap", texture);
		this.getMaterial().setTexture("NormalMap", assetManager.loadTexture("Textures/8k_earth_normal_map.png"));
		this.getMaterial().setTexture("SpecularMap", assetManager.loadTexture("Textures/8k_earth_specular_map.png"));
		this.getMaterial().setFloat("Shininess",250f);
		this.getMaterial().setBoolean("UseMaterialColors",true);
		this.getMaterial().setColor("Diffuse",ColorRGBA.White);
		this.getMaterial().setColor("Specular", ColorRGBA.White);
		this.getMaterial().setColor("Ambient", ColorRGBA.DarkGray);
		((Sphere) this.getMesh()).setTextureMode(Sphere.TextureMode.Projected);
		
		Geometry3D lightOfEarth = new Geometry3D("Sphere",new Sphere(32,32,1));
		lightOfEarth.setName("Outline");
		Material mat3 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		mat3.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Earth8kNight.jpg"));
		mat3.setTexture("NormalMap", assetManager.loadTexture("Textures/8k_earth_normal_map.png"));
		((Sphere) lightOfEarth.getMesh()).setTextureMode(Sphere.TextureMode.Projected);
		//mat3.setTexture("SpecularMap", assetManager.loadTexture("Textures/8k_earth_specular_map.png"));
		//mat3.setFloat("Shininess",64f);
		ColorRGBA white = ColorRGBA.White;
		white = white.mult(1000).setAlpha(0.001f);
		
		mat3.setColor("Diffuse",white);
		//mat3.setColor("Specular", ColorRGBA.White);
		mat3.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		//mat3.setTexture("AlphaMap", assetManager.loadTexture("Textures/Earth_GlowMap_Trans.png"));
		mat3.setBoolean("UseMaterialColors", true);
		lightOfEarth.setShadowMode(ShadowMode.Off);
		lightOfEarth.setQueueBucket(Bucket.Transparent);
		lightOfEarth.setMaterial(mat3);
		lightOfEarth.setLocalScale((float)(this.getRadius()));
		lightOfEarth.setInvertedLighting(true);
		this.setOutline(lightOfEarth);
		
		Geometry3D sphereOutline = new Geometry3D("Sphere",new Sphere(32,32,1));
		sphereOutline.setName("Outline");
		Material mat2 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		sphereOutline.setMaterial(mat2);
		sphereOutline.setLocalScale((float)(this.getRadius()+0.01*this.getRadius()));
		mat2.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		((Sphere) sphereOutline.getMesh()).setTextureMode(Sphere.TextureMode.Projected);
		mat2.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Earth8KClouds.jpg"));
		mat2.setTexture("AlphaMap", assetManager.loadTexture("Textures/Earth8KClouds.jpg"));
		mat2.setFloat("AlphaDiscardThreshold", 0.05f);
		sphereOutline.setQueueBucket(Bucket.Transparent);
		mat2.setBoolean("UseMaterialColors", true);
		mat2.setColor("Diffuse", ColorRGBA.White);
		mat2.setColor("Ambient", ColorRGBA.DarkGray);
		lightOfEarth.setOutline(sphereOutline);
		
		Geometry3D sphereOutline2 = new Geometry3D("Sphere",new Sphere(32,32,1));
		sphereOutline2.setName("Outline");
		Material mat4 = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		ColorRGBA blue = ColorRGBA.Blue;
		sphereOutline2.setMaterial(mat4);
		sphereOutline2.setLocalScale((float)(this.getRadius()+0.06*this.getRadius()));
		mat4.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		sphereOutline2.setQueueBucket(Bucket.Transparent);
		blue.setAlpha(0.05f);
		mat4.setBoolean("UseMaterialColors", true);
		mat4.setColor("Diffuse", blue);
		mat4.setColor("Ambient", ColorRGBA.DarkGray);
		sphereOutline.setOutline(sphereOutline2);
		
		
		
	}

}

