package dk.ms.SponsorsAndAgencies;
import java.sql.Timestamp;
import java.util.ArrayList;
public class Agency implements Cloneable{


	private String   	worldID; // which world does this agency belong to?
	private Timestamp   creationDate;
	private int 		name; // simple name of an agency
	private Sponsor 	chosenSponsor;
	private double 		budget; // the given budget of a beggar. 
	private double 		moneyNeeded; // How much does the beggar actually need?
	private double  	savings; // defines the savings of a given beggar
	private double  	risk = 0.25;
	private double[] 	position; // the agency position in the world
	private double 		payout;   // how much money did the agency actually get?
	private double 		eyesight; // How far can an agency see
	private ArrayList<Sponsor> possibleSponsors; 
	private boolean		cutDown = true;
	private double		savingsdifference = 0;
	private double		budgetIncrease = 1; // factor for budget increase per iteration

	public Agency(Utilities util, String worldID, Timestamp creationDate, int name, int width, int height, 
				  double mu, double sigma, double eyesight, double moneyReserveFactor,
				  double budgetIncrease){
		this.worldID 			= worldID;
		this.creationDate		= creationDate;
		this.name 				= name;
		this.chosenSponsor 		= null;
		this.position 			= new double[] {width*Math.random(), height*Math.random()};
		this.budget 			= util.gaussian(mu, sigma);
		this.savings 			= this.budget * moneyReserveFactor;
		this.possibleSponsors 	= new ArrayList<Sponsor>();
		this.eyesight 			= eyesight;
		this.budgetIncrease		= budgetIncrease;
	} // Constructor

	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	// Simple getters
	public Sponsor getSponsor()		{return chosenSponsor;}
	public double getMoneyLeft()	{return savings;}
	public ArrayList<Sponsor> getPossibleSponsors(){return possibleSponsors;}
	public double getPayout()		{return payout;}
	public double getSavings()		{return savings;}
	public double getRisk()			{return risk;}
	public double getMoneyNeeded() 	{return moneyNeeded;}
	public double getBudget() 		{return budget;}
	public double getEyesight()		{return eyesight;}
	public int getName()			{return name;}
	public boolean getCutDown()		{return cutDown;}
	public String getWorldID()		{return worldID;}
	public Timestamp getCreationDate(){return creationDate;}
	public boolean getBroke()		{return savings <= 0;}
	public double getSavingsdiff()	{return savingsdifference;}

	public final void addSponsor(Sponsor sponsor){
		possibleSponsors.add(sponsor);
	}

	public void clearPossibleSponsors(){
		this.possibleSponsors.clear();
	}

	public void setSavings(final double savings) {
		savingsdifference = -1 * ((this.savings - savings) / this.savings) * 100; // sets the percentage difference related to payout
		// the minus one is in order to have a positive increase if new savings > old savings.
		this.savings = savings;
	}


	public void setMoneyNeeded(Utilities util, double mu, double sigma) {
		double result = -1;
		while (result<0)
			result = util.gaussian(mu, sigma);
		moneyNeeded = result;
	}
	
	public void setNewBudget(Utilities util, double mu, double sigma){
		double result = -1;
		while (result<0)
			result = this.budget *util.gaussian(mu, sigma);
		this.budget = result;
	}

	public void clearSavingsDifference(){
		savingsdifference = 0;
	}

	public void setPayout(double payout){
		this.payout = payout;
	}

	public void setRisk(double risk){
		this.risk = risk;
	}

	public boolean noSponsors(){
		return chosenSponsor == null; 
	}
			

	public void setSponsor(Sponsor sponsor){
		this.chosenSponsor = sponsor;
	}

	public double[] getPosition(){return position;}

	public void setPosition(double width, double height){
		position[0] = width;
		position[1] = height;
	}

	public void setCutDown(boolean bool){
		cutDown = bool;
	}

	public void subtractsavings(double cutDown){
		this.setSavings(savings - cutDown);
	}

	public boolean hasMoneyLeft(){
		return savings > 0;
	}

	public void newBudget(){
		budget = budget * budgetIncrease;
	}


}// class Agency
