package dk.ms.SponsorsAndAgenciesControl;

public enum MoveMethod {
	PERCENTAGE_OF_DISTANCE, 
	REAL_DISTANCE, 
	;

	public static MoveMethod convert (String string) {
		for (MoveMethod cm : MoveMethod.values())
			if( cm.toString().equals(string) ) {
				return cm;
			}
		return null;
	} // convert

} // enum AllocationMethod

