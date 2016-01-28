package dk.ms.SponsorsAndAgenciesModel.Reader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dk.ms.SponsorsAndAgenciesControl.Settings;
import dk.ms.SponsorsAndAgenciesControl.World;
import dk.ms.SponsorsAndAgenciesControl.WriteMethod;
import dk.ms.SponsorsAndAgenciesControl.CutDownModel;

public class Reader {
	private Connection conn = null;
	private String ConnURL; 
	private PreparedStatement stmt = null;
	private boolean connected = true;          // hope for the best
	private final String WORLDID_READ = "SELECT worldID FROM sponsors_agencies.worlds ORDER BY creationDate";
	private final String WORLD_READ = "SELECT worldID, " +
			"idworld," + 
			"worldID," +  
			"creationDate," + 
			"initialNumberOfSponsors," +
			"initialNumberOfAgencies,"+
			"cutDownModel," +
			"worldSize," +
			"sponsorSigmaFactor," +
			"sponsorMoney," +
			"agencyMoney," +
			"agencyMoneyReserveFactor," +
			"agencySigmaFactor," +
			"agencyRequirementNeed," +
			"agencyRequirementSigma," +
			"sightOfAgency," +
			"moveRate," +
			"pickRandomSponsor," +
			"numberOfIterations, " +
			"baseRisk " +
			"FROM sponsors_agencies.worlds WHERE worldID = ?";
	ResultSet rs = null;
	Settings settings;

	public Reader(Settings settings){
		this.settings = settings;
	}

	public ArrayList<String> worldIDs (Settings settings) throws SQLException{
		ArrayList<String> resultList = new ArrayList<String>();
		try{
			connect(settings);
		}
		catch(SQLException e){
			connected = false;
		}
		if (connected){
			try{ 
				stmt = conn.prepareStatement(WORLDID_READ);
				rs = stmt.executeQuery();	
				while (rs.next()){
					resultList.add(rs.getString("worldID"));
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try{
					if (stmt != null)
						stmt.close();

				} 
				catch(SQLException sqlex){sqlex.printStackTrace(); 
				try {
					conn.close();
				} 
				catch (SQLException e) {e.printStackTrace();}
				}
			}
		}
		else
			throw new SQLException("no retrieved world IDs");

		return resultList;

	} // worldIDs

	public World readWorld (Settings settings, String worldID) throws SQLException{
		World world = null;
		try{
			connect(settings);
		}
		catch(SQLException e){
			connected = false;
		}
		if (connected){
			try{ 
				stmt = conn.prepareStatement(WORLD_READ);
				stmt.setString    (1,worldID);
				rs = stmt.executeQuery();
				while (rs.next()){
					String[] wsS = new String[2];
					wsS = rs.getString("worldSize").split(",");
					int[] wsi = new int[2];
					wsi[0] = Integer.parseInt(wsS[0]);
					wsi[1] = Integer.parseInt(wsS[1]);
					boolean random = (rs.getInt("pickRandomSponsor") == 1 ? true :false);
					CutDownModel cm = CutDownModel.convert(rs.getString("cutDownModel"));
					world = new World(rs.getInt("numberofiterations"),
							rs.getInt("initialNumberOfSponsors"),
							rs.getInt("initialNumberOfAgencies"),
							cm,
							wsi,
							rs.getDouble("sponsorSigmaFactor"),
							rs.getDouble("sponsorMoney"),
							rs.getDouble("agencyMoney"),

							rs.getInt("agencyMoneyReserveFactor"),
							rs.getDouble("agencySigmaFactor"),
							rs.getDouble("agencyRequirementNeed"),
							rs.getDouble("agencyRequirementSigma"),
							rs.getDouble("sightOfAgency"),
							random,
							WriteMethod.NONE,
							rs.getDouble("moveRate"),
							1.02,
							rs.getDouble("baseRisk"),
							null);
				}



			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try{
					if (stmt != null)
						stmt.close();

				} 
				catch(SQLException sqlex){sqlex.printStackTrace(); 
				try {
					conn.close();
				} 
				catch (SQLException e) {e.printStackTrace();}
				}
			}
		}
		else
			throw new SQLException("no retrieved world IDs");

		return world;

	} // worldIDs

	private void connect(Settings settings) throws SQLException{
		try {
			if (conn ==null || conn.isClosed()){
				ConnURL = settings.getConnectionUrl();

				Class.forName(settings.getdbConnector()).newInstance();
				String connectionUser = settings.getuser();
				String connectionPassword = settings.getpw(); 
				conn = DriverManager.getConnection(ConnURL, connectionUser, connectionPassword);
				conn.setAutoCommit(false);
			}
		} //catch(Exception e){e.printStackTrace();}
		catch(ClassNotFoundException c){c.printStackTrace(); throw new SQLException("class not found");  }
		catch(IllegalAccessException c){c.printStackTrace(); throw new SQLException("illegal access");}
		catch(InstantiationException c){c.printStackTrace(); throw new SQLException("instantiation exception");}
	} // connect

}
