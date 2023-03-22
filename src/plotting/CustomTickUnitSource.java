package plotting;

import org.jfree.chart.axis.StandardTickUnitSource;
import org.jfree.chart.axis.TickUnit;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.NumberTickUnitSource;
import org.jfree.chart.axis.TickUnitSource;

public class CustomTickUnitSource extends NumberTickUnitSource {
	
	
	
	@Override
	public TickUnit getLargerTickUnit(final TickUnit unit) {
		TickUnit temp = super.getLargerTickUnit(unit);
		System.out.println("HA:" + temp.getSize());
		return temp;
	}


}