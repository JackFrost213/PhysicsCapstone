package precreatedObjects;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

import main.SimulationMain;
import shapes3D.SpaceObject;
import shapes3D.Trail;

public class Sun_EarthSize extends Sun{
	
	public Sun_EarthSize() {
		super();
		this.mass = 5.97219E24; //kg
		this.radius = 6371.07103; //km
		this.setName("Sun_EarthSize");
		this.setLocalScale((float)this.radius);
		this.getOutline().setLocalScale((float)(1.75*this.radius));
	}
}
