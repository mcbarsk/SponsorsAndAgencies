package dk.ms.SponsorsAndAgenciesControl;

public final class Settings {
		private final String connectionUrl = "jdbc:mysql://localhost:3306/sponsors_agencies"  + "?useSSL=false";
		private final String saveLocation = "C:\\Data\\";
		private final String dbConnector = "com.mysql.jdbc.Driver";
		private final String user = "root";
		//private String pw   = "1064"; //"?Hard2type!";
		private final String pw   = "?Hard2type!";
	

	/*
	 * Small getters to retrieve some world specific settings, embedded in the private class: Settings. 
	 */
	public final String getConnectionUrl(){return this.connectionUrl;	}
	public final String getPath(){return this.saveLocation;};
	public final String getdbConnector(){return this.dbConnector;}
	public final String getuser(){return this.user;}
	public final String getpw(){return this.pw;} 
	/* obviously no-one leaves a password hardcoded in the source. 
	 * Specify password in encrypted config file and read encrypted password into char array (as opposed to the current String)
	 * So basically read all config settings into user interface (assuming the data has been stored in a file. (and pw in a segregated encrypted file!) 
	 * (maybe use JPasswordField or PasswordField depending on whether JavaFX is chosen)  
	 *  Initialise the Setting class with all available config, including pw in char-array instead of current String.
	 */
} // Class Settings
