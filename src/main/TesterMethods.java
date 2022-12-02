package main;

import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.jme3.math.Vector3f;

import shapes3D.SpaceObject;

public class TesterMethods {
	
	private static double gravitationalConstant = 6.67430E-11;
	
	private static JTextArea analyticalField;
	
	
	public static void setAnalyticalField(JTextArea as) {
		analyticalField = as;
	}
	
	
	static float updateSpeed = 0.4f;
	static float tpfTot = 0;
	public static void compareToAnalyticalSolution(ArrayList<SpaceObject> spaceObjects, double totalTimeElapsed, float tpf) {
		if (spaceObjects.size() != 2)
			throw new RuntimeException("Cannot analytically solve more than 2 body system");
		
		SpaceObject obj1 = spaceObjects.get(0);
		SpaceObject obj2 = spaceObjects.get(1);
		Vector3f initialObj1Pos = obj1.getInitialConditions().get(0); //in km
		Vector3f initialObj1Vel = obj1.getInitialConditions().get(1);
		Vector3f initialObj2Pos = obj2.getInitialConditions().get(0);
		Vector3f initialObj2Vel = obj2.getInitialConditions().get(1);
		Vector3f obj2FromObj1Initial = initialObj2Pos.subtract(initialObj1Pos);
		double reducedMass = 1/((1/obj1.getMass()) + (1/obj2.getMass()));
		Vector3f initialCenterOfMassPosition = initialObj1Pos.mult((float) obj1.getMass()).add(initialObj2Pos.mult((float)obj2.getMass()));
		initialCenterOfMassPosition = initialCenterOfMassPosition.divide((float)(obj1.getMass()+obj2.getMass()));
		Vector3f initialCenterOfMassVelocity = initialObj1Vel.mult((float)obj1.getMass());
		initialCenterOfMassVelocity = initialCenterOfMassVelocity.add(initialObj2Vel.mult((float)obj2.getMass()));
		initialCenterOfMassVelocity = initialCenterOfMassVelocity.divide((float)(obj1.getMass()+obj2.getMass()));
		Vector3f axisOfRotation = obj2FromObj1Initial.normalize();
		Vector3f numericalObj2FromObj1 = obj2.getToScalePosition().subtract(obj1.getToScalePosition());
		Vector3f currentNormalizedObj2FromObj1 = numericalObj2FromObj1.normalize();
		double theta = currentNormalizedObj2FromObj1.angleBetween(axisOfRotation);
		//double theta = Math.acos((currentNormalizedObj2FromObj1.dot(axisOfRotation)));

		Vector3f rHatInitial = axisOfRotation;
		double initialDistance = obj2FromObj1Initial.length();
		Vector3f r1Initial = initialObj1Pos.subtract(initialCenterOfMassPosition);
		Vector3f r2Initial = initialObj2Pos.subtract(initialCenterOfMassPosition);
		Vector3f r1DotInitial = initialObj1Vel.subtract(initialCenterOfMassVelocity);
		Vector3f r2DotInitial = initialObj2Vel.subtract(initialCenterOfMassVelocity);
		Vector3f Lc = r1Initial.cross(r1DotInitial.mult((float) obj1.getMass())).add(r2Initial.cross(r2DotInitial).mult((float)obj2.getMass()));
		Vector3f LcNormalized = Lc.divide((float)Lc.length());
		Vector3f thetaHatInitial = LcNormalized.cross(rHatInitial);
		Vector3f rHatAtPIOver2 = rHatInitial.mult((float)Math.cos(Math.PI/2)).add(thetaHatInitial.mult((float)Math.sin(Math.PI/2)));
		Vector3f planeNormal = axisOfRotation.cross(rHatAtPIOver2).normalize();
		Vector3f currentNormal = currentNormalizedObj2FromObj1.cross(axisOfRotation).normalize();
		double sign = planeNormal.dot(currentNormal);
		theta = sign < 0 ? theta : -theta;
		Vector3f rHatAtTheta = rHatInitial.mult((float)Math.cos(theta)).add(thetaHatInitial.mult((float)Math.sin(theta)));
		
		Vector3f rDotInitial = initialObj2Vel.subtract(initialObj1Vel);
		double vRInitial = rDotInitial.dot(rHatInitial);
		
		/*System.out.println("Lc: " + Lc);
		System.out.println("LcNorm: " + LcNormalized);
		System.out.println("rHatInitial: " + rHatInitial);
		System.out.println("Initial Obj1 Vel: " + initialObj1Vel);
		System.out.println("Initial COM Vel: " + initialCenterOfMassVelocity);
		System.out.println("r1 initial: " + r1Initial);
		System.out.println("r1Dot initial: " + r1DotInitial);
		System.out.println("r1 cross r1dot: " + r1Initial.cross(r1DotInitial.mult((float) obj1.getMass())));
		System.out.println("rHatInitial: " + rHatInitial);
		*/
		//System.out.println("thetaHatInitial: " + thetaHatInitial);
		//System.out.println("rDotInitial: " + rDotInitial);
		double vThetaInitial = rDotInitial.dot(thetaHatInitial);
		
		//System.out.println("Reduced Mass: " + reducedMass);
		//System.out.println("Initial Distance: " + initialDistance);
		//System.out.println("VThetaInitial: " + vThetaInitial);
		//double K = (gravitationalConstant*reducedMass)/Math.pow(vThetaInitial*initialDistance*1000, 2);
		double K = 1/(Math.pow(Lc.length()*Math.pow(10, 6), 2)/(gravitationalConstant*obj1.getMass()*obj2.getMass()*reducedMass));
		//System.out.println("K: " + K);
		double A = 1/(initialDistance*1000) - K;
		//System.out.println("A: " + A);
		double B = -vRInitial/(vThetaInitial*initialDistance*1000);
		double C = Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
		double analyticalDistance = (1/K)/(1-(C/K)*Math.cos(theta));
		Vector3f positionOfObj2 = initialCenterOfMassPosition.add(initialCenterOfMassVelocity.mult((float) totalTimeElapsed));
		positionOfObj2 = positionOfObj2.add(rHatAtTheta.mult((float)(analyticalDistance/1000)).mult((float)(obj1.getMass()/(obj1.getMass()+obj2.getMass()))));
		
		Vector3f positionOfObj1 = initialCenterOfMassPosition.add(initialCenterOfMassVelocity.mult((float) totalTimeElapsed));
		positionOfObj1 = positionOfObj1.subtract(rHatAtTheta.mult((float)(analyticalDistance/1000)).mult((float)(obj2.getMass()/(obj1.getMass()+obj2.getMass()))));
		
		//System.out.println("Angular Momentum: " + Lc.length()*Math.pow(10,6));
		//System.out.println("C Value: " + (1/K));
		//System.out.println("Little C Value: " + littleC);
		//System.out.println("Eccentricity: " + (C/K));
		//double distance_of_1_to_2 = c/(1+ecc_temp*Math.cos(theta));
		//System.out.println("Analytical Distance: " + analyticalDistance);
		double numericalDistance = obj2.getToScalePosition().subtract(obj1.getToScalePosition()).length()*1000;
		Vector3f currentCenterOfMassPos = obj1.getToScalePosition().mult((float) obj1.getMass()).add(obj2.getToScalePosition().mult((float)obj2.getMass()));
		currentCenterOfMassPos = currentCenterOfMassPos.divide((float)(obj1.getMass()+obj2.getMass()));	
		//System.out.println("Theoretical Distance:"+ numericalDistance);
		/*System.out.println("Numerical Position of Obj1: " + obj1.getToScalePosition());
		System.out.println("Analytical Position of Obj1: " + positionOfObj1);
		System.out.println("Numerical Position of Obj2: " + obj2.getToScalePosition());
		System.out.println("Analytical Position of Obj2: " + positionOfObj2);
		System.out.println("Percent Error For Object 1: " + (Math.abs(analyticalDistance-numericalDistance)/analyticalDistance)*100 + "%");
		*/
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
		//System.out.println("Center of Mass Position: " + initialCenterOfMassPosition.add(initialCenterOfMassVelocity.mult((float) totalTimeElapsed)));

	}
	
	public static void compareToAnalyticalSolution2(ArrayList<SpaceObject> spaceObjects, double totalTimeElapsed) {
		if (spaceObjects.size() != 2)
			throw new RuntimeException("Cannot analytically solve more than 2 body system");
		
		SpaceObject obj1 = spaceObjects.get(0);
		SpaceObject obj2 = spaceObjects.get(1);
		Vector3f position1 = obj1.getInitialConditions().get(0); //in km
		Vector3f position2 = obj2.getInitialConditions().get(0);
		Vector3f centerOfMassPosition = position1.mult((float) obj1.getMass()).add(position2.mult((float)obj2.getMass()));
		centerOfMassPosition = centerOfMassPosition.divide((float)(obj1.getMass()+obj2.getMass()));
		centerOfMassPosition = centerOfMassPosition.mult(1000);
		Vector3f centerOfMassVelocity = obj1.getInitialConditions().get(1).mult((float) obj1.getMass());
		centerOfMassVelocity = centerOfMassVelocity.add(obj2.getInitialConditions().get(1).mult((float) obj2.getMass()));
		centerOfMassVelocity = centerOfMassVelocity.divide((float)(obj1.getMass()+obj2.getMass()));
		centerOfMassVelocity = centerOfMassVelocity.mult(1000);
		
		Vector3f u1_initial = obj1.getInitialConditions().get(1).mult(1000).subtract(centerOfMassVelocity);
		Vector3f u2_initial = obj2.getInitialConditions().get(1).mult(1000).subtract(centerOfMassVelocity);
		Vector3f new_x_axis = (position2
				.subtract(position1)).normalize();
		Vector3f current_axis = (obj2.getToScalePosition()
				.subtract(obj1.getToScalePosition())).normalize();
		float distance = obj2.getInitialConditions().get(0).subtract(obj1.getInitialConditions().get(0)).length();
		distance = distance*1000;
		double alpha = u1_initial.angleBetween(new_x_axis);
		double u_not = u1_initial.length();
		Vector3f r_not_vec = centerOfMassPosition.subtract(position1.mult(1000));
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
		Vector3f positionOfObj1 = centerOfMassVelocity.mult((float) totalTimeElapsed);
		double obj1Scalar = obj2.getMass()/(obj1.getMass()+obj2.getMass());
		Vector3f r_vector = new Vector3f((float)(distance_of_1_to_2*Math.cos(theta)),0f,(float)(distance_of_1_to_2*Math.sin(theta))).mult((float) obj1Scalar);
		
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
		
		Vector3f centerOfMassPositionCur = obj1.getToScalePosition().mult((float) obj1.getMass()).add(obj2.getToScalePosition().mult((float)obj2.getMass()));
		centerOfMassPositionCur = centerOfMassPosition.divide((float)(obj1.getMass()+obj2.getMass()));
		centerOfMassPositionCur = centerOfMassPosition;
		System.out.println("Analytical Distance:"+distance_of_1_to_2);
		System.out.println("Theoertical Distance From m1 to com: " + obj1.getToScalePosition().subtract(centerOfMassPositionCur).length());
		System.out.println("Theoretical Distance:"+ numericalDistance);
		//System.out.println("Analytical 2 Distance: " + distance_Try_2);
		System.out.println("Center of Mass Position: " + centerOfMassPosition.add(centerOfMassVelocity.mult((float) totalTimeElapsed)));
		System.out.println("Percent Error: " + (Math.abs(distance_of_1_to_2-numericalDistance)/distance_of_1_to_2)*100 + "%");
		
		
		

	}
}
