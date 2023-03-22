package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.jme3.math.Vector3f;

import net.wcomohundro.jme3.math.Vector3d;
import precreatedScenarios.Simulation;

public class FileIO {

	
	public static void writeFile(String filename, String filesetup, ArrayList<ChaosNumber> chaosValues) {
		int index = 0;
		try {
		      File myObj = new File("src/Data/", filename + index + ".txt");
		      //System.out.println(myObj.getCanonicalPath());
		      while(!myObj.createNewFile()){
		    	  index++;
		    	  myObj = new File("src/Data/" + filename + index + ".txt");
		      }
		      
		      
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		
		 try {
		      FileWriter myWriter = new FileWriter("src/Data/" + filename + index + ".txt");
		      myWriter.write(filesetup + "----------\n");
		      for(ChaosNumber cN : chaosValues) {
		    	  myWriter.write(cN.getPosition() + "," + cN.getLyapunovExponents() +"\n");
		      }
		      myWriter.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	}
	
	public static Object[] readFile(String fileName) {
		ArrayList<ChaosNumber> chaosValues = new ArrayList<ChaosNumber>();
		int[] parameters = new int[5];
		double[] ranges = new double[4];
		Class<? extends Simulation> classType = null;
		
		 try {
			BufferedReader reader = new BufferedReader(new FileReader("src/Data/" + fileName + ".txt"));
			int lineNumber = 0;
			String s = reader.readLine();
			
			while(s != null) {
				if(lineNumber >= 2 && lineNumber <= 6) {
					parameters[lineNumber-2] = readDataLine(s);
				}
				if(lineNumber == 7) {
					ranges = readDataRange(s);
				}
				if(lineNumber == 9) {
					String className = readDataLineS(s);
					try {
						classType = (Class<? extends Simulation>) Class.forName(className);
					}
					catch(Exception e) {
						
					}
				}
				if(lineNumber >= 11) {
					ChaosNumber temp = readChaosNumber(s);
					chaosValues.add(temp);
				}
				lineNumber++;
				s = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		Object[] returnedData = {chaosValues, parameters, ranges, classType};
		return returnedData;
	}
	
	private static int readDataLine(String s) {
		String temp = readDataLineS(s);
		return Integer.valueOf(temp);
	}
	
	private static String readDataLineS(String s) {
		String temp = s.split("\"")[1];
		return temp;
	}
	
	private static double[] readDataRange(String s) {
		String temp = s.split("\"")[1];
		temp = temp.substring(1,temp.length()-1);
		double[] range = new double[4];
		String[] tempRange = new String[4];
		tempRange = temp.split(",");
		for(int x = 0; x < tempRange.length; x++) {
			range[x] = Double.valueOf(tempRange[x]);
		}
		return range;
	}
	
	private static Vector3d readVector(String s) {
		Vector3d temp = new Vector3d();
		String tempString = s.substring(1,s.length()-1);
		String[] arr = tempString.split(",");
		temp.set(Double.valueOf(arr[0]), Double.valueOf(arr[1]), Double.valueOf(arr[2]));
		return temp;
	}
	
	
	private static ChaosNumber readChaosNumber(String s) {
		Vector3d pos = new Vector3d();
		Vector3d chaosValue = new Vector3d();
		String[] arr = s.split("\\),\\(");
		arr[0] += ")";
		arr[1] = "(" + arr[1];
		pos = readVector(arr[0]);
		chaosValue = readVector(arr[1]);
		ChaosNumber temp = new ChaosNumber(0, pos);
		temp.setLyapunovExponents(chaosValue);
		
		return temp;
		
	}
	
}
