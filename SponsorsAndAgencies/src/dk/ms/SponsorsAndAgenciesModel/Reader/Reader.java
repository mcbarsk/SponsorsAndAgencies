package dk.ms.SponsorsAndAgenciesModel.Reader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dk.ms.SponsorsAndAgenciesControl.Settings;
import dk.ms.SponsorsAndAgenciesControl.World;
import dk.ms.SponsorsAndAgenciesControl.WorldIDType;
import dk.ms.SponsorsAndAgenciesControl.WriteMethod;
import dk.ms.SponsorsAndAgenciesControl.AllocationMethod;
import dk.ms.SponsorsAndAgenciesControl.CutDownModel;
import dk.ms.SponsorsAndAgenciesControl.MoveMethod;

public class Reader {
	private Connection conn = null;
	private String ConnURL; 
	private PreparedStatement stmt = null;
	private boolean connected = true;          // hope for the best
	private final String WORLDID_READ = "SELECT worldID, creationDate, worldname FROM sponsors_agencies.worlds ORDER BY creationDate";
	private final String WORLD_READ = 	"SELECT worldID, " +
												"idworld," + 
												"worldname," +
												"creationDate," + 
												"initialNumberOfSponsors," +
												"initialNumberOfAgencies,"+
												"cutDownModel," +
												"allocationMethod," + 
												"worldSize," +
												"sponsorSigmaFactor," +
												"sponsorMoney," +
												"respectSponsorMoney," + 
												"agencyMoney," +
												"agencyMoneyReserveFactor," +
												"agencySigmaFactor," +
												"agencyRequirementNeed," +
												"agencyRequirementSigma," +
												"sightOfAgency," +
												"moveRate," +
												"moveMethod," + 
												"numberOfIterations, " +
												"baseRisk, " +
												"b0," +
												"b1 " +
										"FROM sponsors_agencies.worlds WHERE worldID = ?";
	private final String IT_AVG_REPORT = "SELECT " + 
										          "a.iteration," + 
										          "COUNT(a.name) AS 'Number of agencies'," + 
										          "AVG(a.`budget`) AS 'Average budget'," + 
										          "AVG(a.`moneyNeeded`) AS 'Average money needed'," + 
										          "AVG(a.`savings`) AS 'Average savings'," + 
										          "AVG(a.`payout`) AS 'Average payout'," + 
										          "COUNT(IF(a.`cutdown` = 1, 1, NULL)) AS 'Number cut'," + 
										          "COUNT(IF(a.`cutdown` = 0, 0, NULL)) AS 'Number total payout'," +
										          "AVG(a.`percentageCut`) AS 'Average percentage cut'," + 
										          "MAX(a.name)," + 
										          " CASE WHEN a.iteration = 1 THEN 0 " +  
										          " ELSE MAX(a.name) - (SELECT "  +
										          								"MAX(b.name) " + 
										          								"FROM " + 
										          								"sponsors_agencies.agency_iterations b " + 
										          								"WHERE " + 
										          								"a.worldID = b.worldID " + 
										          								"AND b.iteration = a.iteration - 1) " + 
										          								"END AS 'New agencies' " + 
    									 "FROM sponsors_agencies.agency_iterations a " +  
    									 "WHERE a.worldID = ? " + 
    									 "GROUP BY a.iteration " ;
	private final String IT_SPONSOR_REPORT = "SELECT " + 
			 											"iteration," + 
			 											"avg(A.payoff) as 'Average payoff', " + 
			 											"min(A.payoff) as 'Min payoff', " +
			 											"max(A.payoff) as 'Max payoff', " +
			 											"sum(a.payoff) as 'Total payoff', " +  
			 											"sum(b.money) as 'Total money', " + 
			 											"COUNT(IF(b.money  - a.payoff  > 0.1, 1, NULL)) AS 'Number with reserve' " + 
			 								 "FROM sponsors_agencies.sponsor_iterations A INNER JOIN " +  
			 								 "sponsors_agencies.sponsors B ON A.worldID = B.worldID AND A.name = b.name " + 
			 								 "WHERE  A.worldID = ? " +
			 								 "GROUP BY  A.iteration ";
	ResultSet rs = null;
	Settings settings;

	public Reader(Settings settings){
		this.settings = settings;
	}

	public ArrayList<WorldIDType> worldIDs (Settings settings) throws SQLException{
		ArrayList<WorldIDType> resultList = new ArrayList<WorldIDType>();
		try{
			connect(settings);
		}
		catch(SQLException e){
			connected = false;
		}
		if (connected){
			try{ 
				stmt 	= conn.prepareStatement(WORLDID_READ);
				rs 		= stmt.executeQuery();	
				while (rs.next()){
					resultList.add(new WorldIDType (rs.getString("worldID"), rs.getString("creationDate"), rs.getString("worldname")));
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
					boolean rsm = rs.getInt("respectSponsorMoney") == 1 ? true : false;
					CutDownModel 		cm = CutDownModel.convert(rs.getString("cutDownModel"));
					AllocationMethod 	am = AllocationMethod.convert(rs.getString("allocationMethod"));
					MoveMethod          mm = MoveMethod.convert(rs.getString("moveMethod"));
					world = new World(rs.getString("worldname"),
							rs.getInt("numberofiterations"),
							rs.getInt("initialNumberOfSponsors"),
							rs.getInt("initialNumberOfAgencies"),
							cm,
							wsi,
							rs.getDouble("sponsorSigmaFactor"),
							rs.getDouble("sponsorMoney"),
							rsm,
							rs.getDouble("agencyMoney"),
							rs.getInt("agencyMoneyReserveFactor"),
							rs.getDouble("agencySigmaFactor"),
							rs.getDouble("agencyRequirementNeed"),
							rs.getDouble("agencyRequirementSigma"),
							rs.getDouble("sightOfAgency"),
							WriteMethod.NONE,
							am,
							rs.getDouble("moveRate"),
							mm,
							1.02,
							rs.getDouble("baseRisk"),
							rs.getDouble("b0"),
							rs.getDouble("b1"),
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

	} // readWorld
	
	public ArrayList<Object[]> readAVGReport (Settings settings, String worldID) throws SQLException{
		ArrayList<Object[]> returnList = null;
		try{
			connect(settings);
		}
		catch(SQLException e){
			connected = false;
		}
		if (connected){
			try{ 
				returnList = new ArrayList<Object[]>(); // the return list is created.
				stmt = conn.prepareStatement(IT_AVG_REPORT); // The big SQL is prepared.
				stmt.setString    (1,worldID); // for the WHERE clause
				rs = stmt.executeQuery();
				while (rs.next()){
					Object[] obj ={	rs.getInt("iteration"), 
									rs.getInt("Number of Agencies"), 
									rs.getDouble("Average budget"),
									rs.getDouble("Average money needed"),
									rs.getDouble("Average savings"),
									rs.getDouble("Average payout"),
									rs.getInt("Number cut"),
									rs.getInt("Number total payout"),
									rs.getDouble("Average percentage cut"),
									rs.getInt("New agencies")};
					returnList.add(obj);   // The array is added to the return
						
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
			throw new SQLException("no retrieved data");

		return returnList;

	} // readAVGReport
	
	public ArrayList<Object[]> readSponsorReport (Settings settings, String worldID) throws SQLException{
		ArrayList<Object[]> returnList = null;
		try{
			connect(settings);
		}
		catch(SQLException e){
			connected = false;
		}
		if (connected){
			try{ 
				returnList = new ArrayList<Object[]>(); // the return list is created.
				stmt = conn.prepareStatement(IT_SPONSOR_REPORT); // The big SQL is prepared.
				stmt.setString    (1,worldID); // for the WHERE clause
				rs = stmt.executeQuery();
				while (rs.next()){
					Object[] obj ={	rs.getInt("iteration"), 
									rs.getInt("Average payoff"), 
									rs.getDouble("Min payoff"),
									rs.getDouble("Max payoff"),
									rs.getDouble("Total payoff"),
									rs.getDouble("Total money"),
									rs.getInt("Number with reserve")};
					returnList.add(obj);   // The array is added to the return
						
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
			throw new SQLException("no retrieved data");

		return returnList;

	} // readSponsorReport
		

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
