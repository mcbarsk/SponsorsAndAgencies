package dk.ms.SponsorsAndAgenciesControl;

public enum AllocationMethod {
	RANDOM_SPONSOR, 
	CLOSEST_DISTANCE, 
	LOYALTY_PROBABILITY, 
	SURVIVAL_MODE;

	public static AllocationMethod convert (String string) {
		for (AllocationMethod cm : AllocationMethod.values())
			if( cm.toString().equals(string) ) {
				return cm;
			}
		return null;
	} // convert

} // enum AllocationMethod

