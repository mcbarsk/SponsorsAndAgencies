package dk.ms.SponsorsAndAgenciesControl;


import java.util.ArrayList;
import java.util.Collections;
import dk.ms.Statistics.*;

public class TestMain {

	public static void main(String[] args) {

		int numberOfIterations			= 100;
		int initialNumberOfSponsors 	= 50;
		int initialNumberOfAgencies 	= 100;
		// CutDownModel cm 				= CutDownModel.SAME_PERCENTAGE_RATE;
		 CutDownModel cm 				= CutDownModel.PROBABILITY_CALCULATION;
		int[] ws 						= {5,5};
		double sponsorSigmaFactor 		= 6; 
		double sponsorMoney 			= 50;
		double agencyMoney 				= 10 ;
		int agencyMoneyReserveFactor 	= 5;
		double agencySigmaFactor 		= 6;
		double agencyRequirementNeed	= 0.92;
		double agencyRequirementSigma	= 0.2;
		double sightOfAgency 			= 2;
		boolean pickRandomSponsor 		= false;
		double moveRate					= 0.5;
		double budgetIncrease			= 1.02;
		double baseRisk					= 0.25;
		 WriteMethod writeMethod = WriteMethod.NONE;
		 Settings settings = new Settings();
		// WriteMethod writeMethod = WriteMethod.TO_DATABASE;
		// WriteMethod writeMethod = WriteMethod.TO_FILE;
		World world;
		Statistics statistics = new Statistics();
		//
		int ab = 2;
		if(ab==1){
			Utilities util = new Utilities();
			ArrayList<Double> data = new ArrayList<Double>();
			for (int j=0; j<25;j++){
				data.clear();
				for(int i = 0;i< 1000;i++){
					double number = util.gaussian(10, 0.92);
					data.add(number);              // populate data
					
				}
				Collections.sort(data);
				statistics.setData(data);
				statistics.calculate();
			//	System.out.println("Kurtosis: " + (stat.getKurtosis() +3) + "\tSkewness: " + stat.getSkewness() + "\t\tdev:" + stat.getStandardDeviation());
			//	System.out.println("mean: descriptive" + (stat.getMean()) + "\tMoment: " + firstmoment.getResult() + "\tsecond: " + ((FirstMoment)secondmoment).getResult());
				
				System.out.println(rpad(statistics.getLMean(),23) + rpad(statistics.getLMean(),23) +  rpad(statistics.getLLcv(),23) + rpad(statistics.getLcv(),23) + 
						           rpad(statistics.getLSkewness(),23) + rpad(statistics.getSkewness(),23) + rpad(statistics.getLKurtosis(),23) + rpad(statistics.getKurtosis(),23) );
				
			}
		}
		else{
			world = new World(numberOfIterations, initialNumberOfSponsors,initialNumberOfAgencies, 
					cm, ws,sponsorSigmaFactor, sponsorMoney, agencyMoney,agencyMoneyReserveFactor,
					agencySigmaFactor,agencyRequirementNeed,
					agencyRequirementSigma,sightOfAgency, pickRandomSponsor, writeMethod, moveRate,
					budgetIncrease,baseRisk,settings);
				listener thr ; //= new listener();
				thr = new listener();
				thr.run();
				world.addListener(thr);
			world.orchestrateWorld();
		}
	}
		//		world.initialise();
		//		world.seekPotentialSponsors();
		//		world.allocateSponsor();
		//		world.allocateFunding();
		//		world.spendBudget();
		//		world.writeAgencies();
		//		world.removeExhaustedAgencies();
		//		world.generateNewAgencies(); 
	
	public static String rpad(double inStr, int finalLength)
	{
	    return (inStr + "                 " // typically a sufficient length spaces string.
	        ).substring(0, finalLength);
	}
	
	public static class listener extends Thread implements publishProgress {
		public void getProgress(String msg){
			System.out.println(msg);
		};
	};

} // TestMain

