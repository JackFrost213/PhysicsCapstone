package main;

import java.util.ArrayList;

import net.wcomohundro.jme3.math.Vector3d;
import precreatedObjects.SpaceObject;
import shapes3D.SpaceObjectFast;

public class DifferentialEquationSolvers {

	// distance from earth to sun 149,192,626.18 km
	public static void eulerApproximation(ArrayList<SpaceObject> spaceObjects, double timeElapsed, double timeStep) {
		double timeCalculated = 0;
		while (timeCalculated + timeStep < timeElapsed) {
			eulerCalculation(spaceObjects, timeStep);
			timeCalculated += timeStep;

		}
		eulerCalculation(spaceObjects, timeElapsed - timeCalculated); // does the last calculation to current time

	}

	public static void rungeKuttaApproximation(ArrayList<SpaceObject> spaceObjects, double timeElapsed,
			double timeStep) {
		double timeCalculated = 0;
		ArrayList<SpaceObjectFast> spaceObjectsFast = new ArrayList<SpaceObjectFast>();
		for (int x = 0; x < spaceObjects.size(); x++) {
			spaceObjectsFast.add(new SpaceObjectFast(spaceObjects.get(x)));
		}
		while (timeCalculated + timeStep < timeElapsed) {
			RK4_2(spaceObjectsFast, timeStep);
			timeCalculated += timeStep;

		}
		RK4_2(spaceObjectsFast, timeElapsed - timeCalculated); // does the last calculation to current time

		for (int x = 0; x < spaceObjects.size(); x++) {
			spaceObjects.get(x).setToScalePosition(spaceObjectsFast.get(x).getToScalePosition());
			spaceObjects.get(x).setVelocity(spaceObjectsFast.get(x).getVelocity());
		}

	}

	static void RK4_HardCode(ArrayList<SpaceObjectFast> spaceObjects, double timeStep) {
		SpaceObjectFast earth = spaceObjects.get(0);
		SpaceObjectFast moon = spaceObjects.get(1);
		SpaceObjectFast sun = spaceObjects.get(2);

		Vector3d k1v_earth = GravitationalCalculator.getAccelerationComponentOfAllObjectsOn1(earth, spaceObjects)
				.divide(1000);
		Vector3d k1v_moon = GravitationalCalculator.getAccelerationComponentOfAllObjectsOn1(moon, spaceObjects)
				.divide(1000);
		Vector3d k1v_sun = GravitationalCalculator.getAccelerationComponentOfAllObjectsOn1(sun, spaceObjects)
				.divide(1000);

		Vector3d k1x_earth = earth.getVelocity();
		Vector3d k1x_moon = moon.getVelocity();
		Vector3d k1x_sun = sun.getVelocity();

		ArrayList<SpaceObjectFast> spaceObjectsTemp = new ArrayList<SpaceObjectFast>();
		SpaceObjectFast earth_temp = earth.clone();
		earth_temp.setToScalePosition(earth_temp.getToScalePosition().add(k1x_earth.mult(timeStep / 2)));
		spaceObjectsTemp.add(earth_temp);
		SpaceObjectFast moon_temp = moon.clone();
		moon_temp.setToScalePosition(moon_temp.getToScalePosition().add(k1x_moon.mult(timeStep / 2)));
		spaceObjectsTemp.add(moon_temp);
		SpaceObjectFast sun_temp = sun.clone();
		sun_temp.setToScalePosition(sun_temp.getToScalePosition().add(k1x_sun.mult(timeStep / 2)));
		spaceObjectsTemp.add(sun_temp);

		Vector3d k2v_earth = GravitationalCalculator
				.getAccelerationComponentOfAllObjectsOn1(earth_temp, spaceObjectsTemp).divide(1000);
		Vector3d k2v_moon = GravitationalCalculator.getAccelerationComponentOfAllObjectsOn1(moon_temp, spaceObjectsTemp)
				.divide(1000);
		Vector3d k2v_sun = GravitationalCalculator.getAccelerationComponentOfAllObjectsOn1(sun_temp, spaceObjectsTemp)
				.divide(1000);

		Vector3d k2x_earth = earth.getVelocity().add(k1v_earth.mult(timeStep / 2));
		Vector3d k2x_moon = moon.getVelocity().add(k1v_moon.mult(timeStep / 2));
		Vector3d k2x_sun = sun.getVelocity().add(k1v_sun.mult(timeStep / 2));

		spaceObjectsTemp = new ArrayList<SpaceObjectFast>();
		earth_temp = earth.clone();
		earth_temp.setToScalePosition(earth_temp.getToScalePosition().add(k1x_earth.mult(timeStep / 2)));
		spaceObjectsTemp.add(earth_temp);
		moon_temp = moon.clone();
		moon_temp.setToScalePosition(moon_temp.getToScalePosition().add(k1x_moon.mult(timeStep / 2)));
		spaceObjectsTemp.add(moon_temp);
		sun_temp = sun.clone();
		sun_temp.setToScalePosition(sun_temp.getToScalePosition().add(k1x_sun.mult(timeStep / 2)));
		spaceObjectsTemp.add(sun_temp);

		Vector3d k3v_earth = GravitationalCalculator
				.getAccelerationComponentOfAllObjectsOn1(earth_temp, spaceObjectsTemp).divide(1000);
		Vector3d k3v_moon = GravitationalCalculator.getAccelerationComponentOfAllObjectsOn1(moon_temp, spaceObjectsTemp)
				.divide(1000);
		Vector3d k3v_sun = GravitationalCalculator.getAccelerationComponentOfAllObjectsOn1(sun_temp, spaceObjectsTemp)
				.divide(1000);

		Vector3d k3x_earth = earth.getVelocity().add(k2v_earth.mult(timeStep / 2));
		Vector3d k3x_moon = moon.getVelocity().add(k2v_moon.mult(timeStep / 2));
		Vector3d k3x_sun = sun.getVelocity().add(k2v_sun.mult(timeStep / 2));

		spaceObjectsTemp = new ArrayList<SpaceObjectFast>();
		earth_temp = earth.clone();
		earth_temp.setToScalePosition(earth_temp.getToScalePosition().add(k1x_earth.mult(timeStep / 2)));
		spaceObjectsTemp.add(earth_temp);
		moon_temp = moon.clone();
		moon_temp.setToScalePosition(moon_temp.getToScalePosition().add(k1x_moon.mult(timeStep / 2)));
		spaceObjectsTemp.add(moon_temp);
		sun_temp = sun.clone();
		sun_temp.setToScalePosition(sun_temp.getToScalePosition().add(k1x_sun.mult(timeStep / 2)));
		spaceObjectsTemp.add(sun_temp);

		Vector3d k4v_earth = GravitationalCalculator
				.getAccelerationComponentOfAllObjectsOn1(earth_temp, spaceObjectsTemp).divide(1000);
		Vector3d k4v_moon = GravitationalCalculator.getAccelerationComponentOfAllObjectsOn1(moon_temp, spaceObjectsTemp)
				.divide(1000);
		Vector3d k4v_sun = GravitationalCalculator.getAccelerationComponentOfAllObjectsOn1(sun_temp, spaceObjectsTemp)
				.divide(1000);

		Vector3d k4x_earth = earth.getVelocity().add(k3v_earth.mult(timeStep));
		Vector3d k4x_moon = moon.getVelocity().add(k3v_moon.mult(timeStep));
		Vector3d k4x_sun = sun.getVelocity().add(k3v_sun.mult(timeStep));

		Vector3d dx_earth = (k1x_earth.add(k2x_earth.mult(2)).add(k3x_earth.mult(2)).add(k4x_earth)).mult(timeStep / 6);
		Vector3d dv_earth = (k1v_earth.add(k2v_earth.mult(2)).add(k3v_earth.mult(2)).add(k4v_earth)).mult(timeStep / 6);
		earth.setToScalePosition(earth.getToScalePosition().add(dx_earth));
		earth.setVelocity(earth.getVelocity().add(dv_earth));
		Vector3d dx_moon = (k1x_moon.add(k2x_moon.mult(2)).add(k3x_moon.mult(2)).add(k4x_moon)).mult(timeStep / 6);
		Vector3d dv_moon = (k1v_moon.add(k2v_moon.mult(2)).add(k3v_moon.mult(2)).add(k4v_moon)).mult(timeStep / 6);
		moon.setToScalePosition(moon.getToScalePosition().add(dx_moon));
		moon.setVelocity(moon.getVelocity().add(dv_moon));
		Vector3d dx_sun = (k1x_sun.add(k2x_sun.mult(2)).add(k3x_sun.mult(2)).add(k4x_sun)).mult(timeStep / 6);
		Vector3d dv_sun = (k1v_sun.add(k2v_sun.mult(2)).add(k3v_sun.mult(2)).add(k4v_sun)).mult(timeStep / 6);
		sun.setToScalePosition(sun.getToScalePosition().add(dx_sun));
		sun.setVelocity(sun.getVelocity().add(dv_sun));

	}

	static void RK4_2(ArrayList<SpaceObjectFast> spaceObjects, double timeStep) {
		ArrayList<Vector3d> updatedData = new ArrayList<Vector3d>();
		int indexOffset = spaceObjects.size();

		// k1v 0
		// k1x 1
		// k2v 2
		// k2x 3
		// k3v 4
		// k3x 5
		// k4v 6
		// k4x 7

		for (int x = 0; x < spaceObjects.size(); x++) {
			Vector3d k1v = GravitationalCalculator
					.getAccelerationComponentOfAllObjectsOn1(spaceObjects.get(x), spaceObjects).divide(1000);
			updatedData.add(k1v);
		}
		for (int x = 0; x < spaceObjects.size(); x++) {
			Vector3d k1x = spaceObjects.get(x).getVelocity();
			updatedData.add(k1x);
		}

		ArrayList<SpaceObjectFast> spaceObjectsTemp = new ArrayList<SpaceObjectFast>();
		for (int x = 0; x < spaceObjects.size(); x++) {
			SpaceObjectFast temp = spaceObjects.get(x).clone();
			Vector3d k1x = updatedData.get(x + indexOffset);
			temp.setToScalePosition(temp.getToScalePosition().add(k1x.mult(timeStep / 2)));
			spaceObjectsTemp.add(temp);
		}

		for (int x = 0; x < spaceObjects.size(); x++) {
			SpaceObjectFast temp = spaceObjectsTemp.get(x);
			Vector3d k2v = GravitationalCalculator.getAccelerationComponentOfAllObjectsOn1(temp, spaceObjectsTemp)
					.divide(1000);
			updatedData.add(k2v);
		}

		for (int x = 0; x < spaceObjects.size(); x++) {
			Vector3d k1v = updatedData.get(x);
			Vector3d k2x = spaceObjects.get(x).getVelocity().add(k1v.mult(timeStep / 2));
			updatedData.add(k2x);
		}

		spaceObjectsTemp = new ArrayList<SpaceObjectFast>();
		for (int x = 0; x < spaceObjects.size(); x++) {
			SpaceObjectFast temp = spaceObjects.get(x).clone();
			Vector3d k2x = updatedData.get(x + 3 * indexOffset);
			temp.setToScalePosition(temp.getToScalePosition().add(k2x.mult(timeStep / 2)));
			spaceObjectsTemp.add(temp);
		}

		for (int x = 0; x < spaceObjects.size(); x++) {
			SpaceObjectFast temp = spaceObjectsTemp.get(x);
			Vector3d k3v = GravitationalCalculator.getAccelerationComponentOfAllObjectsOn1(temp, spaceObjectsTemp)
					.divide(1000);
			updatedData.add(k3v);

		}

		for (int x = 0; x < spaceObjects.size(); x++) {
			Vector3d k2v = updatedData.get(x + 2 * indexOffset);
			Vector3d k3x = spaceObjects.get(x).getVelocity().add(k2v.mult(timeStep / 2));
			updatedData.add(k3x);
		}

		spaceObjectsTemp = new ArrayList<SpaceObjectFast>();
		for (int x = 0; x < spaceObjects.size(); x++) {
			SpaceObjectFast temp = spaceObjects.get(x).clone();
			Vector3d k3x = updatedData.get(x + 5 * indexOffset);
			temp.setToScalePosition(temp.getToScalePosition().add(k3x.mult(timeStep)));
			spaceObjectsTemp.add(temp);
		}

		for (int x = 0; x < spaceObjects.size(); x++) {
			SpaceObjectFast temp = spaceObjectsTemp.get(x);
			Vector3d k4v = GravitationalCalculator.getAccelerationComponentOfAllObjectsOn1(temp, spaceObjectsTemp)
					.divide(1000);
			updatedData.add(k4v);
		}

		for (int x = 0; x < spaceObjects.size(); x++) {
			Vector3d k3v = updatedData.get(x + 4 * indexOffset);
			Vector3d k4x = spaceObjects.get(x).getVelocity().add(k3v.mult(timeStep));
			updatedData.add(k4x);
		}

		for (int x = 0; x < spaceObjects.size(); x++) {
			Vector3d k1v = updatedData.get(x);
			Vector3d k1x = updatedData.get(x + indexOffset);
			Vector3d k2v = updatedData.get(x + 2 * indexOffset);
			Vector3d k2x = updatedData.get(x + 3 * indexOffset);
			Vector3d k3v = updatedData.get(x + 4 * indexOffset);
			Vector3d k3x = updatedData.get(x + 5 * indexOffset);
			Vector3d k4v = updatedData.get(x + 6 * indexOffset);
			Vector3d k4x = updatedData.get(x + 7 * indexOffset);
			Vector3d newObj1PositionChange = (k1x.add(k2x.mult(2)).add(k3x.mult(2)).add(k4x)).mult(timeStep / 6);
			Vector3d newObj1Position = spaceObjects.get(x).getToScalePosition().add(newObj1PositionChange);
			Vector3d newObj1VelocityChange = (k1v.add(k2v.mult(2)).add(k3v.mult(2)).add(k4v)).mult(timeStep / 6);
			Vector3d newObj1Velocity = spaceObjects.get(x).getVelocity().add(newObj1VelocityChange);
			spaceObjects.get(x).setToScalePosition(newObj1Position);
			spaceObjects.get(x).setVelocity(newObj1Velocity);
		}
	}

	public static void eulerCalculation(ArrayList<SpaceObject> spaceObjects, double timeStep) {
		ArrayList<Vector3d> accelerationVectors = new ArrayList<Vector3d>();
		for (SpaceObject spaceObject : spaceObjects) {
			ArrayList<Vector3d> accelerationComponents = new ArrayList<Vector3d>();
			for (SpaceObject spaceObject2 : spaceObjects) {
				if (spaceObject != spaceObject2) {
					accelerationComponents
							.add(GravitationalCalculator.getAccelerationComponentOf2On1(spaceObject, spaceObject2)); // in
																														// m/s^2
				}
			}
			Vector3d accelerationVector = new Vector3d(0, 0, 0);
			for (Vector3d acc : accelerationComponents) {
				accelerationVector = accelerationVector.add(acc);
			}
			accelerationVectors.add(accelerationVector);
		}
		for (int x = 0; x < spaceObjects.size(); x++) {
			Vector3d accelerationVector = accelerationVectors.get(x);
			SpaceObject spaceObject = spaceObjects.get(x);
			Vector3d accelerationVectorPre = accelerationVector;
			accelerationVector = accelerationVector.mult(timeStep / 1000);
			Vector3d new_obj_1_velocity = spaceObject.getVelocity().add(accelerationVector); // m/s
			spaceObject.setVelocity(new_obj_1_velocity); // velocity in km/s
			spaceObject
					.setToScalePosition(spaceObject.getToScalePosition().add(spaceObject.getVelocity().mult(timeStep)));

		}
	}

	public static void eulerCalculationFast(ArrayList<SpaceObjectFast> spaceObjects, double timeStep) {
		ArrayList<Vector3d> accelerationVectors = new ArrayList<Vector3d>();
		for (SpaceObjectFast spaceObject : spaceObjects) {
			ArrayList<Vector3d> accelerationComponents = new ArrayList<Vector3d>();
			for (SpaceObjectFast spaceObject2 : spaceObjects) {
				if (spaceObject != spaceObject2) {
					accelerationComponents
							.add(GravitationalCalculator.getAccelerationComponentOf2On1(spaceObject, spaceObject2)); // in
																														// m/s^2
				}
			}
			Vector3d accelerationVector = new Vector3d(0, 0, 0);
			for (Vector3d acc : accelerationComponents) {
				accelerationVector = accelerationVector.add(acc);
			}
			accelerationVectors.add(accelerationVector);
		}
		for (int x = 0; x < spaceObjects.size(); x++) {
			Vector3d accelerationVector = accelerationVectors.get(x);
			SpaceObjectFast spaceObject = spaceObjects.get(x);
			Vector3d accelerationVectorPre = accelerationVector;
			accelerationVector = accelerationVector.mult(timeStep / 1000);
			Vector3d new_obj_1_velocity = spaceObject.getVelocity().add(accelerationVector); // m/s
			spaceObject.setVelocity(new_obj_1_velocity); // velocity in km/s
			spaceObject
					.setToScalePosition(spaceObject.getToScalePosition().add(spaceObject.getVelocity().mult(timeStep)));

		}
	}

	public static Vector3d rk4DT(ArrayList<SpaceObjectFast> spaceObjects, SpaceObjectFast probe, Vector3d e1, double deltaD,
			double timeStep) {

			ArrayList<SpaceObjectFast> tempObjects = new ArrayList<SpaceObjectFast>();
			int indexOfProbe = -1;
			int x = 0;
			for(SpaceObjectFast s : spaceObjects) {
				if(s.getName().equals(probe.getName())) {
					indexOfProbe = x;
					SpaceObjectFast temp = probe.clone();
					temp.setToScalePosition(temp.getToScalePosition().add(e1.mult(deltaD)));
					tempObjects.add(temp);
				}
				else {
					tempObjects.add(s.clone());
				}
				x++;
			}
			
			RK4_2(tempObjects,timeStep);
			//temp(t+1) - temp(t) = e1 * deltaD
			Vector3d newVec = tempObjects.get(indexOfProbe).getToScalePosition().subtract(probe.getToScalePosition());
			return newVec.divide(deltaD);
		}

}
