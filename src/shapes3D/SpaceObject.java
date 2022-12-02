package shapes3D;

import java.util.ArrayList;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

public class SpaceObject extends Geometry3D{

	private Vector3f velocity = new Vector3f(0,0,0);
	private Vector3f initialVelocity = new Vector3f(0,0,0);
	private Vector3f initialPosition = new Vector3f(0,0,0);
	protected double mass = 0; //in kg
	protected double radius = 0; //in km
	protected Trail trail;
	private boolean lockedSize = false;
	private boolean initialConditionsSet = false;
	private double axisRotationSpeed = 0;
	public SpaceObject(String name, Mesh model) {
		super(name, model);
		trail = new Trail(ColorRGBA.White);
	}

	public void initalizeShape() {
		
	}
	
	public ArrayList<Vector3f> getInitialConditions() {
		ArrayList<Vector3f> temp = new ArrayList<Vector3f>();
		temp.add(initialPosition);
		temp.add(initialVelocity);
		return temp;
	}
	
	public void setInitialConditions(ArrayList<Vector3f> initial) {
		initialConditionsSet = true;
		if(initial.size() != 2)
			throw new RuntimeException("SpaceObject needs initial position and velocity");
		initialPosition = initial.get(0);
		this.setToScalePosition(initialPosition);
		initialVelocity = initial.get(1);
		this.setVelocity(initialVelocity);
		
	}
	
	public Vector3f getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector3f v) {
		if (!initialConditionsSet)
			throw new RuntimeException("Must Set Initial Conditions First!");
		velocity = v; //in km/s
	}
	
	public Vector3f getToScalePosition() {
		return this.getLocalTranslation().divide(this.getGlobalScale());
		
	}
	
	//in degrees/s
	public void setOrbitalAxisRotationSpeed(double rotation) {
		axisRotationSpeed = rotation;
	}
	
	public void setToScalePosition(Vector3f pos) {
		if (!initialConditionsSet)
			throw new RuntimeException("Must Set Initial Conditions First!");
		this.getTrail().addPosToTrail(pos);
		this.setLocalTranslation(pos.mult(this.getGlobalScale()));
	}

	public void update(float tpf) {
		Quaternion rotation = new Quaternion();
		rotation.fromAngleAxis((float) (axisRotationSpeed * tpf), new Vector3f(0, 0, 1));
		this.setLocalRotation(this.getLocalRotation().mult(rotation));
	}
	
	public double getMass() {
		return mass;
	}
	
	public void lockSize(boolean lock) {
		lockedSize = lock;
	}
	
	public boolean isSizeLocked() {
		return lockedSize;
	}
	
	public void attachToNode(Node node) {
		node.attachChild(this);
		node.attachChild(this.getTrail());
		Geometry3D outline = this.getOutline();
		while(outline != null) {
			node.attachChild(outline);
			outline = outline.getOutline();
		}
	}
	
	@Override
	public SpaceObject clone() {
		SpaceObject temp = (SpaceObject) super.clone();
		temp.mass = this.getMass();
		temp.initialPosition = this.initialPosition.clone();
		temp.initialVelocity = this.initialVelocity.clone();
		temp.velocity = this.velocity.clone();
		return temp;
	}
	
	@Override
	public void setGlobalScale(Vector3f vec) {
		globalScale = vec;
		if(!lockedSize) {
			super.setGlobalScale(vec);
		}
	}
	
	public Trail getTrail() {
		if(trail == null) {
			trail = new Trail(ColorRGBA.White);
		}
		return trail;
	}
	
}
