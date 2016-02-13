package dk.ms.SponsorsAndAgenciesControl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import dk.ms.SponsorsAndAgenciesControl.Utilities;
import dk.ms.SponsorsAndAgenciesModel.Writer.SponsorsAndAgenciesWriter;
import dk.ms.SponsorsAndAgenciesModel.Writer.WriterFile;
import dk.ms.SponsorsAndAgenciesModel.Writer.WriterSQL;
import dk.ms.Statistics.Statistics;

public class World {
	private String 				worldID;
	private Timestamp           creationDate;
	private int 				initialNumberOfSponsors;
	private int 				initialNumberOfAgencies;
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
	private double 				moveRate					= 0.5; // how far should the agency move.
	private MoveMethod			moveMethod;
	private int					numberOfIterations			= 1000;
	private int					totalNumberOfAgencies 		= 0; // singleton to ensure new agencies get a unique number
	private double 				budgetIncrease				= 1.02; // the idea was to increase the agency budget per iteration. Whether this is necessary must be investigated.
	private double 				baseRisk					= 0.25; // used for calculating real risk. This is used by the funding algorithm.
	private static boolean				respectSponsorMoney;		// specifies the sponsor can't be over-allocated by more than one agency.
	private WriteMethod			writeMethod;		// ENUM for creating the writer object.
	private AllocationMethod	allocationMethod; // ENUM for the allocation algorithms
	private SponsorsAndAgenciesWriter	writer;	// the writer object ensures data will be written. The actual object type defines the actions, i.e. write to DB or file.
	private ArrayList<Double> statisticList = new ArrayList<Double>();     // this list gathers the difference in agencySavings and is used for statistics
	private NumberFormat		formatter = new DecimalFormat("#0.00000"); // for logging purposes. Just formatting a number.
	private Settings			settings; // pertains to the settings object, which contains data regarding pw, user, db-specific settings and file location
	private long 				start;
	private long				end;
	private long 				start1; // just for logging performance
	private long				end1;   // just for logging performance
	private boolean 			log = false; // true if runtime output to console.	
	private List<publishProgress> listeners = new ArrayList<publishProgress>();   // to publish iterations
	private int 				actualIteration = -1; // this is incremented per iteration and reported back to the listeners
	private double 				mean;				// these are used for the calculated statistics
	private double 				lcv;
	private double				skewness;
	private double				kurtosis;
	private double				L_lcv;
	private double 				L_skewness;
	private double 				L_kurtosis;

	public World(int numberOfIterations,
			int initialNumberOfSponsors, 
			int initialNumberOfAgencies, 
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
			WriteMethod writeMethod,
			AllocationMethod allocationMethod,
			double moveRate,
			double budgetIncrease,
			double baseRisk,
			Settings settings
			){
		// Validation
		if(numberOfIterations < 1)
			throw new IllegalArgumentException("Number of iterations must be > 0");
		if(Double.compare(moveRate, 1) > 0 || Double.compare(moveRate,0) < 0)
			throw new IllegalArgumentException("MoveRate must be in the range [0..1]");
		if(initialNumberOfSponsors < 1)
			throw new IllegalArgumentException("Number of sponsors must be > 0");
		if(initialNumberOfAgencies < 0)
			throw new IllegalArgumentException("Number of agencies must be >= 0");
		if (cutDownModel == null)
			throw new IllegalArgumentException("Specify a cut down model");
		if (allocationMethod == null)
			throw new IllegalArgumentException("Specify an allocation method");

		LAgencies 						= new ArrayList<Agency>(); // container for agencies
		LSponsors 						= new ArrayList<Sponsor>(); // container for sponsors
		worldID 						= String.valueOf(UUID.randomUUID()); // generates a unique ID for the world
		Date date 						= new Date();
		creationDate 					= new Timestamp(date.getTime()); // when is the world created
		this.numberOfIterations			= numberOfIterations;
		this.initialNumberOfSponsors 	= initialNumberOfSponsors;
		this.initialNumberOfAgencies 	= initialNumberOfAgencies;
		this.worldSize 					= worldSize.clone(); // sets internal worldsize
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
		this.moveRate					= moveRate;
		this.writeMethod				= writeMethod;
		this.budgetIncrease				= budgetIncrease;
		this.baseRisk					= baseRisk;
		this.allocationMethod			= allocationMethod;
		switch (writeMethod){
		case TO_DATABASE:
			writer = new WriterSQL();
			break;
		case TO_FILE:
			writer = new WriterFile();
			break;
		case NONE:
			writer = null;
		}
		if(settings == null)
			this.settings				= new Settings();
		else 
			this.settings 				= settings;
	}; // World

	@Override public String toString(){return "worldID:" + worldID ;}
	// Simple getters
	public String getWorldID() 				{return worldID;}
	public Timestamp getCreationDate() 		{return creationDate;}
	public int getInitialNumberOfSponsors() {return initialNumberOfSponsors;}
	public int getInitialNumberOfAgencies() {return initialNumberOfAgencies;}
	public CutDownModel getCutDownModel() 	{return cutDownModel;}
	public int[] getWorldSize() 			{return worldSize;}
	public double getSponsorSigmaFactor() 	{return sponsorSigmaFactor;}
	public double getSponsorMoney() 		{return sponsorMoney;}
	public double getAgencyMoney() 			{return agencyMoney;}
	public int getAgencyMoneyReserveFactor() {return agencyMoneyReserveFactor;}
	public double getAgencySigmaFactor() 	{return agencySigmaFactor;}
	public double getAgencyRequirementNeed() {return agencyRequirementNeed;}
	public double getAgencyRequirementSigma() {return agencyRequirementSigma;}
	public double getSightOfAgency() 		{return sightOfAgency;}
	public double getMoveRate() 			{return moveRate;}
	public int getNumberOfIterations() 		{return numberOfIterations;}
	public double getBudgetIncrease() 		{return budgetIncrease;}
	public double getBaseRisk() 			{return baseRisk;}
	public double getMean() 				{return mean;}
	public double getLcv() 					{return lcv;}
	public double getSkewness() 			{return skewness;}
	public double getKurtosis() 			{return kurtosis;}
	public double getL_lcv() 				{return L_lcv;}
	public double getL_skewness() 			{return L_skewness;}
	public double getL_kurtosis() 			{return L_kurtosis;}
	public Settings getSettings()			{return settings;}

	public void initialise(){	// Step 1
		//setstart();
		// Create Sponsors
		for (int i = 0; i<initialNumberOfSponsors;i++ ){
			LSponsors.add(new Sponsor(sponsorUtilities, worldID, creationDate,i,worldSize[0],worldSize[1],sponsorMoney,sponsorMoney/sponsorSigmaFactor));
		} // 
		// Create Agencies. Either the initial number is set, or the system calculates the number of agencies.
		if (initialNumberOfAgencies > 0){
			for(int i = 0; i<initialNumberOfAgencies;i++){
				Agency agency;
				agency = new Agency(agencyUtilities,worldID,creationDate,i,worldSize[0],worldSize[1], agencyMoney,agencyMoney/agencySigmaFactor,
						sightOfAgency, agencyMoneyReserveFactor, budgetIncrease, baseRisk);
				double budget = agency.getBudget();
				agency.setMoneyNeeded(agencyUtilities, budget * agencyRequirementNeed, budget * agencyRequirementSigma);
				LAgencies.add(agency);
				totalNumberOfAgencies += 1;
			}}
		else{
			totalNumberOfAgencies = generateNewAgencies();
		}

		//setend(); 
		//log (start, end, "initialise");
	}; // initialise

	private void setstart(){ start = System.currentTimeMillis();}
	private void setend(){ 	end = System.currentTimeMillis();}
	private void logging (int i, int step){
		if (log){
			if (i==1) 
				start1 = System.currentTimeMillis();
			if (i==2){ 
				end1 = System.currentTimeMillis();
				log(start1, end1, "Step:" + step);
			}
		}
	}
	private static double distance(Sponsor sponsor, Agency agency){ // calculate distance between sponsor and agency
		return Utilities.distance(agency.getPosition()[0],agency.getPosition()[1],sponsor.getPosition()[0],sponsor.getPosition()[1]); 
	}; // distance	

	public void seekPotentialSponsors(){ // Step 2
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
	} // seekPotentialSponsors

	public void allocateSponsor(){ // Step 3
		// setstart();
		for (int i=0;i<LAgencies.size();i++){
			Agency agency = LAgencies.get(i);
			Sponsor sponsor = Allocate.allocateAgencies(allocationMethod, agency);			
			if (!agency.noSponsors()){
				agency.getSponsor().removeAgency(agency); // clean up old sponsor
			}
			agency.setSponsor(sponsor);
			sponsor.addAgency(agency);

		}

		//setend();
		//log(start,end,"step3");
	} // allocateSponsor

	public void allocateFunding(){ // Step 4
		// TODO Consider over allocation
		resetCutDown();                           // initialises the agencies. 
		for (int i=0; i < LSponsors.size();i++){
			Sponsor sponsor = LSponsors.get(i);
			Payout.payout(cutDownModel, sponsor);
		}
	} // allocateFunding

	public void spendBudget(){ // Step 5
		for (int i=0;i<LAgencies.size();i++){
			Agency agency = LAgencies.get(i);
			agency.subtractsavings(agency.getMoneyNeeded() - agency.getPayout());
		}
	} // spendBudget

	public void storeStatisticData(){
		for (int i=0;i<LAgencies.size();i++){
			Agency agency = LAgencies.get(i);
			statisticList.add(agency.getSavingsdiff());
		}
	}

	public void removeExhaustedAgencies(){ // Step 6
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
	} // removeExhaustedAgencies

	public int generateNewAgencies(){ // Step 7
		double totalSponsorMoney = 0;
		double totalAgencyRequirement = 0; //  
		int i;
		for (i=0; i<LSponsors.size();i++){
			totalSponsorMoney += LSponsors.get(i).getMoney();
		}
		for (i=0;i<LAgencies.size();i++){
			totalAgencyRequirement += LAgencies.get(i).getBudget();
		}
		double avgBudget = 0;
		if (initialNumberOfAgencies == 0){
			avgBudget = this.agencyMoney;
		}
		else
			avgBudget = totalAgencyRequirement/LAgencies.size();
		int newAgencies = (int)((totalSponsorMoney - totalAgencyRequirement) / avgBudget); // int cast rounds automatically.
		newAgencies = (int)sponsorUtilities.gaussian(newAgencies, newAgencies * 0.02);
		for (i=1;i<newAgencies;i++){
			Agency agency;
			totalNumberOfAgencies += 1;
			agency = new Agency(agencyUtilities,worldID, creationDate,totalNumberOfAgencies,worldSize[0],worldSize[1], agencyMoney,agencyMoney/agencySigmaFactor,sightOfAgency, 
					agencyMoneyReserveFactor, budgetIncrease, baseRisk);
			agency.setMoneyNeeded(agencyUtilities, avgBudget, avgBudget * 0.02);
			LAgencies.add(agency);

		}
		return newAgencies;
	} // generateNewAgencies

	public void setBudgetRequirements(){ // Step 8
		/* New budgets are allocated.
		 * These are allocated with the algorithm: current_budget *gauss(MU,SIGMA) where MU = 1 and SIGMA = 0.02, hence the constants.
		 */
		final double MU = 1;
		final double SIGMA = 0.02;
		for (int i=0;i< LAgencies.size();i++){
			Agency agency = LAgencies.get(i);
			agency.setNewBudget(agencyUtilities, MU,SIGMA); // completely new budget or increase as per rate?
			agency.setMoneyNeeded(agencyUtilities, agency.getBudget() * agencyRequirementNeed, agency.getBudget() * agencyRequirementSigma);
		}
	}

	public void orchestrateWorld(){
		/* This method arranges all the steps and iterates.
		 */
		initialise();
		setstart();
		for (int i=1;i<=numberOfIterations;i++){
			actualIteration += 1;
			seekPotentialSponsors();
			allocateSponsor();
			allocateFunding();
			spendBudget();
			storeStatisticData();
			write(i);
			removeExhaustedAgencies();
			generateNewAgencies();
			setBudgetRequirements();
			move();
			cleanupActivities();
			notifyListeners();             // for the simple listener pattern. 
		}
		calculateStatistics();
		setend();
		log(start,end,"iteration:" + numberOfIterations);
	}

	public void cleanupActivities(){
		// Payoff is reset for all sponsors
		for (int j=0;j<LSponsors.size();j++){
			LSponsors.get(j).setPayoff(0);
		}
		for (int i=0;i<LAgencies.size();i++){
			Agency agency = LAgencies.get(i);
			agency.clearSavingsDifference();
		}
	}

	public void write(int iteration){
		// this routine calls the generic writer and asks it to write data to wherever the implementation puts the data.
		if (writer != null){
			if (iteration == 1) // only prepare for the first iteration
				writer.prepare(this);
			writer.writeData(this, LAgencies,LSponsors, iteration);
		}
		else
			for (int i=0;i<LAgencies.size();i++){
				//System.out.println(LAgencies.get(i).getPayout());
			}
	} // write

	public void addListener(publishProgress add){
		listeners.add(add);
	}


	public void notifyListeners() {
		for (publishProgress pl : listeners){
			pl.getProgress(actualIteration + ""); //  + "/" + numberOfIterations);
		}
	}

	public void move(){
		moveCloserToSponsor(LAgencies, LSponsors, moveRate);
	} // move
	//  private utility methods
	private void log(long start, long end, String s){ // for debugging purposes

		System.out.println("Execution time is " + formatter.format((end - start) /1000d) + " seconds - " + s);
	}// move



	private void resetCutDown(){
		for (int i=0;i<LAgencies.size();i++){
			LAgencies.get(i).setCutDown(false);
		}
	}

	private void moveCloserToSponsor(ArrayList<Agency> Agencies, ArrayList<Sponsor> Sponsors, double moveRate){
		int agencySize = Agencies.size();
		for (int i=0;i<agencySize;i++){
			Agency agency = Agencies.get(i);
			Sponsor sponsor = agency.getSponsor(); 
			if (sponsor != null){
				double dist = distance(sponsor,agency);
				if (moveRate > distance(sponsor, agency) && dist > 0){
					agency.setPosition(sponsor.getPosition()[0], sponsor.getPosition()[1]);
				}
				else{
					double[] agencyPos  = agency.getPosition();
					double[] sponsorPos = sponsor.getPosition();
					//sine and cosine is utilised for finding distances depending on how the imaginary triangle turns if agency and sponsor specifies 
					// the points opposing the catheti.
					// the formula is sinA = a/c and cosA = b/c
					// I did not manage to find a more trivial way to calculate this.
					// 
					int sw = 0; 
					if (Double.compare(agencyPos[0], sponsorPos[0]) > 0 && Double.compare(agencyPos[1], sponsorPos[1]) < 0 ){sw=1;} 
					if (Double.compare(agencyPos[0], sponsorPos[0]) > 0 && Double.compare(agencyPos[1], sponsorPos[1]) > 0 ){sw=2;}
					if (Double.compare(agencyPos[0], sponsorPos[0]) < 0 && Double.compare(agencyPos[1], sponsorPos[1]) < 0 ){sw=3;}
					if (Double.compare(agencyPos[0], sponsorPos[0]) < 0 && Double.compare(agencyPos[1], sponsorPos[1]) > 0 ){sw=4;}
					if (Double.compare(agencyPos[0], sponsorPos[0]) == 0 && Double.compare(agencyPos[1], sponsorPos[1]) != 0){sw=5;}
					if (Double.compare(agencyPos[1], sponsorPos[1]) == 0 && Double.compare(agencyPos[0], sponsorPos[0]) != 0){sw=6;}
					if (Double.compare(agencyPos[1], sponsorPos[1]) == 0 && Double.compare(agencyPos[0], sponsorPos[0]) == 0){sw=7;}
					double sinA;
					double angle;
					double xDelta = 0;
					double yDelta = 0;
					switch (sw){	
					case 1:     // Agency lower to the right of the sponsor
						sinA = (agencyPos[0] - sponsorPos[0])/dist;
						angle = Math.asin(sinA);
						xDelta = sinA * moveRate * -1;
						yDelta = Math.cos(angle) * moveRate;
						break;
					case 2: 	// Agency higher to the right of the sponsor
						sinA = (agencyPos[0] - sponsorPos[0])/dist;
						angle = Math.asin(sinA);
						xDelta = sinA * moveRate * -1;
						yDelta = Math.cos(angle) * moveRate * -1;
						break;
					case 3:		// Agency lower to the left of the sponsor
						sinA = (sponsorPos[1] - agencyPos[1])/dist;
						angle = Math.asin(sinA);
						xDelta = Math.cos(angle) * moveRate;
						yDelta = sinA * moveRate;
						break;
					case 4:		// Agency higher to the left of the sponsor
						sinA = (agencyPos[1] - sponsorPos[1])/dist;
						angle = Math.asin(sinA);
						xDelta = Math.cos(angle)* moveRate;
						yDelta = sinA * moveRate * -1;
						break;
					case 5: 	// Agency is on the same x-axis as the sponsor
						xDelta = agencyPos[0];
						yDelta = Double.compare(agencyPos[1],sponsorPos[1]) > 0 ? agencyPos[1] - moveRate : agencyPos[1] + moveRate;
						break;
					case 6:		// Agency is on the same y-axis as the sponsor
						xDelta = Double.compare(agencyPos[0],sponsorPos[0]) > 0 ? agencyPos[0] - moveRate : agencyPos[0] + moveRate;
						yDelta = agencyPos[1];
						break;
					}
					agency.setPosition(agencyPos[0] + xDelta, agencyPos[1] + yDelta);
				}	
			}
			else{ // move at random
				// pick a direction 
				double x = Math.random() - 0.5; 
				double y = Math.random() - 0.5; 
				x = agency.getPosition()[0] + (x/Math.abs(x))*moveRate; // to let random produce either negative or positive value.
				y = agency.getPosition()[1] + (y/Math.abs(y))*moveRate;
				if (Double.compare(x,worldSize[0]) > 0)  {x = (x - (x - worldSize[0])); } // if out of bounds, it bounces back into the world
				if (Double.compare(x,0) < 0)             {x = Math.abs(x);} // same if it bounces the other way out of bounds 
				if (Double.compare(y,worldSize[1]) > 0)  {y = (y - (y - worldSize[1])); } // if out of bounds, it bounces back into the world
				if (Double.compare(y,0) < 0)             {y = Math.abs(y);} // same if it bounces the other way out of bounds 
			}
		} // moveCloserToSponsor
	} // class Moving

	public void calculateStatistics(){
		Statistics statistics = new Statistics();
		statistics.setData(statisticList);
		statistics.calculate();
		mean 		= statistics.getMean();
		lcv			= statistics.getLcv();
		skewness	= statistics.getSkewness();
		kurtosis	= statistics.getKurtosis();
		L_lcv		= statistics.getLLcv();
		L_skewness	= statistics.getLSkewness();
		L_kurtosis	= statistics.getLKurtosis();
		log(1,1,"mean :" + mean);
		log(1,1,"lcv :" + lcv);
		log(1,1,"Skew :" + skewness);
		log(1,1,"Kurtosis :" + kurtosis);
		log(1,1,"lLcv :" + L_lcv);
		log(1,1,"LSkew :" + L_skewness);
		log(1,1,"LKurtosis :" + L_kurtosis);

	}

	/* 
	 * Following is a set of private classes which helps make different types of calculation. 
	 * If new enum values are created, this is the place to implement the corresponding code.
	 * 	
	 */
	static class Allocate { //private class for allocating agencies to sponsors based on allocation model

		private static Sponsor allocateAgencies(AllocationMethod am, Agency agency){
			Sponsor foundSponsor = null;
			switch (am){
			case CLOSEST_DISTANCE : 
				foundSponsor = closestDistance(agency);
				break;
			case LOWEST_PREVIOUS_CUT : 
				break;
			case LOYALTY_PROBABILITY : 
				break;
			case RANDOM_SPONSOR :
				foundSponsor = randomSponsor(agency);
				break;
			case SURVIVAL_MODE :
				foundSponsor = survivalMode(agency);
				break;
			}
			return foundSponsor;
		} // allocateAgencies

		private static Sponsor randomSponsor(Agency agency){
			// picks a completely random sponsor as long as it is within eyesight.
			Sponsor sponsor = null;
			int size = agency.getPossibleSponsors().size();
			if (size > 0) {
				int sponsorIndex = (int) (Math.random() * size); 
				sponsor = agency.getPossibleSponsors().get(sponsorIndex);
				agency.setSponsor(sponsor);
				sponsor.addAgency(agency);}
			else agency.setSponsor(null);
			return sponsor;
		} // randomSponsor

		private static Sponsor closestDistance(Agency agency){
			// picks the sponsor closest to the agency unconditionally.
			int sponsorIndex = Allocate.returnClosestSponsorIndex(agency); // look for close sponsor with enough money
			if (sponsorIndex < 0) // close sponsor with sufficient money is not found
				sponsorIndex = Allocate.returnClosestSponsorIndexHighestValue(agency); // find best value 
			if (sponsorIndex >= 0)	
				return agency.getPossibleSponsors().get(sponsorIndex);
			else
				return null;
		} // closestDistance
		
		private static Sponsor lowestPreviousCut(Agency agency){
			// TODO implement lowestpreviousCut
			return null;
		}
		
		private static Sponsor survivalMode(Agency agency){
			// TODO implement survivalmode
			return null;
		}
		
		

		private static int returnClosestSponsorIndex(Agency agency){ // finds the sponsor closest to the Agency
			int returnValue = -1;
			double minDistance = 99999;
			int size = agency.getPossibleSponsors().size();
			// TODO loyalty
			for (int i=0; i<size;i++){
				Sponsor sponsor = agency.getPossibleSponsors().get(i);
				double distance = distance(sponsor, agency);
				if (respectSponsorMoney) {
					if (agency.getBudget() + calculatePayoff(sponsor, agency) < sponsor.getMoney())
						if (distance < minDistance){
							minDistance = distance;
							returnValue = i;
						}
				}
				else{
					if (distance < minDistance){
						minDistance = distance;
						returnValue = i;
					}
					
				}
					
			}
			return returnValue;
		} // returnClosestSponsorIndex

		private static int returnClosestSponsorIndexHighestValue(Agency agency){
			int returnValue = -1;
			double minDistance = 99999;
			double bestValue    = -99999;
			int size = agency.getPossibleSponsors().size();
			for (int i=0; i<size;i++){
				Sponsor sponsor = agency.getPossibleSponsors().get(i);
				double distance = distance(sponsor, agency);
				double comparison = sponsor.getMoney() - agency.getBudget() - calculatePayoff(sponsor, agency); // calculate best money reserve  
				if ((bestValue < comparison) || ((Double.compare(bestValue,comparison) == 0) &&  (Double.compare(distance, minDistance)< 0))){ // a better or similar value has been found. Distance becomes a factor
					minDistance = distance; 
					bestValue = comparison;
					returnValue = i;
				}
			}
			return returnValue;
		}// returnClosestSponsorIndexHighestValue

		private static double calculatePayoff(Sponsor sponsor, Agency agency){
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
		} // calculatePayoff

	} // class Allocate

	private static class Payout { // private class for calculating payout based on payment model
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
					payoutBasedOnRisk(sponsor);
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
				double amount = rate * agency.getBudget(); 
				payout += amount;          // add budget to total amount
				agency.setPayout(amount);  // perform the payout
				agency.setCutDown(true);                      // an agency has been cut. This could affect, whether it chooses to move.
			}
			sponsor.setPayoff(payout); 				// finally set the payoff for the sponsor
		} // payoutPercentageRate

		private static void payoutBasedOnRisk(Sponsor sponsor){
			// finds a set of agencies, which will be cut percentage wise. This is done by initially finding the candidates 
			// at baserisk > random()
			// Afterwards all are investigated based upon baserisk compared against random
			// for all the cutdown agencies, the payout will be: 
			// payout = budget - (totalAgencyNeed*budget/totalcut
			double totalAgencyNeed = getTotalAgencyNeed(sponsor);
			double totalSponsorMoney = sponsor.getMoney();
			double difference = totalAgencyNeed - totalSponsorMoney;
			double startDifference = difference;
			double totalMoneyCut = 0;
			double payoff = 0;
			int size = sponsor.getAgencies().size();
			Agency agency;
			while ( Double.compare(startDifference,0) > 0){ // find the ones to be cut
				for(int i=0;i<size;i++){
					agency = sponsor.getAgencies().get(i);
					if (!agency.getCutDown()){ // only inspect agencies, that hasn't already been cut in this routine
						if (Double.compare(agency.getRisk(),Math.random()) > 0){
							startDifference -= agency.getBudget();
							totalMoneyCut += agency.getBudget();
							agency.setCutDown(true); // the agency has been selected and is now flagged for cut-down
							if (Double.compare(startDifference, 0) <= 0){ i = size;}  // terminate for loop if sufficient have been found

						}
					}
				}
			}
			for(int i=0; i< size;i++){ // then pay the cut agencies the calculated money, and pay the rest their budget
				agency = sponsor.getAgencies().get(i);
				if (!agency.getCutDown()){
					payoff += agency.getBudget();
					agency.setPayout(agency.getBudget());
				}
				else{
					double payAmount = agency.getBudget() - ((totalAgencyNeed - totalSponsorMoney) *agency.getBudget()/totalMoneyCut); // percentage cut
					payoff += payAmount;
					agency.setPayout(payAmount);
				}
			}
			sponsor.setPayoff(payoff); // finally set the amount the sponsor pays out

		} // payoutBasedOnRisk

		private static double calculatePercentage(Sponsor sponsor){
			double totalAmount = 0;
			double totalAgencyNeed = 0;
			double percentageOfPayout;
			totalAmount = sponsor.getMoney();
			totalAgencyNeed = getTotalAgencyNeed(sponsor);

			if (totalAgencyNeed == 0){  // no reason to divide by 0, if there is no money need. Just an insurance, as this routine is not supposed to be invoked if there is no agencyNeed
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

	private static double getTotalAgencyNeed(Sponsor sponsor){
		double totalAgencyNeed = 0;
		for (int i = 0;i<sponsor.getAgencies().size();i++){
			Agency agency = sponsor.getAgencies().get(i);
			totalAgencyNeed += agency.getBudget();
		}
		return totalAgencyNeed;
	} // getTotalAgencyNeed




} // Class World
