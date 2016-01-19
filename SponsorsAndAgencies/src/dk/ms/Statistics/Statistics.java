package dk.ms.Statistics;

import java.util.ArrayList;
import java.lang.NullPointerException;

public class Statistics {
	private double beta0 	= 0;
	private double beta1 	= 0;
	private double beta2 	= 0;
	private double beta3 	= 0;
	private double l1	 	= 0;
	private double l2	 	= 0;
	private double l3 	 	= 0;
	private double l4 	 	= 0;
	private double mean  	= 0;
	private double lcv   	= 0;
	private double skewness	= 0;
	private double kurtosis = 0;
	private ArrayList<Double> data;
	
	
	public void addValue(double point){
		data.add(point);
	}
	
	public double getMean() {
		return mean;
	}

	public double getLcv() {
		return lcv;
	}

	public double getSkewness() {
		return skewness;
	}

	public double getKurtosis() {
		return kurtosis;
	}

	public void setData(ArrayList<Double> data){
		this.data = data;
	}
	
	public void calculate(){
		
		if (data == null) throw new NullPointerException("data has not been set!");
		calculateBetaValues();
		setLValues();
		calculateMean();
		calculateLCV();
		calculateSkewness();
		calculateKurtosis();
		
	}
	
	private void calculateBetaValues(){
		// Beta0 = L1 = L-mean
		double size = data.size();
		double n_minus_one = size - 1;
		double n_minus_two = size - 2;
		double n_minus_three = size - 3;
		double denominatorBeta2 = n_minus_one * n_minus_two;
		double denominatorBeta3 = n_minus_one * n_minus_two * n_minus_three;
		for (int i=1;i<=size;i++){
			double XValue = data.get(i -1);
			beta0 += XValue;
			beta1 += XValue * ( (double)(i-1)  /  n_minus_one  );
			beta2 += XValue * ( ( (double)(i-1)*(double)(i-2) ) / denominatorBeta2  ); // SUM:Xj[(j-1)(j-2)]/[(n-1)(n-2)]
			beta3 += XValue * ( ( (double)(i-1)*(double)(i-2)*(double)(i-3) ) / denominatorBeta3) ; // SUM:Xj[(j-1)(j-2)(j-3)]/[(n-1)(n-2)(n-3)]
		}
		beta0 += data.get(0); // as the for loop does not provide the first value
		beta0 = beta0/size;
		beta1 = beta1/size;
		beta2 = beta2/size;
		beta3 = beta3/size;
	}
	private void setLValues(){
		l1 = beta0;
		l2 = (2*beta1 - beta0);
		l3 = (6*beta2) - (6*beta1) + (beta0);
		l4 = (20*beta3) - (30*beta2) + (12*beta1) - (beta0);
		
	}
	private void calculateMean(){
		mean = l1;
	}
	
	private void calculateLCV(){
		lcv = l2/l1;		
	}
	
	private void calculateSkewness(){
		skewness = l3/l2;
	}
	
	private void calculateKurtosis(){
		kurtosis = l4/l2;
	}
	
	
	
}
