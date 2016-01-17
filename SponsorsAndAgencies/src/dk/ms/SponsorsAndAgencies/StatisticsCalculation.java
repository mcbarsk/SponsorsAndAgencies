package dk.ms.SponsorsAndAgencies;

import java.util.ArrayList;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class StatisticsCalculation {
	private final DescriptiveStatistics stat;
	
	public StatisticsCalculation(){
	stat = new DescriptiveStatistics();
	}
	
	public double calculateKurtosis(double [] inpValues){
		stat.clear();
		for (int i = 0; i<inpValues.length;i++){
			stat.addValue(inpValues[i]);
		}
		return stat.getKurtosis();
	}
}
