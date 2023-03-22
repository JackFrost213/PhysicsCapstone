package main;

import java.util.ArrayList;
import java.util.HashMap;

import net.wcomohundro.jme3.math.Vector3d;
import precreatedObjects.JamesWebbSpaceTelescope;
import precreatedObjects.SpaceObject;
import precreatedScenarios.Simulation;
import shapes3D.SpaceObjectFast;

public class ChaosGenerator {

	public static ArrayList<ChaosNumber> generateChaosNumbers(double xMin, double xMax, double yMin, double yMax,
			int numberOfSamples) {

		ArrayList<ChaosNumber> list = new ArrayList<ChaosNumber>();

		int remainder = 0;
		double ySamples = (Math.floor(Math.sqrt(numberOfSamples)));
		if ((Math.sqrt(numberOfSamples) % 1) != 0) {
			remainder = (int) Math
					.ceil((numberOfSamples - Math.pow(Math.floor(Math.sqrt(numberOfSamples)), 2)) / ySamples);
		}

		double xSamples = (Math.floor(Math.sqrt(numberOfSamples)) + remainder);
		double xLength = (xMax - xMin) / xSamples;
		double yLength = (yMax - yMin) / ySamples;

		for (double x = xMin + xLength / 2; x < xMax; x += xLength) {
			for (double y = yMin + yLength / 2; y < yMax; y += yLength) {
				ChaosNumber temp = new ChaosNumber(0, new Vector3d(x, y, 0));
				list.add(temp);
				if (list.size() >= numberOfSamples) {
					return list;
				}
			}
		}

		return list;
	}

	public static Vector3d getLyapunovValuesNoNormalization(ArrayList<SpaceObjectFast> spaceObjects,
			SpaceObjectFast probe, int numOfSamples, double deltaD, double timeStep, int numOfIterations) {
		ArrayList<ArrayList<SpaceObjectFast>> spaceObjectsTest = new ArrayList<ArrayList<SpaceObjectFast>>();
		ArrayList<SpaceObjectFast> spaceObjectsMain = new ArrayList<SpaceObjectFast>();
		for (SpaceObjectFast s : spaceObjects) {
			if (!s.getName().equals(probe.getName())) {
				SpaceObjectFast clone = s.clone();
				spaceObjectsMain.add(clone);
			} else {
				SpaceObjectFast probeShifted = probe.clone();
				ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
				temp.add(probe.getInitialConditions().get(0));
				temp.add(probe.getInitialConditions().get(1));
				probeShifted.setInitialConditions(temp);
				spaceObjectsMain.add(probeShifted);
			}
		}
		for (int x = 0; x < numOfSamples; x++) {
			ArrayList<SpaceObjectFast> newTestSetup = new ArrayList<SpaceObjectFast>();
			Vector3d deltaPlacement = new Vector3d(
					(double) (deltaD * Math.cos(2 * Math.PI * ((double) x / (numOfSamples)))), 0,
					(double) (deltaD * Math.sin(2 * Math.PI * ((double) x / (numOfSamples)))));
			for (SpaceObjectFast s : spaceObjects) {
				if (!s.getName().equals(probe.getName())) {
					newTestSetup.add(s.clone());
				} else {
					SpaceObjectFast probeShifted = probe.clone();
					ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
					temp.add(probe.getInitialConditions().get(0).add(deltaPlacement));
					temp.add(probe.getInitialConditions().get(1));
					probeShifted.setInitialConditions(temp);
					newTestSetup.add(probeShifted);
				}
			}
			spaceObjectsTest.add(newTestSetup);
		}

		int indexOfProbe = 0;
		for (SpaceObjectFast s : spaceObjectsTest.get(0)) {
			if (s.getName().equals(probe.getName())) {
				break;
			}
			indexOfProbe++;
		}

		SpaceObjectFast currentProbe = spaceObjectsMain.get(indexOfProbe);

		for (int n = 0; n < numOfIterations; n++) {
			DifferentialEquationSolvers.eulerCalculationFast(spaceObjectsMain, (double) timeStep);
			for (int x = 0; x < numOfSamples; x++) {
				DifferentialEquationSolvers.eulerCalculationFast(spaceObjectsTest.get(x), (double) timeStep);
			}
		}

		double maxDelta = Double.NEGATIVE_INFINITY;
		double minDelta = Double.POSITIVE_INFINITY;
		for (int x = 0; x < numOfSamples; x++) {
			SpaceObjectFast s = spaceObjectsTest.get(x).get(indexOfProbe);
			double currentDelta = s.getToScalePosition().subtract(currentProbe.getToScalePosition()).length();
			if (currentDelta > maxDelta) {
				maxDelta = currentDelta;
			}
			if (currentDelta < minDelta) {
				minDelta = currentDelta;
			}
		}

		return new Vector3d(Math.log(maxDelta / deltaD) / (timeStep * numOfIterations),
				Math.log(minDelta / deltaD) / (timeStep * numOfIterations), 0);
	}

	public static Vector3d getLyapunovValuesWithNormalization(ArrayList<SpaceObjectFast> spaceObjects,
			SpaceObjectFast probe, int numOfSamples, double deltaD, double timeStep, int numOfIterations) {
		ArrayList<ArrayList<SpaceObjectFast>> spaceObjectsTest = new ArrayList<ArrayList<SpaceObjectFast>>();
		ArrayList<SpaceObjectFast> spaceObjectsMain = new ArrayList<SpaceObjectFast>();
		for (SpaceObjectFast s : spaceObjects) {
			if (!s.getName().equals(probe.getName())) {
				SpaceObjectFast clone = s.clone();
				spaceObjectsMain.add(clone);
			} else {
				SpaceObjectFast probeShifted = probe.clone();
				ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
				temp.add(probe.getInitialConditions().get(0));
				temp.add(probe.getInitialConditions().get(1));
				probeShifted.setInitialConditions(temp);
				spaceObjectsMain.add(probeShifted);
			}
		}
		for (int x = 0; x < numOfSamples; x++) {
			ArrayList<SpaceObjectFast> newTestSetup = new ArrayList<SpaceObjectFast>();
			Vector3d deltaPlacement = new Vector3d(
					(double) (deltaD * Math.cos(2 * Math.PI * ((double) x / (numOfSamples)))), 0,
					(double) (deltaD * Math.sin(2 * Math.PI * ((double) x / (numOfSamples)))));
			for (SpaceObjectFast s : spaceObjects) {
				if (!s.getName().equals(probe.getName())) {
					newTestSetup.add(s.clone());
				} else {
					SpaceObjectFast probeShifted = probe.clone();
					ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
					temp.add(probe.getInitialConditions().get(0).add(deltaPlacement));
					temp.add(probe.getInitialConditions().get(1));
					probeShifted.setInitialConditions(temp);
					newTestSetup.add(probeShifted);
				}
			}
			spaceObjectsTest.add(newTestSetup);
		}

		int indexOfProbe = 0;
		for (SpaceObjectFast s : spaceObjectsTest.get(0)) {
			if (s.getName().equals(probe.getName())) {
				break;
			}
			indexOfProbe++;
		}

		SpaceObjectFast currentProbe = spaceObjectsMain.get(indexOfProbe);

		HashMap<Integer, Double> data = new HashMap<Integer, Double>();
		for (int n = 0; n < numOfIterations; n++) {
			DifferentialEquationSolvers.eulerCalculationFast(spaceObjectsMain, (double) timeStep);
			for (int x = 0; x < numOfSamples; x++) {
				DifferentialEquationSolvers.eulerCalculationFast(spaceObjectsTest.get(x), (double) timeStep);
				SpaceObjectFast s = spaceObjectsTest.get(x).get(indexOfProbe);
				double distance = s.getToScalePosition().distance(currentProbe.getToScalePosition());
				Vector3d direction = s.getToScalePosition().subtract(currentProbe.getToScalePosition()).normalize();
				s.setToScalePosition(currentProbe.getToScalePosition().add(direction.mult((double) deltaD)));
				if (data.get(x) == null) {
					data.put(x, Math.log(distance / deltaD));
				} else {
					data.put(x, data.get(x) + Math.log(distance / deltaD));
				}
			}
		}

		double maxDelta = Double.NEGATIVE_INFINITY;
		double minDelta = Double.POSITIVE_INFINITY;
		for (int x = 0; x < numOfSamples; x++) {
			SpaceObjectFast s = spaceObjectsTest.get(x).get(indexOfProbe);
			double currentDelta = data.get(x);
			if (currentDelta > maxDelta) {
				maxDelta = currentDelta;
			}
			if (currentDelta < minDelta) {
				minDelta = currentDelta;
			}
		}

		return new Vector3d(maxDelta / (timeStep * numOfIterations), minDelta / (timeStep * numOfIterations), 0);
	}

	public static Vector3d getStickyStability(ArrayList<SpaceObjectFast> spaceObjects, SpaceObjectFast probe,
			int numOfSamples, double deltaD, double timeStep, int numOfIterations) {
		ArrayList<SpaceObjectFast> spaceObjectsMain = new ArrayList<SpaceObjectFast>();
		for (SpaceObjectFast s : spaceObjects) {
			if (!s.getName().equals(probe.getName())) {
				SpaceObjectFast clone = s.clone();
				spaceObjectsMain.add(clone);
			} else {
				SpaceObjectFast probeShifted = probe.clone();
				ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
				temp.add(probe.getInitialConditions().get(0));
				temp.add(probe.getInitialConditions().get(1));
				probeShifted.setInitialConditions(temp);
				spaceObjectsMain.add(probeShifted);
			}
		}

		int indexOfProbe = 0;
		for (SpaceObjectFast s : spaceObjectsMain) {
			if (s.getName().equals(probe.getName())) {
				break;
			}
			indexOfProbe++;
		}
		HashMap<Integer, Double> initialRelativeDistances = new HashMap<Integer, Double>();
		double netDifferences = 0;
		int index = 0;
		for (SpaceObjectFast s : spaceObjectsMain) {
			if (!s.getName().equals(probe.getName())) {
				initialRelativeDistances.put(index,
						spaceObjectsMain.get(indexOfProbe).getToScalePosition().distance(s.getToScalePosition()));
			}
			index++;
		}

		for (int n = 0; n < numOfIterations; n++) {
			DifferentialEquationSolvers.eulerCalculationFast(spaceObjectsMain, (double) timeStep);
			index = 0;
			for (SpaceObjectFast s : spaceObjectsMain) {
				if (!s.getName().equals(probe.getName())) {
					double initialDistance = initialRelativeDistances.get(index);
					netDifferences += Math.abs(initialDistance
							- spaceObjectsMain.get(indexOfProbe).getToScalePosition().distance(s.getToScalePosition()))
							/ initialDistance;
				}
				index++;
			}
		}

		return new Vector3d(0, 0, Math.log(netDifferences) / (numOfIterations * timeStep));
	}

	public static Vector3d getChaosValues(ArrayList<SpaceObjectFast> spaceObjects, SpaceObjectFast probe,
			int numOfSamples, double deltaD, double timeStep, int numOfIterations) {
		ArrayList<ArrayList<SpaceObjectFast>> spaceObjectsTest = new ArrayList<ArrayList<SpaceObjectFast>>();
		ArrayList<SpaceObjectFast> spaceObjectsMain = new ArrayList<SpaceObjectFast>();
		for (SpaceObjectFast s : spaceObjects) {
			if (!s.getName().equals(probe.getName())) {
				SpaceObjectFast clone = s.clone();
				spaceObjectsMain.add(clone);
			} else {
				SpaceObjectFast probeShifted = probe.clone();
				ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
				temp.add(probe.getInitialConditions().get(0));
				temp.add(probe.getInitialConditions().get(1));
				probeShifted.setInitialConditions(temp);
				spaceObjectsMain.add(probeShifted);
			}
		}
		for (int x = 0; x < numOfSamples; x++) {
			ArrayList<SpaceObjectFast> newTestSetup = new ArrayList<SpaceObjectFast>();
			Vector3d deltaPlacement = new Vector3d(
					(double) (deltaD * Math.cos(2 * Math.PI * ((double) x / (numOfSamples)))), 0,
					(double) (deltaD * Math.sin(2 * Math.PI * ((double) x / (numOfSamples)))));
			for (SpaceObjectFast s : spaceObjects) {
				if (!s.getName().equals(probe.getName())) {
					newTestSetup.add(s.clone());
				} else {
					SpaceObjectFast probeShifted = probe.clone();
					ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
					temp.add(probe.getInitialConditions().get(0).add(deltaPlacement));
					temp.add(probe.getInitialConditions().get(1));
					probeShifted.setInitialConditions(temp);
					newTestSetup.add(probeShifted);
				}
			}
			spaceObjectsTest.add(newTestSetup);
		}

		int indexOfProbe = 0;
		for (SpaceObjectFast s : spaceObjectsTest.get(0)) {
			if (s.getName().equals(probe.getName())) {
				break;
			}
			indexOfProbe++;
		}

		HashMap<Integer, Double> initialRelativeDistances = new HashMap<Integer, Double>();
		double netDifferences = 0;
		int index = 0;
		for (SpaceObjectFast s : spaceObjectsMain) {
			if (!s.getName().equals(probe.getName())) {
				initialRelativeDistances.put(index,
						spaceObjectsMain.get(indexOfProbe).getToScalePosition().distance(s.getToScalePosition()));
			}
			index++;
		}

		SpaceObjectFast currentProbe = spaceObjectsMain.get(indexOfProbe);

		HashMap<Integer, Double> data = new HashMap<Integer, Double>();
		for (int n = 0; n < numOfIterations; n++) {
			DifferentialEquationSolvers.eulerCalculationFast(spaceObjectsMain, (double) timeStep);
			index = 0;
			for (SpaceObjectFast s : spaceObjectsMain) {
				if (!s.getName().equals(probe.getName())) {
					double initialDistance = initialRelativeDistances.get(index);
					netDifferences += Math.abs(initialDistance
							- spaceObjectsMain.get(indexOfProbe).getToScalePosition().distance(s.getToScalePosition()))
							/ initialDistance;
				}
				index++;
			}
			for (int x = 0; x < numOfSamples; x++) {
				DifferentialEquationSolvers.eulerCalculationFast(spaceObjectsTest.get(x), (double) timeStep);
				SpaceObjectFast s = spaceObjectsTest.get(x).get(indexOfProbe);
				double distance = s.getToScalePosition().distance(currentProbe.getToScalePosition());
				Vector3d direction = s.getToScalePosition().subtract(currentProbe.getToScalePosition()).normalize();
				s.setToScalePosition(currentProbe.getToScalePosition().add(direction.mult((double) deltaD)));
				if (data.get(x) == null) {
					data.put(x, Math.log(distance / deltaD));
				} else {
					data.put(x, data.get(x) + Math.log(distance / deltaD));
				}
			}
		}

		double maxDelta = Double.NEGATIVE_INFINITY;
		double minDelta = Double.POSITIVE_INFINITY;
		for (int x = 0; x < numOfSamples; x++) {
			// SpaceObjectFast s = spaceObjectsTest.get(x).get(indexOfProbe);
			double currentDelta = data.get(x);
			if (currentDelta > maxDelta) {
				maxDelta = currentDelta;
			}
			if (currentDelta < minDelta) {
				minDelta = currentDelta;
			}
		}

		return new Vector3d(maxDelta / (timeStep * numOfIterations), minDelta / (timeStep * numOfIterations),
				Math.log(netDifferences) / (numOfIterations * timeStep));
	}

	public static Vector3d getMostStableVelocityForPosition(Vector3d position, ArrayList<SpaceObject> spaceObjects,
			SpaceObject probe, double minXSpeed, double topXSpeed, double minYSpeed, double topYSpeed,
			double precision) {
		probe.setToScalePosition(position);
		ArrayList<SpaceObjectFast> spaceObjectsFast = new ArrayList<SpaceObjectFast>();
		SpaceObjectFast probeFast = null;
		for (SpaceObject s : spaceObjects) {
			SpaceObjectFast fastTemp = new SpaceObjectFast(s);
			spaceObjectsFast.add(fastTemp);
			if (s.getName().equals(probe.getName())) {
				probeFast = fastTemp;
			}
		}

		Vector3d bestVelocity = new Vector3d(0, 0, 0);
		double minValue = Double.POSITIVE_INFINITY;
		double currentPrecision = 10;
		while (currentPrecision > precision) {
			for (double xVelocity = minXSpeed; xVelocity <= topXSpeed; xVelocity += currentPrecision) {
				for (double yVelocity = minYSpeed; yVelocity <= topYSpeed; yVelocity += currentPrecision) {
					Vector3d tempVelocity = new Vector3d(xVelocity, 0, yVelocity);
					ArrayList<Vector3d> initialCond = new ArrayList<Vector3d>();
					initialCond.add(position);
					initialCond.add(tempVelocity);
					probeFast.setInitialConditions(initialCond);
					double tempValue = getStickyStability(spaceObjectsFast, probeFast, 20, 50, 500, 500).z;
					if (tempValue < minValue) {
						minValue = tempValue;
						bestVelocity = tempVelocity;
					}
				}
			}
			minXSpeed = bestVelocity.x - currentPrecision;
			topXSpeed = bestVelocity.x + currentPrecision;
			
			minYSpeed = bestVelocity.z - currentPrecision;
			topYSpeed = bestVelocity.z + currentPrecision;
			currentPrecision = currentPrecision/10;
		}
		return bestVelocity;
	}

	public static String getChaosSetupAsString(ArrayList<SpaceObject> spaceObjects, SpaceObject probe, int[] parameters,
			double[] ranges, Class<? extends Simulation> simulationClass) {
		String s = "SpaceObjects: \"";
		int numOfSamples = parameters[0];
		int deltaD = parameters[1];
		int timeStep = parameters[2];
		int numOfIterations = parameters[3];
		int dataSamples = parameters[4];
		double minX = ranges[0];
		double maxX = ranges[1];
		double minY = ranges[2];
		double maxY = ranges[3];
		for (SpaceObject space : spaceObjects) {
			s += space.getName() + ",";
		}
		s = s.substring(0, s.length() - 1);
		s += "\";\n";
		s += "Probe: \"" + probe.getName();
		s += "\";\n";
		s += "numOfSamples: \"" + numOfSamples;
		s += "\";\n";
		s += "deltaD: \"" + deltaD;
		s += "\";\n";
		s += "ts: \"" + timeStep;
		s += "\";\n";
		s += "Iterations: \"" + numOfIterations;
		s += "\";\n";
		s += "dataSamples: \"" + dataSamples;
		s += "\";\n";
		s += "Range: \"{" + minX + "," + maxX + "," + minY + "," + maxY;
		s += "}\";\n";
		s += "Probe Pos: \"" + probe.getToScalePosition();
		s += "\";\n";
		if (simulationClass != null) {
			s += "Simulation Class: \"" + simulationClass.getName();
		} else {
			s += "Simulation Class: \"NULL";
		}
		s += "\";\n";
		return s;
	}

}
