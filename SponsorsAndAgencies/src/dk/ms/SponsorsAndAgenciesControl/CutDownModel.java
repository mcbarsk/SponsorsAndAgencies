package dk.ms.SponsorsAndAgenciesControl;

public enum CutDownModel {
	SAME_PERCENTAGE_RATE, 
	PROBABILITY_CALCULATION, 
	PROBABILITY_TIME_CALCULATION;

	public static CutDownModel convert (String string) {
	for (CutDownModel cm : CutDownModel.values())
    if( cm.toString().equals(string) ) {
         return cm;
     }
	    return null;
	}
}
