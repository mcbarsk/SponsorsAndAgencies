package dk.ms.SponsorsAndAgenciesControl;
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
	private double		savingsdifference = 0; // difference in percentage of the savings, previous round vs this round. This is the basis for the statistics calculation.
	private double		budgetIncrease = 1; // factor for budget increase per iteration
	private int 		loyalty = 0; // amount of times the same sponsor has been allocated.
	private double 		distanceTraveled = 0; // used for picking agencies to be cut.
	private int			uncutCount = 0; // How many times did the agency get full payout based on budget?

	public Agency(Utilities util, String worldID, Timestamp creationDate, int name, int width, int height, 
			double mu, double sigma, double eyesight, double moneyReserveFactor,
			double budgetIncrease, double risk){
		this.worldID 			= worldID;
		this.creationDate		= creationDate;
		this.name 				= name;
		this.chosenSponsor 		= null;
		this.position 			= new double[] {width*Math.random(), height*Math.random()};
		double tmp = -1;
		while(tmp<0)
			tmp = util.gaussian(mu, sigma);
		this.budget 			= tmp;
		this.savings 			= this.budget * moneyReserveFactor;
		this.possibleSponsors 	= new ArrayList<Sponsor>();
		this.eyesight 			= eyesight;
		this.budgetIncrease		= budgetIncrease;
		this.risk				= risk;
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
	public int getLoyalty()			{return loyalty;}
	public double[] getPosition()	{return position;}
	public double getDistTraveled() {return distanceTraveled;}
	public int getUncutCount()		{return uncutCount;}


	public final void addSponsor(Sponsor sponsor){
		possibleSponsors.add(sponsor);
	}

	public void clearPossibleSponsors(){
		this.possibleSponsors.clear();
	}

	public void setSavings(final double savings) {
		savingsdifference = -1 * ((this.savings - savings) / this.savings) * 100; // sets the percentage difference related to payout
		// the minus one is in order to have a positive increase if new savings > old savings.
		if (savingsdifference < -100)
			savingsdifference = -100;
		this.savings = savings;
	}


	public void setMoneyNeeded(Utilities util, double mu, double sigma) {
		double result = -1;
		while (result<0)
			result = util.gaussian(mu, sigma);
		moneyNeeded = result;
	}

	public void setNewBudget(Utilities util, double mu, double sigma, int actualIteration){
		double result = -1;
		while (result<0)
			result = this.budget *util.gaussian(mu, sigma);
		this.budget = result * Math.pow(budgetIncrease, (double) actualIteration);
	}

	public void clearSavingsDifference(){
		savingsdifference = 0;
	}

	public void setPayout(double payout){
		this.payout = payout;
		if (Double.compare(payout, budget) == 0)
			uncutCount += 1;
		else
			uncutCount = 0;
	}

	public void setRisk(double risk){
		this.risk = risk;
	}

	public boolean noSponsors(){
		return chosenSponsor == null; 
	}


	public void setSponsor(Sponsor sponsor){
		if(sponsor != null && sponsor.equals(chosenSponsor)){
			loyalty += 1;
		}
		else{
			this.chosenSponsor = sponsor;
			loyalty = 1;
		}
	}

	public void setPosition(double width, double height){
		position[0] = width;
		position[1] = height;
	}

	public void setDistanceTraveled(double dist){
		distanceTraveled = dist;
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


}// class Agency
