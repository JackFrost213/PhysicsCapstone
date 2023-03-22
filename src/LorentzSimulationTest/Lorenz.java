package LorentzSimulationTest;


	/******************************************************************************
	 *  Compilation:  javac Lorenz.java
	 *  Execution:    java Lorenz
	 *  Dependencies: StdDraw.java
	 *
	 *  Plot phase space (x vs. z) of Lorenz attractor with one set of
	 *  initial conditions and another set of slightly perturbed intial
	 *  conditions. Uses Euler method.
	 *
	 ******************************************************************************/

	import java.awt.Color;
import java.util.ArrayList;

import net.wcomohundro.jme3.math.Vector3d;
import precreatedObjects.Earth;
import precreatedObjects.Moon;
import precreatedObjects.SpaceObject;
import precreatedObjects.Sun;
import twodplots.PositionGraph;

	public class Lorenz {
	    private double x, y, z;
	    private double p,r,b;
	    private Color color;

	    public Lorenz(double x, double y, double z, Color color) {
	        this.x = x;
	        this.y = y;
	        this.z = z;
	        this.p = 10;
	        this.r = 28;
	        this.b = 8/3;
	        this.color = color;
	    }

	    public void update(double dt) {
	       /* double xnew = x + dx(x, y, z) * dt;
	        double ynew = y + dy(x, y, z) * dt;
	        double znew = z + dz(x, y, z) * dt;
	        x = xnew;
	        y = ynew;
	        z = znew;
	        */
	    	
	    	Vector3d temp = rk4(new Vector3d(x,y,z), dt);
	    	x = temp.x;
	    	y = temp.y;
	    	z = temp.z;
	    	
	    }

	    public Vector3d rk4(Vector3d pos, double dt) {
	    	double k1x = dx(pos.x,pos.y,pos.z);
	    	double k1y = dy(pos.x,pos.y,pos.z);
	    	double k1z = dz(pos.x,pos.y,pos.z);
	    	Vector3d tempPos = pos.add(k1x * dt/2.0,k1y * dt/2.0,k1z * dt/2.0);
	    	double k2x = dx(tempPos.x,tempPos.y,tempPos.z);
	    	double k2y = dy(tempPos.x,tempPos.y,tempPos.z);
	    	double k2z = dz(tempPos.x,tempPos.y,tempPos.z);
	    	tempPos = pos.add(k2x * dt/2.0,k2y * dt/2.0,k2z * dt/2.0);
	    	double k3x = dx(tempPos.x,tempPos.y,tempPos.z);
	    	double k3y = dy(tempPos.x,tempPos.y,tempPos.z);
	    	double k3z = dz(tempPos.x,tempPos.y,tempPos.z);
	    	tempPos = pos.add(k3x * dt,k3y * dt,k3z * dt);
	    	double k4x = dx(tempPos.x,tempPos.y,tempPos.z);
	    	double k4y = dy(tempPos.x,tempPos.y,tempPos.z);
	    	double k4z = dz(tempPos.x,tempPos.y,tempPos.z);
	    	double x = (k1x+2*k2x+2*k3x+k4x) * dt/6.0;
	    	double y = (k1y+2*k2y+2*k3y+k4y) * dt/6.0;
	    	double z = (k1z+2*k2z+2*k3z+k4z) * dt/6.0;
	    	return pos.add(x,y,z);

	    }
	    
	    public void draw() {
	    }

	    public Vector3d getPosition() {
	    	return new Vector3d((double)x,(double)y,(double)z);
	    }
	    
	    public void setPosition(Vector3d pos) {
	    	x = pos.x;
	    	y = pos.y;
	    	z = pos.z;
	    }

	    public double dx(double x, double y, double z) {
	        return p*(y - x);
	    }

	    public double dy(double x, double y, double z) {
	        return -x*z + r*x - y;
	    }

	    public double dz(double x, double y, double z) {
	        return x*y - b*z;
	    }


	    public static void main(String[] args) {
	        Lorenz lorenz1 = new Lorenz(0.01, 0, 0, Color.BLUE);
	        double epsilon = 0.001;
	        Lorenz lorenz2 = new Lorenz(0.01+epsilon, 0, 0, Color.RED);
	        
	        Lorenz lorenzUp = new Lorenz(0.01+epsilon, 0, 0, Color.RED);
	        Lorenz lorenzRight = new Lorenz(0.01, 0, 0+epsilon, Color.RED);

	        ArrayList<SpaceObject> spaceObjects = new ArrayList<SpaceObject>();
	    	
	    	SpaceObject earth = new Earth();
			ArrayList<Vector3d> temp = new ArrayList<Vector3d>();
			temp.add(new Vector3d(0,0,0));
			temp.add(new Vector3d(0, 0, 0));
			earth.setInitialConditions(temp);
			spaceObjects.add(earth);
			
			SpaceObject earthUp = new Earth();
			earthUp.setName("Earth2");
			temp = new ArrayList<Vector3d>();
			temp.add(new Vector3d(0,0,0));
			temp.add(new Vector3d(0, 0, 0));
			earthUp.setInitialConditions(temp);
			spaceObjects.add(earthUp);
			
			SpaceObject earthRight = new Earth();
			earthRight.setName("Earth3");
			temp = new ArrayList<Vector3d>();
			temp.add(new Vector3d(0,0,0));
			temp.add(new Vector3d(0, 0, 0));
			earthRight.setInitialConditions(temp);
			//spaceObjects.add(earthRight);
			
			SpaceObject sun = new Sun();
			sun.setName("Sun");
			temp = new ArrayList<Vector3d>();
			temp.add(new Vector3d(0,0,0));
			temp.add(new Vector3d(0, 0, 0));
			sun.setInitialConditions(temp);
			//spaceObjects.add(sun);
			
			SpaceObject moon = new Moon();
			temp = new ArrayList<Vector3d>();
			temp.add(new Vector3d(0,0,0));
			temp.add(new Vector3d(0, 0, 0));
			moon.setInitialConditions(temp);
			//spaceObjects.add(moon);
	        
			earth.setToScalePosition(lorenz1.getPosition());
            sun.setToScalePosition(lorenz2.getPosition());
            
            double initialSepDist = earth.getToScalePosition().distance(sun.getToScalePosition());
			
	        PositionGraph posGraph = new PositionGraph(spaceObjects, false);
            posGraph.setVisible(true);
            
            ArrayList<SpaceObject> spaceObjects2 = new ArrayList<SpaceObject>();
            spaceObjects2.add(moon);
            PositionGraph posGraph2 = new PositionGraph(spaceObjects2, false);
            posGraph2.setVisible(true);
	        // Use Euler's method to numerically solve ODE
	        double dt = 0.01;
	        
	        
	        ArrayList<Double> distances = new ArrayList<Double>();
	        ArrayList<Double> distanceLogs = new ArrayList<Double>();
	        double lfSum = 0;
	        double largestLyapunov = 0;
	        double smallestLyapunov = 0;
	        earthUp.setToScalePosition(lorenzUp.getPosition());
            earthRight.setToScalePosition(lorenzRight.getPosition());
            earth.setToScalePosition(lorenz1.getPosition());
            sun.setToScalePosition(lorenz2.getPosition());
            Vector3d y1 = earthUp.getToScalePosition().subtract(earth.getToScalePosition());
            Vector3d y2 = earthRight.getToScalePosition().subtract(earth.getToScalePosition());
	        double initialArea = y1.cross(y2).length();
	        System.out.println(initialArea);
	        for (int i = 0; i < 1000; i++) {
	            lorenz1.update(dt);
	            lorenz2.update(dt);
	            lorenzUp.update(dt);
	            lorenzRight.update(dt);
	        }
	        for (int i = 0; i < 50000; i++) {
	            lorenz1.update(dt);
	            lorenz2.update(dt);
	            lorenzUp.update(dt);
	            lorenzRight.update(dt);
	            
	            earth.setToScalePosition(lorenz1.getPosition());
	            sun.setToScalePosition(lorenz2.getPosition());
	            earthUp.setToScalePosition(lorenzUp.getPosition());
	            earthRight.setToScalePosition(lorenzRight.getPosition());
	            double distance = earth.getToScalePosition().distance(sun.getToScalePosition());
	            distances.add(distance);
	            distanceLogs.add(Math.log(Math.abs(distance/epsilon)));
	            //if(i*dt > 20) {
	            lfSum += Math.log(Math.abs(distance/epsilon));
	            largestLyapunov = lfSum/(i*dt);
	            //}
	            Vector3d direction = sun.getToScalePosition().subtract(earth.getToScalePosition()).normalize();
	            sun.setToScalePosition(earth.getToScalePosition().add(direction.mult((double)epsilon)));
	            lorenz2.setPosition(sun.getToScalePosition());
	            moon.setToScalePosition(new Vector3d((double)(dt*i),0,(double)distance));
	            //System.out.println("Slope: " + (double)(Math.log(distance/initialSepDist)/(dt*i)));
	           
	            y1 = earthUp.getToScalePosition().subtract(earth.getToScalePosition());
	            y2 = earthRight.getToScalePosition().subtract(earth.getToScalePosition());
		       double currentArea = y1.cross(y2).length(); 
	           
	            
	            smallestLyapunov = Math.log(currentArea/initialArea)/(i*dt);
	            posGraph.update();
	            posGraph2.update();
	            try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        System.out.println("Largest Lyapunov:" + largestLyapunov);
	        System.out.println("Smallest Lyapunov:" + smallestLyapunov);
	    }

	}

