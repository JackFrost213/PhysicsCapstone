package LorentzSimulationTest;

import java.util.ArrayList;

import net.wcomohundro.jme3.math.Vector3d;
import precreatedObjects.Earth;
import precreatedObjects.SpaceObject;
import shapes3D.SpaceObjectFast;

public class ObjectCreationTimeTest {

	public static void main(String[] args) {
		ArrayList<SpaceObject> spaceObjectsSlow = new ArrayList<SpaceObject>();
		ArrayList<SpaceObjectFast> spaceObjectsFast = new ArrayList<SpaceObjectFast>();
		int samples = 500;
		int iterations = 20;	
		
		ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
		temp.add(new Vector3d(0,0,0));
		temp.add(new Vector3d(0, 0, (float)30.29));
		SpaceObject earth = new Earth();
		earth.setInitialConditions(temp);
		
		
		long startTime1 = System.nanoTime();
		for(int y = 0; y < samples; y++) {
			for(int x = 0; x < iterations; x++) {
				SpaceObject test = earth.clone();
				spaceObjectsSlow.add(test);
			}
			spaceObjectsSlow = new ArrayList<SpaceObject>();
		}
		long endTime1 = System.nanoTime();
		
		
		long startTime2 = System.nanoTime();
		for(int y = 0; y < samples; y++) {
			for(int x = 0; x < iterations; x++) {
				SpaceObjectFast test = new SpaceObjectFast(earth);
				spaceObjectsFast.add(test);
			}
			spaceObjectsFast = new ArrayList<SpaceObjectFast>();
		}
		long endTime2 = System.nanoTime();
		
		
		System.out.println("Execution Time of SpaceObject Class: " + (double)(endTime1-startTime1)/Math.pow(10, 6));
		System.out.println("Execution Time of SpaceObjectFast Class: " + (double)(endTime2-startTime2)/Math.pow(10, 6));
		
	}
	
}
