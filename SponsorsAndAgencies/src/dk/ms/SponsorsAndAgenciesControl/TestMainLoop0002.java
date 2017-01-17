package dk.ms.SponsorsAndAgenciesControl;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import dk.ms.Statistics.*;



public class TestMainLoop0002 {

	public static void main(String[] args) {
		int ite = 1;
		for(int repeater : Arrays.asList(1, 2, 3)) {		
		for(int sponsnum : Arrays.asList(25, 50, 100, 200)) {		//Full: 25, 50, 100, 200. 
		for(int agentsight : Arrays.asList(1,3,5,7,9)) {			//Full: 1,3,5,7,9
		for(int sponsmoney : Arrays.asList(25,50,75)) {				//Full: 25,50,75
		for(Double agentneed : Arrays.asList(0.92, 0.95, 0.98)){	//Full: 0.92, 0.95, 0.98	
		for(Double moverat : Arrays.asList(0.25, 0.50, 0.75)) {
		for(Double baserisk : Arrays.asList(0.25)) { 				//Full: 0.2, 0.4, 0.6, 0.8	(Default: 0.25)			
		for(Double b1_num : Arrays.asList(-0.5, -0.2)) {			//First run -0.5 and -0,2 with b0 set to 1 (below), and then 0.5 and 0.2 with b0 set to -1
		
		int numberOfIterations			= 121;
		int initialNumberOfSponsors 	= sponsnum;
		int initialNumberOfAgencies 	= 100;
		// CutDownModel cm 				= CutDownModel.SAME_PERCENTAGE_RATE;
		// CutDownModel cm 				= CutDownModel.PROBABILITY_CALCULATION;
		 CutDownModel cm				= CutDownModel.PROBABILITY_TIME_CALCULATION;
		int[] ws 						= {5,5};
		double sponsorSigmaFactor 		= 6; 
		double sponsorMoney 			= sponsmoney;
		boolean respectSponsorMoney     = true;
		double agencyMoney 				= 10 ;
		int agencyMoneyReserveFactor 	= 5;
		double agencySigmaFactor 		= 6;
		double agencyRequirementNeed	= agentneed;
		double agencyRequirementSigma	= 0.2;
		double sightOfAgency 			= agentsight;
		double moveRate					= moverat;
		double budgetIncrease			= 1.00;
		double baseRisk					= baserisk;
		double b0						= 1;
		double b1						= b1_num;
		WriteMethod writeMethod = WriteMethod.NONE;
	    //WriteMethod writeMethod = WriteMethod.TO_DATABASE;
		//WriteMethod writeMethod = WriteMethod.TO_FILE;
		AllocationMethod am = AllocationMethod.CLOSEST_DISTANCE;
		MoveMethod mm       = MoveMethod.REAL_DISTANCE;				//REAL_DISTANCE or PERCENTAGE_OF_DISTANCE
	
				
		 Settings settings = new Settings();
		World world;
		Statistics statistics = new Statistics();
		//
		
		int ab = 2;
		if(ab==1){
			Utilities util = new Utilities();
			ArrayList<Double> data = new ArrayList<Double>();
			for (int j=0; j<1;j++){
				data.clear();
				for(int i = 0;i< 150000;i++){
					double number = util.gaussian(0, 0.92);
					data.add(number);              // populate data
					//System.out.println(number);
					
				}
				Collections.sort(data);
				statistics.setData(data);
				statistics.calculate();
				System.out.println("Kurtosis: " + (statistics.getKurtosis() +3) + "\tSkewness: " + statistics.getSkewness() +
						           "\tlcv: " + statistics.getLcv() + "\tmean: " + statistics.getMean());
			//	System.out.println("mean: descriptive" + (stat.getMean()) + "\tMoment: " + firstmoment.getResult() + "\tsecond: " + ((FirstMoment)secondmoment).getResult());
				
			//	System.out.println(rpad(statistics.getLMean(),23) + rpad(statistics.getLMean(),23) +  rpad(statistics.getLLcv(),23) + rpad(statistics.getLcv(),23) + 
			//			           rpad(statistics.getLSkewness(),23) + rpad(statistics.getSkewness(),23) + rpad(statistics.getLKurtosis(),23) + rpad(statistics.getKurtosis(),23) );
				
			}
		}
		else{
			world = new World("",numberOfIterations, initialNumberOfSponsors,initialNumberOfAgencies, 
					cm, ws,sponsorSigmaFactor, sponsorMoney, respectSponsorMoney,agencyMoney,agencyMoneyReserveFactor,
					agencySigmaFactor,agencyRequirementNeed,
					agencyRequirementSigma,sightOfAgency,  writeMethod,am, moveRate, mm,
					budgetIncrease,baseRisk,b0,b1,settings);
				listener thr ; //= new listener();
				thr = new listener();
				thr.run();
				world.addListener(thr);
			world.orchestrateWorld();
		}
	
		//		world.initialise();
		//		world.seekPotentialSponsors();
		//		world.allocateSponsor();
		//		world.allocateFunding();
		//		world.spendBudget();
		//		world.writeAgencies();
		//		world.removeExhaustedAgencies();
		//		world.generateNewAgencies(); 
		System.out.println("\r\n" +
						   "Current Iteration: " + ite + " out of 3240(2)" + "\r\n" +
						   "Current time: " + LocalDateTime.now() + "\r\n" +
						   "Current level of Move Rate: " + moverat + "\r\n" +
						   "Current level of Agent Need: " + agentneed + "\r\n" +
						   "Current level of Sponsor Money: " + sponsmoney + "\r\n" +
						   "Current level of Agency Sight: " + agentsight + "\r\n" +
						   "Current level of Number of Sponsors: " + sponsnum + "\r\n" +
						   "Current level of Baserisk: " + baserisk + "\r\n" +
						   "Current level of b0: " + b0 + "and b1: " + b1 + "\r\n" +
						   "Current level of Robustness check: " + repeater);
		ite++;
	}					//MY LOOP BEGIN!!!    Robustness check (Repeater)
	}					//					  Move rate
	}					//					  Agent Need
	}					//					  Sponsor Money
	}					//					  Agency Sight
	}					//				      Baserisk
	}
	}					// My Loop END!!!	  Initial number of Sponsors
		
	}
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

