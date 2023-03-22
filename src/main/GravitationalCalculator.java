package main;

import java.util.ArrayList;

import net.wcomohundro.jme3.math.Vector3d;
import precreatedObjects.SpaceObject;
import shapes3D.SpaceObjectFast;

public class GravitationalCalculator {
	
	private static double gravitationalConstant = 6.67430E-11;
	
	public static Vector3d getAccelerationComponentOf2On1(SpaceObject obj1, SpaceObject obj2) {
		double distanceFrom1To2InKM = obj1.getToScalePosition().distance(obj2.getToScalePosition());
		double distanceFrom1To2InM = distanceFrom1To2InKM * 1000;
		double acc_of_1_from_2 = (gravitationalConstant * (obj2.getMass())) / (Math.pow(distanceFrom1To2InM, 2));
		Vector3d obj_1_to_2_vec = (obj2.getToScalePosition().subtract(obj1.getToScalePosition())).normalize();
		return obj_1_to_2_vec.mult((double) acc_of_1_from_2); // in m/s^2
	}

	public static Vector3d getAccelerationComponentOfAllObjectsOn1(SpaceObject obj1,
			ArrayList<SpaceObject> spaceObjects) {
		Vector3d accVec = new Vector3d();
		for (SpaceObject s : spaceObjects) {
			if(!s.getName().equals(obj1.getName())) {
			double distanceFrom1To2InKM = obj1.getToScalePosition().distance(s.getToScalePosition());
			double distanceFrom1To2InM = distanceFrom1To2InKM * 1000;
			double acc_of_1_from_2 = (gravitationalConstant * (s.getMass())) / (Math.pow(distanceFrom1To2InM, 2));
			Vector3d obj_1_to_2_vec = (s.getToScalePosition().subtract(obj1.getToScalePosition())).normalize();
			accVec = accVec.add(obj_1_to_2_vec.mult((double) acc_of_1_from_2)); // in m/s^2
			}
		}
		return accVec;
	}
	
	public static Vector3d getAccelerationComponentOfAllObjectsOn1(SpaceObjectFast obj1,
			ArrayList<SpaceObjectFast> spaceObjects) {
		Vector3d accVec = new Vector3d();
		for (SpaceObjectFast s : spaceObjects) {
			if(!s.getName().equals(obj1.getName())) {
			double distanceFrom1To2InKM = obj1.getToScalePosition().distance(s.getToScalePosition());
			double distanceFrom1To2InM = distanceFrom1To2InKM * 1000;
			double acc_of_1_from_2 = (gravitationalConstant * (s.getMass())) / (Math.pow(distanceFrom1To2InM, 2));
			Vector3d obj_1_to_2_vec = (s.getToScalePosition().subtract(obj1.getToScalePosition())).normalize();
			accVec = accVec.add(obj_1_to_2_vec.mult((double) acc_of_1_from_2)); // in m/s^2
			}
		}
		return accVec;
	}

	public static Vector3d getAccelerationComponentOf2On1(SpaceObjectFast obj1, SpaceObjectFast obj2) {
		double distanceFrom1To2InKM = obj1.getToScalePosition().distance(obj2.getToScalePosition());
		double distanceFrom1To2InM = distanceFrom1To2InKM * 1000;
		double acc_of_1_from_2 = (gravitationalConstant * (obj2.getMass())) / (Math.pow(distanceFrom1To2InM, 2));
		Vector3d obj_1_to_2_vec = (obj2.getToScalePosition().subtract(obj1.getToScalePosition())).normalize();
		return obj_1_to_2_vec.mult((double) acc_of_1_from_2); // in m/s^2
	}
}
