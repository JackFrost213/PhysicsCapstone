package LorentzSimulationTest;

public class TransformationTest {

	public static void main(String[] args) {
		double[] temp = new double[]{-5,-4,-2,-2,-2,20,30};
		double[] transformed = new double[temp.length];
		
		double sum = 0;
		double minValue = Double.POSITIVE_INFINITY;
		double maxValue = Double.NEGATIVE_INFINITY;
		for(int x = 0; x<temp.length; x++) {
			if(temp[x] < minValue) {
				minValue = temp[x];
			}
			if(temp[x] > maxValue) {
				maxValue = temp[x];
			}
		}
		for(int x = 0; x<temp.length; x++) {
			transformed[x] = (temp[x]-minValue)/(maxValue-minValue);
		}
		
		for(double d : transformed) {
			System.out.print(d + ", ");
		}
		//new min to be 0 and new max to be 1
		//dif between max value and min value to be 1, then min value is increased to be 0
		
	}
}
