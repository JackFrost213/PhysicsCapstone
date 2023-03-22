package main.runnable;

import java.util.ArrayList;

import main.ChaosGenerator;
import net.wcomohundro.jme3.math.Vector3d;
import precreatedObjects.SpaceObject;
import precreatedScenarios.Simulation;
import precreatedScenarios.SunEarthJamesWebbL4Simulation;
import precreatedScenarios.SunEarthJamesWebbSimulation;

public class StableVelocityChecker {

	public static void main(String[] args) {
		Simulation sim = new SunEarthJamesWebbSimulation(null);
		sim.createInitialItems();
		ArrayList<SpaceObject> spaceObjects = sim.getSpaceObjects();
    	SpaceObject probe = spaceObjects.get(1);
    	
    	double theoreticaldist = spaceObjects.get(0).getToScalePosition().distance(spaceObjects.get(2).getToScalePosition());
    	double theoreticalL2XPos = theoreticaldist * (1 + Math.pow(spaceObjects.get(0).getMass()/(3*spaceObjects.get(2).getMass()), 1.0/3.0));
		theoreticalL2XPos -= theoreticaldist;
		Vector3d theoreticalL2 = new Vector3d(-theoreticalL2XPos, 0,0);
		
		double theoreticalL1XPos = theoreticaldist * (1 - Math.pow(spaceObjects.get(0).getMass()/(3*spaceObjects.get(2).getMass()), 1.0/3.0));
		theoreticalL1XPos -= theoreticaldist;
		Vector3d theoreticalL1 = new Vector3d(-theoreticalL1XPos, 0,0);
    	
		double theoreticalL3XPos = theoreticaldist + theoreticaldist * (1 + (5.0/12.0)*spaceObjects.get(0).getMass()/spaceObjects.get(2).getMass());
		Vector3d theoreticalL3 = new Vector3d(theoreticalL3XPos, 0,0);
		
		Vector3d theoreticalL4 = new Vector3d(0.5f*theoreticaldist,0,Math.sqrt(Math.pow(theoreticaldist, 2) - Math.pow(0.5f*theoreticaldist, 2)));
		Vector3d theoreticalL5 = new Vector3d(0.5f*theoreticaldist,0,-(Math.sqrt(Math.pow(theoreticaldist, 2) - Math.pow(0.5f*theoreticaldist, 2))));
		
    	double uncertainty = 0.0001;
    	Vector3d stableVelocity = ChaosGenerator.getMostStableVelocityForPosition(theoreticalL1,spaceObjects,probe,-50,50,-50,50,uncertainty);
		System.out.println("Theoretical L1 Position: " + theoreticalL1);
    	System.out.println("The most stable velocity for probe in Lagrange Point L1 is: " + stableVelocity + " +/- " + uncertainty);
    	
		stableVelocity = ChaosGenerator.getMostStableVelocityForPosition(theoreticalL2,spaceObjects,probe,-50,50,-50,50,uncertainty);
		System.out.println("\nTheoretical L2 Position: " + theoreticalL2);
		System.out.println("The most stable velocity for probe in Lagrange Point L2 is: " + stableVelocity + " +/- " + uncertainty);
		
		stableVelocity = ChaosGenerator.getMostStableVelocityForPosition(theoreticalL3,spaceObjects,probe,-50,50,-50,50,uncertainty);
		System.out.println("\nTheoretical L3 Position: " + theoreticalL3);
		System.out.println("The most stable velocity for probe in Lagrange Point L3 is: " + stableVelocity + " +/- " + uncertainty);
		
		stableVelocity = ChaosGenerator.getMostStableVelocityForPosition(theoreticalL4,spaceObjects,probe,-50,50,-50,50,uncertainty);
		System.out.println("\nTheoretical L4 Position: " + theoreticalL4);
		System.out.println("The most stable velocity for probe in Lagrange Point L4 is: " + stableVelocity + " +/- " + uncertainty);
		
		stableVelocity = ChaosGenerator.getMostStableVelocityForPosition(theoreticalL5,spaceObjects,probe,-50,50,-50,50,uncertainty);
		System.out.println("\nTheoretical L5 Position: " + theoreticalL5);
		System.out.println("The most stable velocity for probe in Lagrange Point L5 is: " + stableVelocity + " +/- " + uncertainty);
		
	}
	
}
