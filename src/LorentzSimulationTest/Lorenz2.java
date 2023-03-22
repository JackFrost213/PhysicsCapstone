package LorentzSimulationTest;

import java.util.ArrayList;

import net.wcomohundro.jme3.math.Vector3d;

public class Lorenz2 {

		static double sigma = 10;
    	static double R = 28;
    	static double b = -((double)8)/3;
	
		public static double dx(Vector3d pos) {
	        return sigma*(pos.y - pos.x);
	    }

	    public static double dy(Vector3d pos) {
	        return -pos.x*pos.z + R*pos.x - pos.y;
	    }

	    public static double dz(Vector3d pos) {
	        return pos.x*pos.y + b*pos.z;
	    }	
	    
	    public static double ddx(Vector3d pos, Vector3d dPos) {
	        return sigma*(dPos.y - dPos.x);
	    }

	    public static double ddy(Vector3d pos, Vector3d dPos) {
	        return -pos.x*dPos.z + (R-pos.z)*dPos.x - dPos.y;
	    }

	    public static double ddz(Vector3d pos, Vector3d dPos) {
	        return pos.x*dPos.y + dPos.x*pos.y+ b*dPos.z;
	    }	
	    
	    public static Vector3d eulerApprox(Vector3d pos, double dt) {
	    	Vector3d newPos = new Vector3d();
	    	newPos = pos.add(new Vector3d(dx(pos)*dt,dy(pos)*dt,dz(pos)*dt));
	    	return newPos;
	    }
	    
	    public static ArrayList<Vector3d> eulerApproxSim(Vector3d pos, Vector3d dPos, double dt) {
	    	Vector3d newDPos = new Vector3d();
	    	newDPos = dPos.add(new Vector3d(ddx(pos, dPos)*dt,ddy(pos, dPos)*dt,ddz(pos, dPos)*dt));
	    	ArrayList<Vector3d> finalData = new ArrayList<Vector3d>();
	    	finalData.add(pos.add(newDPos.mult(dt)));
	    	finalData.add(newDPos);
	    	return finalData;
	    }
	    
	    public static Vector3d eulerApproxDT(Vector3d pos,  Vector3d dPos, double dt) {
	    	Vector3d newPos = new Vector3d();
	    	newPos = dPos.add(new Vector3d(ddx(pos, dPos)*dt,ddy(pos, dPos)*dt,ddz(pos, dPos)*dt));
	    	return newPos;
	    }
	    
	    public static Vector3d rk4(Vector3d pos, double dt) {
	    	double k1x = dx(pos);
	    	double k1y = dy(pos);
	    	double k1z = dz(pos);
	    	Vector3d tempPos = pos.add(k1x * dt/2.0,k1y * dt/2.0,k1z * dt/2.0);
	    	double k2x = dx(tempPos);
	    	double k2y = dy(tempPos);
	    	double k2z = dz(tempPos);
	    	tempPos = pos.add(k2x * dt/2.0,k2y * dt/2.0,k2z * dt/2.0);
	    	double k3x = dx(tempPos);
	    	double k3y = dy(tempPos);
	    	double k3z = dz(tempPos);
	    	tempPos = pos.add(k3x * dt,k3y * dt,k3z * dt);
	    	double k4x = dx(tempPos);
	    	double k4y = dy(tempPos);
	    	double k4z = dz(tempPos);
	    	double x = (k1x+2*k2x+2*k3x+k4x) * dt/6.0;
	    	double y = (k1y+2*k2y+2*k3y+k4y) * dt/6.0;
	    	double z = (k1z+2*k2z+2*k3z+k4z) * dt/6.0;
	    	return pos.add(x,y,z);

	    }
	    
	    public static ArrayList<Vector3d> rk4Sim(Vector3d pos, Vector3d dPos, double dt) {
	    	//k1v
	    	Vector3d k1v = new Vector3d(ddx(pos, dPos), ddy(pos, dPos), ddz(pos, dPos));
	    	
	    	//k1x
	    	Vector3d k1x = dPos;
	    	
	    	Vector3d tempDPos = dPos.add(k1v.mult(dt/2));
	    	Vector3d tempPos = pos.add(k1x.mult(dt/2));
	    	
	    	//k2v
	    	Vector3d k2v = new Vector3d(ddx(tempPos, tempDPos), ddy(tempPos, tempDPos), ddz(tempPos, tempDPos));
	    	
	    	//k2x
	    	Vector3d k2x = tempDPos;
	    	
	    	tempDPos = dPos.add(k2v.mult(dt/2));
	    	tempPos = pos.add(k2x.mult(dt/2));
	    	
	    	//k3v
	    	Vector3d k3v = new Vector3d(ddx(tempPos, tempDPos), ddy(tempPos, tempDPos), ddz(tempPos, tempDPos));

	    	//k3x
	    	Vector3d k3x = tempDPos;
	    	
	    	tempDPos = dPos.add(k3v.mult(dt));
	    	tempPos = pos.add(k3x.mult(dt));
	    	
	    	//k4v
	    	Vector3d k4v = new Vector3d(ddx(tempPos, tempDPos), ddy(tempPos, tempDPos), ddz(tempPos, tempDPos));

	    	//k4x
	    	Vector3d k4x = tempDPos;
	    	
	    	Vector3d dv = k1v.add(k2v.mult(2)).add(k3v.mult(2)).add(k4v).mult(dt/6);
	    	Vector3d dx = k1x.add(k2x.mult(2)).add(k3x.mult(2)).add(k4x).mult(dt/6);
	    	
	    	ArrayList<Vector3d> finalData = new ArrayList<Vector3d>();
	    	finalData.add(pos.add(dx));
	    	finalData.add(dPos.add(dv));
	    	return finalData;	
	    }
	    
	    public static Vector3d rk4DT(Vector3d pos, Vector3d dPos, double dt) {
	    	double k1x = ddx(pos, dPos);
	    	double k1y = ddy(pos, dPos);
	    	double k1z = ddz(pos, dPos);
	    	Vector3d tempPos = dPos.add(k1x * dt/2.0,k1y * dt/2.0,k1z * dt/2.0);
	    	double k2x = ddx(pos, tempPos);
	    	double k2y = ddy(pos, tempPos);
	    	double k2z = ddz(pos, tempPos);
	    	tempPos = dPos.add(k2x * dt/2.0,k2y * dt/2.0,k2z * dt/2.0);
	    	double k3x = ddx(pos, tempPos);
	    	double k3y = ddy(pos, tempPos);
	    	double k3z = ddz(pos, tempPos);
	    	tempPos = dPos.add(k3x * dt,k3y * dt,k3z * dt);
	    	double k4x = ddx(pos, tempPos);
	    	double k4y = ddy(pos, tempPos);
	    	double k4z = ddz(pos, tempPos);
	    	double dx = (k1x+2*k2x+2*k3x+k4x) * dt/6;
	    	double dy = (k1y+2*k2y+2*k3y+k4y) * dt/6;
	    	double dz = (k1z+2*k2z+2*k3z+k4z) * dt/6;
	    	
	    	return dPos.add(dx,dy,dz);	
	    }
	    
	    public static void main2(String[] args) {
	    	Vector3d state = new Vector3d(5,5,5);
	    	Vector3d delta = new Vector3d(dx(state),dy(state),dz(state));
	    	
	    	Vector3d state2 = new Vector3d(5,5,5);
	    	Vector3d delta2 = new Vector3d(dx(state),dy(state),dz(state));
	    	
	    	Vector3d state3 = new Vector3d(5,5,5);
	    	Vector3d delta3 = new Vector3d(dx(state),dy(state),dz(state));
	    	
	    	Vector3d state4 = new Vector3d(5,5,5);
	    	Vector3d delta4 = new Vector3d(dx(state),dy(state),dz(state));
	    	double dt = 0.0001;
	    	int nIterations = 20000;
	    	
	    	for(int n = 0; n < nIterations; n++) {
	    		state = eulerApprox(state,dt);
	    		state2 = eulerApproxSim(state2,delta2,dt).get(0);
	    		delta2 = eulerApproxSim(state2,delta2,dt).get(1);
	    		System.out.println("Euler Approx: " + state);
	    		System.out.println("Euler 2nd Order Approx: " + state2);
	    		System.out.println("Next Test: ");
	    		state3 = rk4(state3,dt);
	    		state4 = rk4Sim(state4,delta4,dt).get(0);
	    		delta4 = rk4Sim(state4,delta4,dt).get(1);
	    		System.out.println("RK4 Approx: " + state3);
	    		System.out.println("RK4 2nd Order Approx: " + state4);
	    		System.out.println("\n\n");
	    	}
	    }
	    
	    public static void main(String[] args) {
	    	Vector3d exponents = rk4Try();
	    	Vector3d analyticalExponents = new Vector3d(0.9058, 0.0, -14.572);
	    	System.out.println("Numerical LCEs: " + exponents);
	    	System.out.println("Analytical LCEs: " + analyticalExponents);
	    	System.out.println("Perecent Error LCE1: " + 100*Math.abs((analyticalExponents.x - exponents.x)/analyticalExponents.x));
	    	System.out.println("Perecent Error LCE2: " + 100*Math.abs((analyticalExponents.y - exponents.y)/analyticalExponents.y));
	    	System.out.println("Perecent Error LCE3: " + 100*Math.abs((analyticalExponents.z - exponents.z)/analyticalExponents.z));
	    }
	    
	    public static void eulerApproxTry() {
	    	double dt = 0.001;
	    	int nTransients = 1000;
	    	int nIterates = 200000;
	    	double epsilon = 1;
	    	
	    	Vector3d state = new Vector3d(5,5,5);
	    	Vector3d velocity = new Vector3d(dx(state),dy(state),dz(state));
	    	Vector3d e1 = new Vector3d(epsilon*1,0,0);
	    	Vector3d e2 = new Vector3d(0,epsilon*1,0);
	    	Vector3d e3 = new Vector3d(0,0,epsilon*1);
	    	
	    	for(int n = 0; n < nTransients; n++) {
	    			state = eulerApprox(state,dt);
	    			e1 = eulerApproxDT(state,e1,dt);
	    			e2 = eulerApproxDT(state,e2,dt);
	    			e3 = eulerApproxDT(state,e3,dt);
	    			
	    			e1 = e1.normalize();
	    			double e1_dot_e2 = e1.dot(e2);
	    			e2 = e2.subtract(e1.mult(e1_dot_e2));
	    			
	    			e2 = e2.normalize();
	    			double e1_dot_e3 = e1.dot(e3);
	    			double e2_dot_e3 = e2.dot(e3);
	    			e3 = e3.subtract(e1.mult(e1_dot_e3).add(e2.mult(e2_dot_e3)));
	    			e3 = e3.normalize();
	    	}
	    	
	    	double LCE1 = 0.0;
	    	double LCE2 = 0.0;
	    	double LCE3 = 0.0;
	    	
	    	for(int n = 0; n < nIterates; n++) {
	    			state = eulerApprox(state,dt);
	    			e1 = eulerApproxDT(state,e1,dt);
	    			e2 = eulerApproxDT(state,e2,dt);
	    			e3 = eulerApproxDT(state,e3,dt);
	    			LCE1 += Math.log(e1.length());
	    			e1 = e1.normalize();
	    			double e1_dot_e2 = e1.dot(e2);
	    			e2 = e2.subtract(e1.mult(e1_dot_e2));
	    			LCE2 += Math.log(e2.length());
	    			e2 = e2.normalize();
	    			double e1_dot_e3 = e1.dot(e3);
	    			double e2_dot_e3 = e2.dot(e3);
	    			e3 = e3.subtract(e1.mult(e1_dot_e3).add(e2.mult(e2_dot_e3)));
	    			LCE3 += Math.log(e3.length());
	    			e3 = e3.normalize();	    	
	    	}
	    	
	    	double IntegrationTime = dt * nIterates;
	    	LCE1 = LCE1 / IntegrationTime;
	    	LCE2 = LCE2 / IntegrationTime;
	    	LCE3 = LCE3 / IntegrationTime;
	    	
	    	System.out.println("LCE1: " + LCE1);
	    	System.out.println("LCE2: " + LCE2);
	    	System.out.println("LCE3: " + LCE3);
    }
	    
	    public static Vector3d rk4Try() {
	    	double dt = 0.04;
	    	int nTransients = 10000;
	    	int nIterates = 50000;
	    	double epsilon = 1;
	    	
	    	Vector3d state = new Vector3d(5,5,5);
	    	Vector3d velocity = new Vector3d(dx(state),dy(state),dz(state));
	    	Vector3d e1 = new Vector3d(epsilon*1,0,0);
	    	Vector3d e2 = new Vector3d(0,epsilon*1,0);
	    	Vector3d e3 = new Vector3d(0,0,epsilon*1);
	    	
	    	for(int n = 0; n < nTransients; n++) {
	    			state = rk4(state,dt);
	    			e1 = rk4DT(state,e1,dt);
	    			e2 = rk4DT(state,e2,dt);
	    			e3 = rk4DT(state,e3,dt);
	    			
	    			e1 = e1.normalize();
	    			double e1_dot_e2 = e1.dot(e2);
	    			e2 = e2.subtract(e1.mult(e1_dot_e2));
	    			
	    			e2 = e2.normalize();
	    			double e1_dot_e3 = e1.dot(e3);
	    			double e2_dot_e3 = e2.dot(e3);
	    			e3 = e3.subtract(e1.mult(e1_dot_e3).add(e2.mult(e2_dot_e3)));
	    			e3 = e3.normalize();
	    	}
	    	
	    	double LCE1 = 0.0;
	    	double LCE2 = 0.0;
	    	double LCE3 = 0.0;
	    	
	    	for(int n = 0; n < nIterates; n++) {
	    			state = rk4(state,dt);
	    			e1 = rk4DT(state,e1,dt);
	    			e2 = rk4DT(state,e2,dt);
	    			e3 = rk4DT(state,e3,dt);
	    			LCE1 += Math.log(e1.length());
	    			e1 = e1.normalize();
	    			double e1_dot_e2 = e1.dot(e2);
	    			e2 = e2.subtract(e1.mult(e1_dot_e2));
	    			LCE2 += Math.log(e2.length());
	    			e2 = e2.normalize();
	    			double e1_dot_e3 = e1.dot(e3);
	    			double e2_dot_e3 = e2.dot(e3);
	    			e3 = e3.subtract(e1.mult(e1_dot_e3).add(e2.mult(e2_dot_e3)));
	    			LCE3 += Math.log(e3.length());
	    			e3 = e3.normalize();	    	
	    	}
	    	
	    	double IntegrationTime = dt * nIterates;
	    	LCE1 = LCE1 / IntegrationTime;
	    	LCE2 = LCE2 / IntegrationTime;
	    	LCE3 = LCE3 / IntegrationTime;
	    	
	    	//System.out.println("LCE1: " + LCE1);
	    	//System.out.println("LCE2: " + LCE2);
	    	//System.out.println("LCE3: " + LCE3);
	    	
	    	return new Vector3d(LCE1, LCE2,LCE3);
    }
	    
	    public static void rk42ndOrderTry() {
		    	double dt = 0.01;
		    	int nTransients = 1000;
		    	int nIterates = 20000;
		    	double epsilon = 1;
		    	
		    	Vector3d state = new Vector3d(5,5,5);
		    	Vector3d velocity = new Vector3d(dx(state),dy(state),dz(state));
		    	Vector3d e1 = new Vector3d(epsilon*1,0,0);
		    	Vector3d e2 = new Vector3d(0,epsilon*1,0);
		    	Vector3d e3 = new Vector3d(0,0,epsilon*1);
		    	
		    	for(int n = 0; n < nTransients; n++) {
		    			ArrayList<Vector3d> finalData = rk4Sim(state,velocity,dt);
		    			state = finalData.get(0);
		    			velocity = finalData.get(1);
		    			e1 = rk4DT(state,e1,dt);
		    			e2 = rk4DT(state,e2,dt);
		    			e3 = rk4DT(state,e3,dt);
		    			
		    			e1 = e1.normalize();
		    			double e1_dot_e2 = e1.dot(e2);
		    			e2 = e2.subtract(e1.mult(e1_dot_e2));
		    			
		    			e2 = e2.normalize();
		    			double e1_dot_e3 = e1.dot(e3);
		    			double e2_dot_e3 = e2.dot(e3);
		    			e3 = e3.subtract(e1.mult(e1_dot_e3).add(e2.mult(e2_dot_e3)));
		    			e3 = e3.normalize();
		    	}
		    	
		    	double LCE1 = 0.0;
		    	double LCE2 = 0.0;
		    	double LCE3 = 0.0;
		    	
		    	for(int n = 0; n < nIterates; n++) {
		    			ArrayList<Vector3d> finalData = rk4Sim(state,velocity,dt);
		    			state = finalData.get(0);
		    			velocity = finalData.get(1);
		    			e1 = rk4DT(state,e1,dt);
		    			e2 = rk4DT(state,e2,dt);
		    			e3 = rk4DT(state,e3,dt);
		    			LCE1 += Math.log(e1.length());
		    			e1 = e1.normalize();
		    			double e1_dot_e2 = e1.dot(e2);
		    			e2 = e2.subtract(e1.mult(e1_dot_e2));
		    			LCE2 += Math.log(e2.length());
		    			e2 = e2.normalize();
		    			double e1_dot_e3 = e1.dot(e3);
		    			double e2_dot_e3 = e2.dot(e3);
		    			e3 = e3.subtract(e1.mult(e1_dot_e3).add(e2.mult(e2_dot_e3)));
		    			LCE3 += Math.log(e3.length());
		    			e3 = e3.normalize();	    	
		    	}
		    	
		    	double IntegrationTime = dt * nIterates;
		    	LCE1 = LCE1 / IntegrationTime;
		    	LCE2 = LCE2 / IntegrationTime;
		    	LCE3 = LCE3 / IntegrationTime;
		    	
		    	System.out.println("LCE1: " + LCE1);
		    	System.out.println("LCE2: " + LCE2);
		    	System.out.println("LCE3: " + LCE3);
	    }
	    
	    public static void euler2ndOrderApproxTry() {
	    	double dt = 0.00001;
	    	int nTransients = 1000000;
	    	int nIterates = 20000000;
	    	double epsilon = 1;
	    	//time taken is same
	    	Vector3d state = new Vector3d(5,5,5);
	    	Vector3d velocity = new Vector3d(dx(state),dy(state),dz(state));
	    	Vector3d e1 = new Vector3d(epsilon*1,0,0);
	    	Vector3d e2 = new Vector3d(0,epsilon*1,0);
	    	Vector3d e3 = new Vector3d(0,0,epsilon*1);
	    	
	    	for(int n = 0; n < nTransients; n++) {
	    			ArrayList<Vector3d> finalData = eulerApproxSim(state,velocity,dt);
	    			state = finalData.get(0);
	    			velocity = finalData.get(1);
	    			e1 = eulerApproxDT(state,e1,dt);
	    			e2 = eulerApproxDT(state,e2,dt);
	    			e3 = eulerApproxDT(state,e3,dt);
	    			
	    			e1 = e1.normalize();
	    			double e1_dot_e2 = e1.dot(e2);
	    			e2 = e2.subtract(e1.mult(e1_dot_e2));
	    			
	    			e2 = e2.normalize();
	    			double e1_dot_e3 = e1.dot(e3);
	    			double e2_dot_e3 = e2.dot(e3);
	    			e3 = e3.subtract(e1.mult(e1_dot_e3).add(e2.mult(e2_dot_e3)));
	    			e3 = e3.normalize();
	    	}
	    	
	    	double LCE1 = 0.0;
	    	double LCE2 = 0.0;
	    	double LCE3 = 0.0;
	    	
	    	for(int n = 0; n < nIterates; n++) {
	    			ArrayList<Vector3d> finalData = eulerApproxSim(state,velocity,dt);
	    			state = finalData.get(0);
	    			velocity = finalData.get(1);
	    			e1 = eulerApproxDT(state,e1,dt);
	    			e2 = eulerApproxDT(state,e2,dt);
	    			e3 = eulerApproxDT(state,e3,dt);
	    			LCE1 += Math.log(e1.length());
	    			e1 = e1.normalize();
	    			double e1_dot_e2 = e1.dot(e2);
	    			e2 = e2.subtract(e1.mult(e1_dot_e2));
	    			LCE2 += Math.log(e2.length());
	    			e2 = e2.normalize();
	    			double e1_dot_e3 = e1.dot(e3);
	    			double e2_dot_e3 = e2.dot(e3);
	    			e3 = e3.subtract(e1.mult(e1_dot_e3).add(e2.mult(e2_dot_e3)));
	    			LCE3 += Math.log(e3.length());
	    			e3 = e3.normalize();	    	
	    	}
	    	
	    	double IntegrationTime = dt * nIterates;
	    	LCE1 = LCE1 / IntegrationTime;
	    	LCE2 = LCE2 / IntegrationTime;
	    	LCE3 = LCE3 / IntegrationTime;
	    	
	    	System.out.println("LCE1: " + LCE1);
	    	System.out.println("LCE2: " + LCE2);
	    	System.out.println("LCE3: " + LCE3);
	    }
	
}
