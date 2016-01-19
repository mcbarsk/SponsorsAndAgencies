package dk.ms.SponsorsAndAgencies;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.moment.*;

public class TestMain {

	public static void main(String[] args) {

		int numberOfIterations			= 1000;
		int initialNumberOfSponsors 	= 50;
		int initialNumberOfAgencies 	= 270;
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
		// WriteMethod writeMethod = WriteMethod.NONE;
		// WriteMethod writeMethod = WriteMethod.TO_DATABASE;
		WriteMethod writeMethod = WriteMethod.TO_FILE;
		World world;
		FirstMoment firstmoment;
		SecondMoment secondmoment;
		//
		int ab = 1;
		if(ab==1){
			Utilities util = new Utilities();
			firstmoment = new FirstMoment();
			secondmoment = new SecondMoment();
			DescriptiveStatistics stat;
			stat = new DescriptiveStatistics();
			stat.clear();
			firstmoment.clear();
			secondmoment.clear();
			double[] dArray = new double[1000];
			for (int j=0; j<25;j++){
				for(int i = 0;i< 1000;i++){
					double number = util.gaussian(10, 1.6667);
					firstmoment.increment(number);
					secondmoment.increment(number);
					dArray[i] = number;
					stat.addValue(dArray[i]);
					//	System.out.println(dArray[i]);
				}

				System.out.println("Kurtosis: " + (stat.getKurtosis() +3) + "\tSkewness: " + stat.getSkewness() + "\t\tdev:" + stat.getStandardDeviation());
				System.out.println("mean: descriptive" + (stat.getMean()) + "\tMoment: " + firstmoment.getResult() + "\tsecond: " + ((FirstMoment)secondmoment).getResult());
				
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
