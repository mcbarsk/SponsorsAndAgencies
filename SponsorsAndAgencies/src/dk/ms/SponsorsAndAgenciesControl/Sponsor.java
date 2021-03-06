package dk.ms.SponsorsAndAgenciesControl;


import java.sql.Timestamp;
import java.util.ArrayList;


public class Sponsor implements Cloneable{
	// attributes	
	private String				worldID;
	private Timestamp 			creationDate;
	private int 				name;
	private double 				width;
	private double 				height;
	private double 				money;
	private ArrayList<Agency> 	LAgency;
	private double[] 			position;
	private double 				payoff;
	private double 				previousReserve; 

	// constructor	
	public Sponsor(Utilities util,String worldID,Timestamp creationDate, int name, int width, int height, double mu, double sigma){
		this.worldID		= worldID;
		this.creationDate	= creationDate;
		this.name   		= name;
		this.height 		= height;
		this.width  		= width;
		this.payoff 		= 0;
		this.position 		= new double[2];
		this.position[0] 	= Math.random() * width;
		this.position[1] 	= Math.random() * height;
		this.money 			= util.gaussian(mu, sigma); // based on initial money set up in master.
		this.previousReserve= this.money;
		
		LAgency 			= new ArrayList<Agency>();
	} // Constructor
	
	public Object clone(){
		try {
			return super.clone();
		}
		catch (Exception e){
			return null;
		}
	} // Clone
	
	// simple getters	
	public int getName() 				{return name;}
	public double getWidth() 			{return width;}
	public double getHeight() 			{return height;}
	public double[] getPosition() 		{return position;}
	public double getPayoff() 			{return payoff;}
	public double getMoney() 			{return money;}
	public Timestamp getCreationDate() 	{return creationDate;}
	public Double getPrevReserve()  	{return previousReserve;}
	
	// simple setters	
	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	public void setPosition(double[] position) {
		this.position = position;
	}

	public void setPayoff(double payoff) {
		this.payoff = payoff;
		previousReserve = money - this.payoff;
	}

	public void addAgency(Agency agency){
		LAgency.add(agency);
	}

	public void removeAgency(Agency agency){
		LAgency.remove(agency);
	}

	public ArrayList<Agency> getAgencies(){
		return LAgency;
	}

	public Object getWorldID() {
		return worldID;
	}
		
}
