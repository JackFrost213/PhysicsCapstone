package precreatedObjects;

import java.awt.Color;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Mesh;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

import main.SimulationMain;
import main.Support3DOther;
import net.wcomohundro.jme3.math.Vector3d;
import shapes3D.Geometry3D;
import shapes3D.QuadShape;
import shapes3D.Trail;

public class JamesWebbSpaceTelescope extends SpaceObject {

	SpaceObject orbiter;
	public String dataTakingName;
	double distanceToOrbiter;

	public JamesWebbSpaceTelescope() {
		super("quad", new QuadShape());
		this.mass = 6500; // kg
		this.radius = 1; // km
		this.setName("James Webb Space Telescope");
		trail = new Trail(ColorRGBA.LightGray);

		try {
			initalizeShape();
		} catch (Exception e) {

		}
	}

	public void activateBoosters(double dist, SpaceObject other) {
		distanceToOrbiter = dist;
		orbiter = other;
	}

	@Override
	public void update(float tpf) {
		if(tpf > 0) {
		super.update(tpf);
		}

		if (orbiter != null) {
			double actualDistance = this.getToScalePosition().subtract(orbiter.getToScalePosition()).length();

			if (actualDistance > distanceToOrbiter && !(actualDistance > 1.1*distanceToOrbiter)) {
				Vector3d direction = orbiter.getToScalePosition().subtract(this.getToScalePosition()).normalize();
				this.setVelocity(this.getVelocity().add(direction.mult((double) 0.01)));
			} else if (actualDistance < distanceToOrbiter && !(actualDistance < 0.9*distanceToOrbiter)) {
				Vector3d direction = orbiter.getToScalePosition().subtract(this.getToScalePosition()).mult(-1)
						.normalize();
				this.setVelocity(this.getVelocity().add(direction.mult((double) 0.01)));
			}
		}

	}

	@Override
	public void initalizeShape() {
		AssetManager assetManager = SimulationMain.assetManagerExternal;

		Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setTexture("ColorMap", assetManager.loadTexture("Textures/Telescope.png"));
		this.setName("James Webb");
		this.setMaterial(mat2);
		mat2.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		this.setQueueBucket(Bucket.Transparent);
		this.setLocalScale((float) (this.getRadius()));
		this.updateModelBound();
		BillboardControl bill = new BillboardControl();
		bill.setAlignment(BillboardControl.Alignment.Camera);
		this.addControl(bill);

	}

}
