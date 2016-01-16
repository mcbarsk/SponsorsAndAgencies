package dk.ms.SponsorsAndAgencies;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class TestMain {

	public static void main(String[] args) {

		int numberOfIterations			= 1;
		int initialNumberOfSponsors 	= 50;
		int initialNumberOfAgencies 	= 270;
		CutDownModel cm 				= CutDownModel.SAME_PERCENTAGE_RATE;
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
		WriteMethod writeMethod = WriteMethod.NONE;
		World world;
		//
		int ab = 2;
		if(ab==2){
			Utilities util = new Utilities();
			DescriptiveStatistics stat;
			stat = new DescriptiveStatistics();
			stat.clear();
			double[] dArray = new double[1000];
			for (int j=0; j<25;j++){
				for(int i = 0;i< 1000;i++){
					dArray[i] = util.gaussian(10, 1.6667);
					stat.addValue(dArray[i]);
					//	System.out.println(dArray[i]);
				}

				System.out.println("Kurtosis: " + (stat.getKurtosis() +3) + "\tSkewness: " + stat.getSkewness() + "\t\tdev:" + stat.getStandardDeviation());
				
			}
		}
		else{
			world = new World(numberOfIterations, initialNumberOfSponsors,initialNumberOfAgencies, 
					cm, ws,sponsorSigmaFactor, sponsorMoney, agencyMoney,agencyMoneyReserveFactor,
					agencySigmaFactor,agencyRequirementNeed,
					agencyRequirementSigma,sightOfAgency, pickRandomSponsor, writeMethod, moveRate);
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
	}

}
