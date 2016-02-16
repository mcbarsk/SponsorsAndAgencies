package dk.ms.SponsorsAndAgenciesControl;

public enum MoveMethod {
	PERCENTAGE_OF_DISTANCE("%"), 
	REAL_DISTANCE("abs"), 
	;
	
	private String name;

	private MoveMethod(String s){
		name = s;
	}
	public static MoveMethod convert (String string) {
		for (MoveMethod cm : MoveMethod.values())
			if( cm.toString().equals(string) ) {
				return cm;
			}
		return null;
	} // convert
 @Override public String toString(){ // to return the shortname for the value list in the GUI
	 return name;
	
}
} // enum AllocationMethod

