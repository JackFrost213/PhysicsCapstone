package plotting;

import java.text.NumberFormat;

import org.jfree.chart.axis.NumberTickUnit;

public class CustomNumberTickUnit extends NumberTickUnit {

	public CustomNumberTickUnit(double size, NumberFormat formatter) {
		super(size, formatter);
	}

	public String valueToString(double value) {
		String temp = super.valueToString(value);
		if(value == 1.0) {
			temp = "Most Chaotic: " + temp;
		}
		else if(value == 0.0) {
			temp = "Least Chaotic: " + temp;
		}
		return temp;
	}
}
