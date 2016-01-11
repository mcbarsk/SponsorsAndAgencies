package dk.ms.SponsorsAndAgencies;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import dk.ms.SponsorsAndAgencies.Utilities;
import dk.ms.writer.*;


public class World {
	private String 				worldID;
	private Timestamp           creationDate;
	private int 				initialNumberOfSponsors;
	private int 				initialNumberOfAgencies;
	private MoveSetting 		moveSetting;
	private CutDownModel		cutDownModel;
	private int[] 				worldSize = new int[] {5,5};
	private ArrayList<Agency> 	LAgencies;
	private ArrayList<Sponsor> 	LSponsors;
	private Utilities	        sponsorUtilities; 
	private Utilities			agencyUtilities;	
	private double				sponsorSigmaFactor 			=  6; 
	private double				sponsorMoney				= 10;
	private double 				agencyMoney					= 50;
	private int					agencyMoneyReserveFactor	=  5;
	private double				agencySigmaFactor         	=  6;
	private double				agencyRequirementNeed		= 0.92;
	private double				agencyRequirementSigma		= 0.2;
	private double				sightOfAgency				= 2;
	private double 				moveRate					= 0.5;
	private boolean				pickRandomSponsor			= false;
	private int					numberOfIterations			= 1000;
	private int					totalNumberOfAgencies 		= 0; // singleton to ensure new agencies get a unique number
	private WriteMethod			writeMethod;
	private SponsorsAndAgenciesWriter	writer;				
	private NumberFormat		formatter = new DecimalFormat("#0.00000");
	private long 				start;
	private long				end;
	private Settings			settings; 

	public World(int numberOfIterations,
			int initialNumberOfSponsors, 
			int initialNumberOfAgencies, 
			MoveSetting movesetting, 
			CutDownModel cutDownModel,
			int[] worldSize,
			double sponsorSigmaFactor,
			double sponsorMoney,
			double agencyMoney,
			int agencyMoneyReserveFactor,
			double agencySigmaFactor,
			double agencyRequirementNeed,
			double agencyRequirementSigma,
			double sightOfAgency,
			boolean pickRandomSponsor,
			WriteMethod writeMethod,
			double moveRate
			){
		// Validation
		if(numberOfIterations < 1)
			throw new IllegalArgumentException("Number of iterations must be > 0");
		if(moveRate > 1 || moveRate < 0)
			throw new IllegalArgumentException("MoveRate must be in the range [0..1]");
		if(initialNumberOfSponsors < 1)
			throw new IllegalArgumentException("Number of sponsors must be > 0");
		if(initialNumberOfAgencies < 1)
			throw new IllegalArgumentException("Number of agencies must be > 0");
		if (movesetting == null)
			throw new IllegalArgumentException("Specify a movesetting");
		// End of validation
		if (cutDownModel == null)
			throw new IllegalArgumentException("Specify a cut down model");
		
		LAgencies 						= new ArrayList<Agency>(); // container for agencies
		LSponsors 						= new ArrayList<Sponsor>(); // container for sponsors
		worldID 						= String.valueOf(UUID.randomUUID()); // generates a unique ID for the world
		Date date 						= new Date();
		creationDate 					= new Timestamp(date.getTime()); // when is the world created
		this.numberOfIterations			= numberOfIterations;
		this.initialNumberOfSponsors 	= initialNumberOfSponsors;
		this.initialNumberOfAgencies 	= initialNumberOfAgencies;
		this.worldSize 					= worldSize.clone(); // sets internal worldsize
		this.moveSetting				= movesetting;
		this.cutDownModel				= cutDownModel;
		sponsorUtilities				= new Utilities();
		agencyUtilities					= new Utilities();
		this.sponsorSigmaFactor			= sponsorSigmaFactor;
		this.sponsorMoney				= sponsorMoney;
		this.agencyMoney				= agencyMoney;
		this.agencyMoneyReserveFactor	= agencyMoneyReserveFactor; // for setting agency money reserve.
		this.agencySigmaFactor			= agencySigmaFactor;
		this.agencyRequirementNeed		= agencyRequirementNeed;    // for setting real requirements
		this.agencyRequirementSigma		= agencyRequirementSigma;   // for setting real requirements
		this.sightOfAgency				= sightOfAgency;
		this.pickRandomSponsor			= pickRandomSponsor;
		this.moveRate					= moveRate;
		this.writeMethod				= writeMethod;
		switch (writeMethod){
		case TO_DATABASE:
			writer = new WriterSQL();
			break;
		case TO_FILE:
			break;
		}
		settings 						= new Settings();



	}; // World
	
	public void initialise(){	// Step 1
		//setstart();
		// Create Sponsors
		for (int i = 0; i<initialNumberOfSponsors;i++ ){
			LSponsors.add(new Sponsor(sponsorUtilities, worldID, creationDate,i,worldSize[0],worldSize[1],sponsorMoney,sponsorSigmaFactor));
		} // 
		// Create Agencies
		for(int i = 0; i<initialNumberOfAgencies;i++){
			Agency agency;
			agency = new Agency(agencyUtilities,worldID,creationDate,i,worldSize[0],worldSize[1], agencyMoney,agencyMoney/agencySigmaFactor,sightOfAgency, agencyMoneyReserveFactor);
			double budget = agency.getBudget();
			agency.setMoneyNeeded(agencyUtilities, budget * agencyRequirementNeed, budget * agencyRequirementSigma);
			LAgencies.add(agency);
			totalNumberOfAgencies += 1;
		}
		//setend(); 
		//log (start, end, "initialise");
	}; // initialise

	private void setstart(){ start = System.currentTimeMillis();}
	private void setend(){ 	end = System.currentTimeMillis();}
	private double distance(Sponsor sponsor, Agency agency){ // calculate distance between sponsor and agency
		return Utilities.distance(agency.getPosition()[0],agency.getPosition()[1],sponsor.getPosition()[0],sponsor.getPosition()[1]); 
	}; // distance	


	public void seekPotentialSponsors(){ // Step 2
		// setstart();
		for (int i=0; i<LAgencies.size();i++){
			Agency agency = LAgencies.get(i);
			agency.getPossibleSponsors().clear(); // start all over with new potential sponsors
			double maxdistance = agency.getEyesight();
			for (int j=0;j<LSponsors.size();j++){
				Sponsor sponsor = LSponsors.get(j);
				if (maxdistance>=distance(sponsor,agency))
					agency.addSponsor(sponsor);
			}
		}
		//setend(); 
		//log(start,end,"step2");
	} // seekPotentialSponsors

	public void allocateSponsor(){ // Step 3
		// setstart();
		for (int i=0;i<LAgencies.size();i++){
			Agency agency = LAgencies.get(i);
			if(pickRandomSponsor){ // finds a random index in the list of possible sponsors
				int size = agency.getPossibleSponsors().size();
				if (size > 0) {
					int sponsorIndex = (int) (Math.random() * size); 
					Sponsor sponsor = agency.getPossibleSponsors().get(sponsorIndex);
					agency.setSponsor(sponsor);
					sponsor.addAgency(agency);}
				else agency.setSponsor(null);
			} else{ // normal finding of valid sponsor
				int sponsorIndex = returnClosestSponsorIndex(agency); // look for close sponsor with enough money
				if (sponsorIndex < 0) // close sponsor with sufficient money is not found
					sponsorIndex = returnClosestSponsorIndexHighestValue(agency); // find best value 
				if (sponsorIndex >= 0){	
					Sponsor sponsor = agency.getPossibleSponsors().get(sponsorIndex);
					if (!agency.noSponsors()){
						agency.getSponsor().removeAgency(agency); // clean up old sponsor
					}
					agency.setSponsor(sponsor);
					sponsor.addAgency(agency);
				};
			}
		}
		//setend();
		//log(start,end,"step3");
	} // allocateSponsor

	public void allocateFunding(){ // Step 4
		//setstart();
		resetCutDown();                           // initialises the agencies. 
		for (int i=0; i < LSponsors.size();i++){
			Sponsor sponsor = LSponsors.get(i);
			Payout.payout(cutDownModel, sponsor);
		}
		//setend();
		//log(start,end,"step4");
	} // allocateFunding

	public void spendBudget(){ // Step 5
		for (int i=0;i<LAgencies.size();i++){
			Agency agency = LAgencies.get(i);
			agency.subtractsavings(agency.getMoneyNeeded() - agency.getPayout());
		}
		//log(start,end,"step5");
	} // spendBudget

	public void removeExhaustedAgencies(){ // Step 6
		//setstart();
		Agency agency;
		ArrayList<Agency> tmpList = new ArrayList<Agency>(LAgencies); // tmplist created, so removal of entries doesn't affect the loop
		for (int i=0;i<tmpList.size();i++){
			agency = tmpList.get(i);
			if (agency.getBroke()){
				if (agency.getSponsor() != null)   // If a sponsor is assigned, then the sponsor need to be updated
					agency.getSponsor().removeAgency(agency); 
				LAgencies.remove(agency);              // remove the agency from the global container.
			}
		}
		//setend();
		//log(start,end,"step6");
	} // removeExhaustedAgencies

	public void generateNewAgencies(){ // Step 7
		//setstart();
		double totalSponsorMoney = 0;
		double totalAgencyRequirement = 0;
		int i;
		for (i=0; i<LSponsors.size();i++){
			totalSponsorMoney += LSponsors.get(i).getMoney();
		}
		for (i=0;i<LAgencies.size();i++){
			totalAgencyRequirement += LAgencies.get(i).getBudget();
		}
		double avgBudget = totalAgencyRequirement/LAgencies.size();
		int newAgencies = (int)((totalSponsorMoney - totalAgencyRequirement) / avgBudget); // int cast rounds automatically.
		newAgencies = (int)sponsorUtilities.gaussian(newAgencies, newAgencies * 0.02);
		for (i=1;i<newAgencies;i++){
			Agency agency;
			totalNumberOfAgencies += 1;
			agency = new Agency(agencyUtilities,worldID, creationDate,totalNumberOfAgencies,worldSize[0],worldSize[1], agencyMoney,agencyMoney/agencySigmaFactor,sightOfAgency, agencyMoneyReserveFactor);
			agency.setMoneyNeeded(agencyUtilities, avgBudget, avgBudget * 0.02);
			LAgencies.add(agency);

		}
		//setend();
		//log(start,end,"step7)");
	} // generateNewAgencies

	public void setBudgetRequirements(){ // Step 8
		/* New budgets are allocated.
		 * These are allocated with the algorithm: current_budget *gauss(MU,SIGMA) where MU = 1 and SIGMA = 0.02, hence the constants.
		 */
		final double MU = 1;
		final double SIGMA = 0.02;
		for (int i=0;i< LAgencies.size();i++){
			Agency agency = LAgencies.get(i);
			agency.setNewBudget(agencyUtilities, MU,SIGMA);
			agency.setMoneyNeeded(agencyUtilities, agency.getBudget() * agencyRequirementNeed, agency.getBudget() * agencyRequirementSigma);
		}
		/* Now payoff is reset for all sponsors
		 * 
		 */
		for (int j=0;j<LSponsors.size();j++){
			LSponsors.get(j).setPayoff(0);
		}
		//setend();
		//log(start,end,"step8");
	}

	public void orchestrateWorld(){
		/* This method arranges all the steps and iterates.
		 */
		setstart();
		initialise();
		for (int i=1;i<=numberOfIterations;i++){
			seekPotentialSponsors();
			allocateSponsor();
			allocateFunding();
			spendBudget();
			write(i);
			removeExhaustedAgencies();
			generateNewAgencies();
			setBudgetRequirements();
			move();
		}
		setend();
		log(start,end,"iteration:" + numberOfIterations);
	}
	public void write(int iteration){
		writer.prepare(this);
		writer.writeData(LAgencies,LSponsors, iteration);
	} // write

	public void move(){
		Moving.move(moveSetting, LAgencies, LSponsors, moveRate);
	} // move
	//  private utility methods
	private void log(long start, long end, String s){ // for debugging purposes

		System.out.println("Execution time is " + formatter.format((end - start) /1000d) + " seconds - " + s);
	}// move

	private int returnClosestSponsorIndex(Agency agency){
		int returnValue = -1;
		double minDistance = 99999;
		int size = agency.getPossibleSponsors().size();
		for (int i=0; i<size;i++){
			Sponsor sponsor = agency.getPossibleSponsors().get(i);
			double distance = distance(sponsor, agency);
			if (agency.getBudget() + calculatePayoff(sponsor, agency) < sponsor.getMoney())
				if (distance < minDistance){
					minDistance = distance;
					returnValue = i;
				}
		}
		return returnValue;
	} // returnClosestSponsorIndex

	private int returnClosestSponsorIndexHighestValue(Agency agency){
		int returnValue = -1;
		double minDistance = 99999;
		double bestValue    = -99999;
		int size = agency.getPossibleSponsors().size();
		for (int i=0; i<size;i++){
			Sponsor sponsor = agency.getPossibleSponsors().get(i);
			double distance = distance(sponsor, agency);
			double comparison = sponsor.getMoney() - agency.getBudget() - calculatePayoff(sponsor, agency); // calculate best money reserve  
			if ((bestValue < comparison) || (bestValue == comparison &&  (distance < minDistance))){ // a better or similar value has been found. Distance becomes a factor
				minDistance = distance; 
				bestValue = comparison;
				returnValue = i;
			}
		}
		return returnValue;
	}// returnClosestSponsorIndexHighestValue

	private void resetCutDown(){
		for (int i=0;i<LAgencies.size();i++){
			LAgencies.get(i).setCutDown(false);
		}
	}

	private double calculatePayoff(Sponsor sponsor, Agency agency){
		// calculates potential money a sponsor is already paying, which is a factor for finding sponsors.
		// agency is an optional input. If populated, it will not add the actual agency's budget to the total payout.
		double returnValue = 0;
		for (int i=0;i<sponsor.getAgencies().size();i++){
			Agency agencylocal;
			agencylocal = sponsor.getAgencies().get(i);
			if (agency == null || agency != agencylocal) // avoid adding input agency budget.
				returnValue += agencylocal.getBudget();
		}
		return returnValue;
	}
/*
 * Small getters to retrieve some world specific settings, embedded in the private class: Settings. 
 */
	public String getConnectionUrl(){return settings.connectionUrl;	}
	public String getPath(){return settings.saveLocation;};
	/* 
	 * Following is a set of private classes which helps make different types of calculation. 
	 * If new enum values are created, this is the place to implement the corresponding code.
	 * 	
	 */
	static final class Moving { // private class for performing movement.
		private static void move(MoveSetting ms, ArrayList<Agency> LAgencies, ArrayList<Sponsor> LSponsors, double moveRate){
			switch (ms){
			case CLOSER_TO_SPONSOR:
				moveCloserToSponsor(LAgencies, LSponsors, moveRate);
				break;
			case MOVE_AT_RANDOM:
				moveAtRandom(LAgencies, LSponsors);
				break;
			case MOVE_FOR_BETTER_SPONSOR:
				moveForBetter(LAgencies, LSponsors);
				break;
			case NO_MOVEMENT:
			};
		}	
		private static void moveCloserToSponsor(ArrayList<Agency> Agencies, ArrayList<Sponsor> Sponsors, double moveRate){
			int agencySize = Agencies.size();
			for (int i=0;i<agencySize;i++){
				Agency agency = Agencies.get(i);
				Sponsor sponsor = agency.getSponsor(); 
				if (sponsor != null){
					double[] agencyPos  = agency.getPosition();
					double[] sponsorPos = sponsor.getPosition();
					double x = agencyPos[0] + ((sponsorPos[0] - agencyPos[0]) * moveRate);
					double y = agencyPos[1] + ((sponsorPos[1] - agencyPos[1])  * moveRate);
					agency.setPosition(x,y);
				}
			}
		} // moveCloserToSponsor

		private static void moveAtRandom(ArrayList<Agency> Agencies, ArrayList<Sponsor> Sponsors){

		} // moveAtRandom
		private static void moveForBetter(ArrayList<Agency> Agencies, ArrayList<Sponsor> Sponsors){
		} // moveForBetter
	} // class Moving

	static class Payout { // private class for calculating payout based on payment model
		private static void payout(CutDownModel model, Sponsor sponsor){
			/* If a sponsor has sufficient money, the cutDownModel is never used. Hence the invocation of sufficientMoneyForSponsor
			 * at the very beginning of this routine.
			 */
			boolean sufficientMoney = sufficientMoneyForSponsor(sponsor);
			if (sufficientMoney)
				payoutSufficient(sponsor);
			else {

				switch(model){
				case SAME_PERCENTAGE_RATE:
					double rate = calculatePercentage(sponsor);
					payoutPercentageRate(rate, sponsor);
					break;
				case PROBABILITY_CALCULATION: 
					break;
				case PROBABILITY_TIME_CALCULATION:
					break;
				}
			}

		}; // Payout

		private static boolean sufficientMoneyForSponsor(Sponsor sponsor){
			int lines = sponsor.getAgencies().size();
			Agency agency;
			double totalMoneyNeeded = 0;
			for (int i=0; i<lines;i++){
				agency = sponsor.getAgencies().get(i);
				totalMoneyNeeded += agency.getBudget();
			}
			return totalMoneyNeeded < sponsor.getMoney();
		} // sufficientMoneyForSponsor

		private static void payoutSufficient(Sponsor sponsor){
			double totalpayout = 0;
			int lines = sponsor.getAgencies().size();
			//sponsor.
			Agency agency;
			for (int i=0; i<lines;i++){
				agency = sponsor.getAgencies().get(i); // get agency
				totalpayout += agency.getBudget();          // add budget to total amount
				agency.setPayout(agency.getBudget());  // perform the payout
			}
			sponsor.setPayoff(totalpayout); 				// finally set the payoff for the sponsor

		} // payoutSufficient

		private static void payoutPercentageRate(double rate,Sponsor sponsor){
			double payout = 0;
			int lines = sponsor.getAgencies().size();
			//sponsor.
			Agency agency;
			for (int i=0; i<lines;i++){
				agency = sponsor.getAgencies().get(i); // get agency
				payout += rate * agency.getBudget();          // add budget to total amount
				agency.setPayout(rate * agency.getBudget());  // perform the payout
				agency.setCutDown(true);                      // an agency has been cut. This could affect, whether it chooses to move.
			}
			sponsor.setPayoff(payout); 				// finally set the payoff for the sponsor
		} // payoutPercentageRate

		private static double calculatePercentage(Sponsor sponsor){
			double totalAmount = 0;
			double totalAgencyNeed = 0;
			double percentageOfPayout;
			Agency agency;
			totalAmount = sponsor.getMoney();
			for (int i = 0;i<sponsor.getAgencies().size();i++){
				agency = sponsor.getAgencies().get(i);
				totalAgencyNeed += agency.getBudget();
			}
			if (totalAgencyNeed == 0){  // no reason to divide by 0, if there is no moneyneed
				percentageOfPayout = 0;
				return percentageOfPayout;
			}
			else{
				percentageOfPayout = (1 - (totalAgencyNeed - totalAmount)/totalAgencyNeed); 
				if (percentageOfPayout > 1) 
					percentageOfPayout = 1;
				return percentageOfPayout;
			}	
		} // calculatePercentage
	} // class Payout
	
	private static class Settings{
		private String connectionUrl = "jdbc:mysql://localhost:3306/sponsors_agencies"  + "?useSSL=false";
		private String saveLocation = "C:\\";
	}

} // Class World
