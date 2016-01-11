package dk.ms.SponsorsAndAgencies;

public class TestMain {

	public static void main(String[] args) {
		
		int numberOfIterations			= 1000;
		int initialNumberOfSponsors 	= 50;
		int initialNumberOfAgencies 	= 100;
		MoveSetting ms 					= MoveSetting.CLOSER_TO_SPONSOR;
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
		WriteMethod writeMethod = WriteMethod.TO_DATABASE;
		World world = new World(numberOfIterations, initialNumberOfSponsors,initialNumberOfAgencies, 
				                ms, cm, ws,sponsorSigmaFactor, sponsorMoney, agencyMoney,agencyMoneyReserveFactor,
				                agencySigmaFactor,agencyRequirementNeed,
			                    agencyRequirementSigma,sightOfAgency, pickRandomSponsor, writeMethod, moveRate);
//		world.initialise();
//		world.seekPotentialSponsors();
//		world.allocateSponsor();
//		world.allocateFunding();
//		world.spendBudget();
//		world.writeAgencies();
//		world.removeExhaustedAgencies();
//		world.generateNewAgencies(); 
		world.orchestrateWorld();
	}

}
