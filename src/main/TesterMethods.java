package main;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.wcomohundro.jme3.math.Vector3d;
import precreatedObjects.SpaceObject;

public class TesterMethods {
	
	private static double gravitationalConstant = 6.67430E-11;
	
	private static JTextArea analyticalField;
	
	
	public static void setAnalyticalField(JTextArea as) {
		analyticalField = as;
	}
	
	
	static double updateSpeed = 0.4f;
	static double tpfTot = 0;
	
	private static HashMap<String,Double> realTesting = new HashMap<String,Double>();
	private static boolean firstTrigger = false, secondTrigger = false;
	public static void compareToEarthSunMoonSolution(ArrayList<SpaceObject> spaceObjects, double totalTimeElapsed, double tpf) {
		SpaceObject earth = spaceObjects.get(0);
		SpaceObject moon = spaceObjects.get(1);
		SpaceObject sun = spaceObjects.get(2);
		
		if(totalTimeElapsed/(86400*365.25/2) >= 1 && !firstTrigger) {
			realTesting.put(earth.getName() + "0", 100*Math.abs(152093249.865 - earth.getToScalePosition().subtract(sun.getToScalePosition()).length())/152093249.865);
			realTesting.put(sun.getName() + "0", (double) (100*Math.abs(sun.getToScalePosition().length() - 147120163)/147120163));
			realTesting.put(moon.getName() + "0", (double) (100*Math.abs(moon.getToScalePosition().subtract(earth.getToScalePosition()).length() - 392973.3161339043)/392973.3161339043));
			firstTrigger = true;
			//System.out.println(moon.getToScalePosition().subtract(earth.getToScalePosition()).length());
		}
		
		if(totalTimeElapsed/(86400*365.25) >= 1 && !secondTrigger) {
			realTesting.put(earth.getName() + "1", (double) (100*Math.abs(147120163 - earth.getToScalePosition().subtract(sun.getToScalePosition()).length())/147120163));
			realTesting.put(sun.getName() + "1", (double) (100*Math.abs(sun.getToScalePosition().length() - 147120163)/147120163));
			realTesting.put(moon.getName() + "1", (double) (100*Math.abs(moon.getToScalePosition().subtract(earth.getToScalePosition()).length() - 392719.5291859591)/392719.5291859591));
			secondTrigger = true;
			//System.out.println(moon.getToScalePosition().subtract(earth.getToScalePosition()).length());
		}
		
		tpfTot += tpf;
		if(tpfTot > updateSpeed && analyticalField != null && totalTimeElapsed/(86400*365.25) < 1) {
			String text = "";
			text += "Results for Real Data Tester Method: \n";
			text += "Awaiting One Orbital Period for Data...\n";
			text += "Simulation Time Elapsed: " + totalTimeElapsed/86400 + " days\n";
			analyticalField.setText(text);
			tpfTot = 0;
		}
		if(tpfTot > updateSpeed && analyticalField != null && totalTimeElapsed/(86400*365.25) >= 1) {
			String text = "";
			//text += "Two Body Analytical Solution for: " + obj1.getName() + " and " + obj2.getName() + " \n\n";
			double earthError = realTesting.get(earth.getName() + "0") + realTesting.get(earth.getName() + "1");
			double sunError = realTesting.get(sun.getName() + "0") + realTesting.get(sun.getName() + "1");
			double moonError = realTesting.get(moon.getName() + "0") + realTesting.get(moon.getName() + "1");
			text += "Results for Real Data Tester Method: \n";
			text += "Percent Error For " + earth.getName() + ": " + earthError + "%\n";
			text += "Percent Error For " + moon.getName() + ": " + moonError/10.0 + "%\n";
			text += "Percent Error For " + sun.getName() + ": " + sunError + "%\n";
			text += "Simulation Time Elapsed: " + totalTimeElapsed/3600 + " hours\n";
			analyticalField.setText(text);
			tpfTot = 0;
		}
	}

	public static void compareToAnalyticalSolution(ArrayList<SpaceObject> spaceObjects, double totalTimeElapsed, double tpf) {
		if (spaceObjects.size() != 2)
			throw new RuntimeException("Cannot analytically solve more than 2 body system");
		
		SpaceObject obj1 = spaceObjects.get(0);
		SpaceObject obj2 = spaceObjects.get(1);
		Vector3d initialObj1Pos = obj1.getInitialConditions().get(0); //in km
		Vector3d initialObj1Vel = obj1.getInitialConditions().get(1);
		Vector3d initialObj2Pos = obj2.getInitialConditions().get(0);
		Vector3d initialObj2Vel = obj2.getInitialConditions().get(1);
		Vector3d obj2FromObj1Initial = initialObj2Pos.subtract(initialObj1Pos);
		double reducedMass = 1/((1/obj1.getMass()) + (1/obj2.getMass()));
		Vector3d initialCenterOfMassPosition = initialObj1Pos.mult((double) obj1.getMass()).add(initialObj2Pos.mult((double)obj2.getMass()));
		initialCenterOfMassPosition = initialCenterOfMassPosition.divide((double)(obj1.getMass()+obj2.getMass()));
		Vector3d initialCenterOfMassVelocity = initialObj1Vel.mult((double)obj1.getMass());
		initialCenterOfMassVelocity = initialCenterOfMassVelocity.add(initialObj2Vel.mult((double)obj2.getMass()));
		initialCenterOfMassVelocity = initialCenterOfMassVelocity.divide((double)(obj1.getMass()+obj2.getMass()));
		Vector3d axisOfRotation = obj2FromObj1Initial.normalize();
		Vector3d numericalObj2FromObj1 = obj2.getToScalePosition().subtract(obj1.getToScalePosition());
		Vector3d currentNormalizedObj2FromObj1 = numericalObj2FromObj1.normalize();
		double theta = currentNormalizedObj2FromObj1.angleBetween(axisOfRotation);
		//double theta = Math.acos((currentNormalizedObj2FromObj1.dot(axisOfRotation)));

		Vector3d rHatInitial = axisOfRotation;
		double initialDistance = obj2FromObj1Initial.length();
		Vector3d r1Initial = initialObj1Pos.subtract(initialCenterOfMassPosition);
		Vector3d r2Initial = initialObj2Pos.subtract(initialCenterOfMassPosition);
		Vector3d r1DotInitial = initialObj1Vel.subtract(initialCenterOfMassVelocity);
		Vector3d r2DotInitial = initialObj2Vel.subtract(initialCenterOfMassVelocity);
		Vector3d Lc = r1Initial.cross(r1DotInitial.mult((double) obj1.getMass())).add(r2Initial.cross(r2DotInitial).mult((double)obj2.getMass()));
		Vector3d LcNormalized = Lc.divide((double)Lc.length());
		Vector3d thetaHatInitial = LcNormalized.cross(rHatInitial);
		Vector3d rHatAtPIOver2 = rHatInitial.mult((double)Math.cos(Math.PI/2)).add(thetaHatInitial.mult((double)Math.sin(Math.PI/2)));
		Vector3d planeNormal = axisOfRotation.cross(rHatAtPIOver2).normalize();
		Vector3d currentNormal = currentNormalizedObj2FromObj1.cross(axisOfRotation).normalize();
		double sign = planeNormal.dot(currentNormal);
		theta = sign < 0 ? theta : -theta;
		Vector3d rHatAtTheta = rHatInitial.mult((double)Math.cos(theta)).add(thetaHatInitial.mult((double)Math.sin(theta)));
		
		Vector3d rDotInitial = initialObj2Vel.subtract(initialObj1Vel);
		double vRInitial = rDotInitial.dot(rHatInitial);
		double vThetaInitial = rDotInitial.dot(thetaHatInitial);
		double K = 1/(Math.pow(Lc.length()*Math.pow(10, 6), 2)/(gravitationalConstant*obj1.getMass()*obj2.getMass()*reducedMass));
		//System.out.println("K: " + K);
		double A = 1/(initialDistance*1000) - K;
		//System.out.println("A: " + A);
		double B = -vRInitial/(vThetaInitial*initialDistance*1000);
		double C = Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
		double analyticalDistance = (1/K)/(1-(C/K)*Math.cos(theta));
		Vector3d positionOfObj2 = initialCenterOfMassPosition.add(initialCenterOfMassVelocity.mult((double) totalTimeElapsed));
		positionOfObj2 = positionOfObj2.add(rHatAtTheta.mult((double)(analyticalDistance/1000)).mult((double)(obj1.getMass()/(obj1.getMass()+obj2.getMass()))));
		
		Vector3d positionOfObj1 = initialCenterOfMassPosition.add(initialCenterOfMassVelocity.mult((double) totalTimeElapsed));
		positionOfObj1 = positionOfObj1.subtract(rHatAtTheta.mult((double)(analyticalDistance/1000)).mult((double)(obj2.getMass()/(obj1.getMass()+obj2.getMass()))));
		double numericalDistance = obj2.getToScalePosition().subtract(obj1.getToScalePosition()).length()*1000;
		Vector3d currentCenterOfMassPos = obj1.getToScalePosition().mult((double) obj1.getMass()).add(obj2.getToScalePosition().mult((double)obj2.getMass()));
		currentCenterOfMassPos = currentCenterOfMassPos.divide((double)(obj1.getMass()+obj2.getMass()));	
		tpfTot += tpf;
		if(tpfTot > updateSpeed && analyticalField != null) {
			String text = "";
			//text += "Two Body Analytical Solution for: " + obj1.getName() + " and " + obj2.getName() + " \n\n";
			text += "Angular Momentum of C.O.M.: " + Lc.length()*Math.pow(10, 6) + "\n";
			text += "Position of C.O.M.: " + currentCenterOfMassPos + "\n";
			text += "Analytical Eccentricity of " + obj1.getName() + ": " + (C/K) + "\n";
			text += "Numerical Position of " + obj1.getName() + ": " + obj1.getToScalePosition() + "\n";
			text += "Analytical Position of " + obj1.getName() + ": " + positionOfObj1 + "\n";
			text += "Numerical Position of " + obj2.getName() + ": " + obj2.getToScalePosition() + "\n";
			text += "Analytical Position of " + obj2.getName() + ": " + positionOfObj2 + "\n";
			text += "Percent Error For " + obj1.getName() + ": " + (Math.abs(analyticalDistance-numericalDistance)/analyticalDistance)*100 + "%\n";
			text += "Simulation Time Elapsed: " + totalTimeElapsed/3600 + " hours\n"; 
			analyticalField.setText(text);
			tpfTot = 0;
		}
		//System.out.println("Center of Mass Position: " + initialCenterOfMassPosition.add(initialCenterOfMassVelocity.mult((double) totalTimeElapsed)));

	}
	
	public static void compareToAnalyticalSolution2(ArrayList<SpaceObject> spaceObjects, double totalTimeElapsed) {
		if (spaceObjects.size() != 2)
			throw new RuntimeException("Cannot analytically solve more than 2 body system");
		
		SpaceObject obj1 = spaceObjects.get(0);
		SpaceObject obj2 = spaceObjects.get(1);
		Vector3d position1 = obj1.getInitialConditions().get(0); //in km
		Vector3d position2 = obj2.getInitialConditions().get(0);
		Vector3d centerOfMassPosition = position1.mult((double) obj1.getMass()).add(position2.mult((double)obj2.getMass()));
		centerOfMassPosition = centerOfMassPosition.divide((double)(obj1.getMass()+obj2.getMass()));
		centerOfMassPosition = centerOfMassPosition.mult(1000);
		Vector3d centerOfMassVelocity = obj1.getInitialConditions().get(1).mult((double) obj1.getMass());
		centerOfMassVelocity = centerOfMassVelocity.add(obj2.getInitialConditions().get(1).mult((double) obj2.getMass()));
		centerOfMassVelocity = centerOfMassVelocity.divide((double)(obj1.getMass()+obj2.getMass()));
		centerOfMassVelocity = centerOfMassVelocity.mult(1000);
		
		Vector3d u1_initial = obj1.getInitialConditions().get(1).mult(1000).subtract(centerOfMassVelocity);
		Vector3d u2_initial = obj2.getInitialConditions().get(1).mult(1000).subtract(centerOfMassVelocity);
		Vector3d new_x_axis = (position2
				.subtract(position1)).normalize();
		Vector3d current_axis = (obj2.getToScalePosition()
				.subtract(obj1.getToScalePosition())).normalize();
		double distance = obj2.getInitialConditions().get(0).subtract(obj1.getInitialConditions().get(0)).length();
		distance = distance*1000;
		double alpha = u1_initial.angleBetween(new_x_axis);
		double u_not = u1_initial.length();
		Vector3d r_not_vec = centerOfMassPosition.subtract(position1.mult(1000));
		double r_not = r_not_vec.length();
		double L = r_not*u_not*Math.sin(alpha);
		double K = gravitationalConstant*Math.pow(obj1.getMass(), 2)*obj2.getMass();
		K = K/Math.pow(obj1.getMass()+obj2.getMass(), 2);
		double reduced_mass = (obj1.getMass()*obj2.getMass())/(obj1.getMass()+obj2.getMass());
		double angular_momentum = r_not*u_not*Math.sin(alpha);
		angular_momentum = angular_momentum*reduced_mass;
		//double standard_gravitational_parameters = gravitationalConstant*(obj1.getMass()+obj2.getMass());	
		double c = Math.pow(angular_momentum, 2)/(gravitationalConstant*obj1.getMass()*obj2.getMass()*reduced_mass);
		double ecc_temp = c/distance - 1;
		//System.out.println(c/distance);
		//System.out.println("Initial Earth Velocity: " + obj1.getInitialConditions().get(1));
		//System.out.println("u_not: " + u_not);
		//System.out.println("K: " + K);
		//System.out.println("L: " + L);
		System.out.println("C: " + c);
		System.out.println("Distance: " + distance);
		double theta = current_axis.angleBetween(new_x_axis);
		double distance_of_1_to_2 = c/(1+ecc_temp*Math.cos(theta));
		double distance_Try_2 = Math.pow(L, 2)*(obj1.getMass()+obj2.getMass());
		distance_Try_2 = distance_Try_2/(K*obj2.getMass()*(1+0*Math.cos(theta)));
		Vector3d positionOfObj1 = centerOfMassVelocity.mult((double) totalTimeElapsed);
		double obj1Scalar = obj2.getMass()/(obj1.getMass()+obj2.getMass());
		Vector3d r_vector = new Vector3d((double)(distance_of_1_to_2*Math.cos(theta)),0f,(double)(distance_of_1_to_2*Math.sin(theta))).mult((double) obj1Scalar);
		
		positionOfObj1 = positionOfObj1.add(obj2.getInitialConditions().get(0).mult(1000).subtract(r_vector));
		//positionOfObj1 = positionOfObj1.add(r_vector);
		double numericalDistance = obj2.getToScalePosition().subtract(obj1.getToScalePosition()).length()*1000;
		
		System.out.println("Angular Momentum: " + angular_momentum);
		System.out.println("Eccentricity: " + ecc_temp);
		//System.out.println("Reduced Mass: " + reduced_mass);
		/*
		System.out.println("Alpha: " + alpha);
		System.out.println("r_not (distance from m1 to com): " + r_not);
		System.out.println("u1 initial: " + u1_initial.length());
		System.out.println("Eccentricity: " + ecc_temp);
		
		System.out.println("Initial Distance: " + distance);
		System.out.println("Numerical Position of Obj1 (" + obj1.getName() + "): " + obj1.getToScalePosition());
		System.out.println("Analytical Position of Obj1 (" + obj1.getName() + "): " + positionOfObj1.divide(1000));
		System.out.println("OBJ 2: " + obj2.getName());
		System.out.println("Time Passed: " + totalTimeElapsed/(3600*24) + " Days");
		System.out.println("Theta: " + theta);
		*/
		
		Vector3d centerOfMassPositionCur = obj1.getToScalePosition().mult((double) obj1.getMass()).add(obj2.getToScalePosition().mult((double)obj2.getMass()));
		centerOfMassPositionCur = centerOfMassPosition.divide((double)(obj1.getMass()+obj2.getMass()));
		centerOfMassPositionCur = centerOfMassPosition;
		System.out.println("Analytical Distance:"+distance_of_1_to_2);
		System.out.println("Theoertical Distance From m1 to com: " + obj1.getToScalePosition().subtract(centerOfMassPositionCur).length());
		System.out.println("Theoretical Distance:"+ numericalDistance);
		//System.out.println("Analytical 2 Distance: " + distance_Try_2);
		System.out.println("Center of Mass Position: " + centerOfMassPosition.add(centerOfMassVelocity.mult((double) totalTimeElapsed)));
		System.out.println("Percent Error: " + (Math.abs(distance_of_1_to_2-numericalDistance)/distance_of_1_to_2)*100 + "%");
		
		
		

	}
}
