package shapes3D;

import java.util.ArrayList;

import main.ChaosNumber;
import net.wcomohundro.jme3.math.Vector3d;
import precreatedObjects.SpaceObject;

public class SpaceObjectFast {

	private double mass;
	private Vector3d position;
	private Vector3d velocity;
	private ArrayList<Vector3d> initialCon;
	private double radius;
	private String name;
	
	public SpaceObjectFast() {
		mass = 0;
		position = new Vector3d(0,0,0);
		velocity = new Vector3d(0,0,0);
		initialCon = new ArrayList<Vector3d>();
		radius = 0;
	}
	
	
	public SpaceObjectFast(SpaceObject s) {
		this.mass = s.getMass();
		this.radius = s.getRadius();
		this.position = s.getToScalePosition();
		this.velocity = s.getVelocity();
		initialCon = s.getInitialConditions();
		this.name = s.getName();
	}
	
	
	public void loadSpaceObject(SpaceObject s) {
		this.mass = s.getMass();
		this.radius = s.getRadius();
		this.position = s.getToScalePosition();
		this.velocity = s.getVelocity();
		initialCon = s.getInitialConditions();
		this.name = s.getName();
	}
	
	public Vector3d getToScalePosition() {
		return this.position;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getMass() {
		return this.mass;
	}


	public Vector3d getVelocity() {
		return this.velocity;
	}


	public void setToScalePosition(Vector3d add) {
		this.position = add;
	}
	
	@Override
	public SpaceObjectFast clone() {
		SpaceObjectFast temp = new SpaceObjectFast();
		temp.mass = this.mass;
		temp.name = this.name;
		temp.position = this.position;
		temp.velocity = this.velocity;
		temp.radius = this.radius;
		
		return temp;
	}


	public void setVelocity(Vector3d newVelocity) {
		this.velocity = newVelocity;
	}


	public ArrayList<Vector3d> getInitialConditions() {
		return initialCon;
	}


	public void setInitialConditions(ArrayList<Vector3d> temp) {
		initialCon = temp;
		this.position = temp.get(0);
		this.velocity = temp.get(1);
	}
	
}
