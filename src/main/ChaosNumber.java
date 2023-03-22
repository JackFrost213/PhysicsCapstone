package main;

import net.wcomohundro.jme3.math.Vector3d;

public class ChaosNumber {

	private Vector3d position = new Vector3d();
	private Vector3d lyapunovExponents = new Vector3d();
	private Vector3d lyapunovExponentsScaled = new Vector3d();
	
	public ChaosNumber() {
		position = new Vector3d((Math.random()*150000000),(double)(Math.random()*150000000),(double)(Math.random()*3)+-2);
	}
	
	public ChaosNumber(int shape) {
		if(shape == 1) {
		position = new Vector3d((double)(Math.random()*150000000),(double)(Math.random()*150000000),0);
		position.z = ((double)(position.x/150000000)*10 );
		}
		if(shape == 2) {
			position = new Vector3d((double)(Math.random()*150000000),(double)(Math.random()*150000000),0);
			position.z = ((double)Math.sqrt(Math.pow((position.x/150000000),2) + Math.pow((position.y/150000000),2))*10);
		}
		if(shape == 3) {
			position = new Vector3d((double)(Math.random()*150000000),(double)(Math.random()*150000000),0);
			position.z = ((double)Math.sin(Math.sqrt(Math.pow((position.x/150000000),2) + Math.pow((position.y/150000000),2))*10));
		}
	}
	
	public ChaosNumber(int shape, Vector3d pos) {
		position = pos;
		if(shape == 1) {
		position.z = ((double)(position.x/150000000)*10);
		}
		if(shape == 2) {
			position.z = ((double)Math.sqrt(Math.pow((position.x/150000000),2) + Math.pow((position.y/150000000),2))*10);
		}
		if(shape == 3) {
			position.z = ((double)Math.sin(Math.sqrt(Math.pow((position.x/150000000),2) + Math.pow((position.y/150000000),2))*10));
		}
		if(shape == 4) {
			position.z = ((double)Math.sqrt(Math.pow((position.x/150000000),2) + Math.pow((position.y/150000000),2))*10);
		}
	}
	
	public Vector3d getLyapunovExponents() {
		return lyapunovExponents;
	}
	
	public Vector3d getLyapunovExponentsScaled() {
		return lyapunovExponentsScaled;
	}
	
	public void setLyapunovExponents(Vector3d temp) {
		lyapunovExponents = temp;
	}
	
	public Vector3d getPosition() {
		return position;
	}
	
}
