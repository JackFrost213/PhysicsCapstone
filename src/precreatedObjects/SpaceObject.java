package precreatedObjects;

import java.util.ArrayList;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

import net.wcomohundro.jme3.math.Vector3d;
import shapes3D.Geometry3D;
import shapes3D.Trail;

public class SpaceObject extends Geometry3D{

	private Vector3d initialVelocity = new Vector3d(0,0,0);
	private Vector3d initialPosition = new Vector3d(0,0,0);
	private Vector3d precisePosition = new Vector3d(0,0,0);
	private Vector3d velocity = new Vector3d(0,0,0);
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
	
	public ArrayList<Vector3d> getInitialConditions() {
		ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
		temp.add(initialPosition);
		temp.add(initialVelocity);
		return temp;
	}
	
	public void setInitialConditions(ArrayList<Vector3d> initial) {
		initialConditionsSet = true;
		if(initial.size() != 2)
			throw new RuntimeException("SpaceObject needs initial position and velocity");
		initialPosition = initial.get(0);
		this.setToScalePosition(initialPosition);
		initialVelocity = initial.get(1);
		this.setVelocity(initialVelocity);
		
	}
	
	public Vector3d getVelocity() {
		return velocity;
	}
	
	public void setVelocity(Vector3d v) {
		if (!initialConditionsSet)
			throw new RuntimeException("Must Set Initial Conditions First!");
		velocity = v; //in km/s
	}
	
	public Vector3d getToScalePosition() {
		return this.precisePosition;
		
	}
	
	public Vector3f getToScalePositionFloat() {
		return this.getLocalTranslation().divide(this.getGlobalScale());
		
	}
	
	//in degrees/s
	public void setOrbitalAxisRotationSpeed(double rotation) {
		axisRotationSpeed = rotation;
	}
	
	public void setToScalePosition(Vector3d pos) {
		if (!initialConditionsSet)
			throw new RuntimeException("Must Set Initial Conditions First!");
		this.precisePosition = pos;
		Vector3f lessPrecisePos = new Vector3f((float)pos.x,(float)pos.y,(float)pos.z);
		this.getTrail().addPosToTrail(lessPrecisePos);
		this.setLocalTranslation(lessPrecisePos.mult(this.getGlobalScale()));
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
		temp.setToScalePosition(this.getToScalePosition());
		return temp;
	}
	
	@Override
	public void setGlobalScale(Vector3f vec) {
		globalScale = vec;
		if(!lockedSize) {
			super.setGlobalScale(vec);
		}
	}
	
	public void setMass(double m) {
		this.mass = m;
	}
	
	public Trail getTrail() {
		if(trail == null) {
			trail = new Trail(ColorRGBA.White);
		}
		return trail;
	}

	public double getRadius() {
		return radius;
	}
	
}
