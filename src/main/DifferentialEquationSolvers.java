package main;

import java.util.ArrayList;

import com.jme3.math.Vector3f;

import shapes3D.SpaceObject;

public class DifferentialEquationSolvers {

	private static double gravitationalConstant = 6.67430E-11;

	// distance from earth to sun 149,192,626.18 km
	public static void eulerApproximation(ArrayList<SpaceObject> spaceObjects, float timeElapsed, float timeStep) {
		float timeCalculated = 0;
		while (timeCalculated + timeStep < timeElapsed) {
			eulerCalculation(spaceObjects, timeStep);
			timeCalculated += timeStep;

		}
		eulerCalculation(spaceObjects, timeElapsed - timeCalculated); // does the last calculation to current time

	}

	public static void rungeKuttaApproximation(ArrayList<SpaceObject> spaceObjects, float timeElapsed, float timeStep) {
		float timeCalculated = 0;
		while (timeCalculated + timeStep < timeElapsed) {
			rungeKuttaCalculation(spaceObjects, timeStep);
			timeCalculated += timeStep;

		}
		rungeKuttaCalculation(spaceObjects, timeElapsed - timeCalculated); // does the last calculation to current time

	}

	private static void rungeKuttaCalculation(ArrayList<SpaceObject> spaceObjects, float timeStep) {
		for (SpaceObject spaceObject : spaceObjects) {
			Vector3f accelerationVector = new Vector3f(0, 0, 0);
			Vector3f k1 = new Vector3f(0,0,0);
			Vector3f k2 = new Vector3f(0,0,0);
			Vector3f k3 = new Vector3f(0,0,0);
			Vector3f k4 = new Vector3f(0,0,0);
			for (SpaceObject spaceObject2 : spaceObjects) {
				if (spaceObject != spaceObject2) {
					accelerationVector.add(getAccelerationComponentOf2On1(spaceObject,spaceObject2)); // in m/s^2
				}
			}
			k1 = accelerationVector;
			Vector3f newObj1PositionChange = (k1.add(k2.mult(2)).add(k3.mult(2)).add(k4)).mult(timeStep/6);
			Vector3f newObj1Position = spaceObject.getToScalePosition().add(newObj1PositionChange);
		}
	}
	
	// dy/dt = acc, y(t) = v
	private static void RK4(ArrayList<SpaceObject> spaceObjects) {
		//k1_1 = acceleration(t=0,v=v initial)
		//k2_1 = acceleration(timeStep/2, v + timeStep*k1/2) 
		//k3_1 = acceleration(timeStep/2, v + timeStep*k2/2)
		//how to calculate acceleration at timeStep/2 where the velocity = v + timeStep * k1/2, v2
		
		//k1_2 = velocity(t=0, p = initial)
		//k2_2 = velocity(timeStep/2, p + timeStep*k1/2)
		//k3_2 = velocity(timeStep/2, p + timeStep*k2/2)
		//how to calculate velocity at position p + timeStep*k1/2 = p2
		
		//at position p2, the velocity is v2, and the acceleration is some function of all the p2s
		//acceleration(timeStep/2, p2)
		//k1_2 = initial_velocity
		//p2 = position at timeStep/2 = initial_position + timeStep*k1_2/2
		//a2 = acceleration at timeStep/2 = f(p2)
		//v2 = velocity at timeStep/2 = initial_velocity + timeStep*initial_acceleration/2
		//k2_1 = acceleration(timeStep/2, p2)
		
		
		
		
		
		//acceleration is a function of position -> a(t,p)
		//I want to get that hypothetical p(t,v) or y(t+1)
		//there is also a v(t,a)
		//k1v = initial_acceleration
		//k1x = initial_velocity
		//k2x = velocity(t+dt/2, initial_position + dt*k1x/2, initial_velocity + dt*k1v/2)
		//k2v = acceleration(t+dt/2, initial_velocity + dt*k1v/2)
		//The object has a velocity 
	}
	
	private static ArrayList<SpaceObject> getTimeSteppedSpaceObjects(ArrayList<SpaceObject> spaceObjects, float timeStep, int rkNumber){
		ArrayList<SpaceObject> spaceObjectsCopy = new ArrayList<SpaceObject>();
		for(SpaceObject s : spaceObjects) {
			spaceObjectsCopy.add(s.clone());
		}
		
		if(rkNumber == 1) {
			//I need the velocity at current time
		}
		
		return spaceObjectsCopy;
	}
	
	private static Vector3f getAccelerationComponentOf2On1(SpaceObject obj1, SpaceObject obj2) {
		double distanceFrom1To2InKM = obj1.getToScalePosition()
				.distance(obj2.getToScalePosition());
		double distanceFrom1To2InM = distanceFrom1To2InKM * 1000;
		double acc_of_1_from_2 = (gravitationalConstant * (obj2.getMass()))
				/ (Math.pow(distanceFrom1To2InM, 2));
		Vector3f obj_1_to_2_vec = (obj2.getToScalePosition()
				.subtract(obj1.getToScalePosition())).normalize();
		return obj_1_to_2_vec.mult((float) acc_of_1_from_2); // in m/s^2
	}
	
	private static void eulerCalculation(ArrayList<SpaceObject> spaceObjects, float timeStep) {
		for (SpaceObject spaceObject : spaceObjects) {
			ArrayList<Vector3f> accelerationComponents = new ArrayList<Vector3f>();
			for (SpaceObject spaceObject2 : spaceObjects) {
				if (spaceObject != spaceObject2) {
					accelerationComponents.add(getAccelerationComponentOf2On1(spaceObject,spaceObject2)); // in m/s^2
				}
			}
			Vector3f accelerationVector = new Vector3f(0, 0, 0);
			for (Vector3f acc : accelerationComponents) {
				accelerationVector = accelerationVector.add(acc);
			}

			Vector3f accelerationVectorPre = accelerationVector;
			accelerationVector = accelerationVector.mult(timeStep / 1000);
			Vector3f new_obj_1_velocity = spaceObject.getVelocity().add(accelerationVector); // m/s
			spaceObject.setVelocity(new_obj_1_velocity); // velocity in km/s
			spaceObject
					.setToScalePosition(spaceObject.getToScalePosition().add(spaceObject.getVelocity().mult(timeStep)));
		}

	}

}
