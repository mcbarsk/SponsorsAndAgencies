package dk.ms.SponsorsAndAgenciesControl;

public enum CutDownModel {
	SAME_PERCENTAGE_RATE, 
	PROBABILITY_CALCULATION, 
	PROBABILITY_TIME_CALCULATION;

	
/**
* A common method for all enums since they can't have another base class
* @param <T> Enum type
* @param c enum type. All enums must be all caps.
* @param string case insensitive
* @return corresponding enum, or null
*/
public static CutDownModel convert (String string) {
	for (CutDownModel cm : CutDownModel.values())
    if( cm.toString().equals(string) ) {
         return cm;
     }
	    return null;
	}
}
