package dk.ms.SponsorsAndAgencies;
import java.util.*;

public class Utilities {
	private Random rnd;
	public Utilities(){
		rnd = new Random();
	} // Constructor
	
	public double gaussian( double mu, double sigma){
		return mu + sigma * rnd.nextGaussian(); 
	}
	
	public static double distance(double x1, double y1, double x2, double y2){
		return  Math.hypot((x1 - x2), (y1 - y2));
	}

}
