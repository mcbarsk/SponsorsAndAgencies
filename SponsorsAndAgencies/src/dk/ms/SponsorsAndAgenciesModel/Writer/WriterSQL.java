package dk.ms.SponsorsAndAgenciesModel.Writer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
// import java.sql.ResultSet; used for retrieving data or generated keys
import java.sql.SQLException;
import java.util.ArrayList;

import dk.ms.SponsorsAndAgenciesControl.Agency;
import dk.ms.SponsorsAndAgenciesControl.Sponsor;
import dk.ms.SponsorsAndAgenciesControl.World;
import dk.ms.Statistics.Statistics;

/**
 * This class connects to a MySQL database and handles all database access.
 * Similar classes can be created if other databases are used, or maybe just writing to a file.
 * Autocommit is switched off solely for performance reasons.
 * When running the prepare method, it will open a connection to the database and write a single record into the world table.
 * The connection will remain open, as it is required for the subsequent writes of agencies and sponsors.  
 * Notice it executes in a batch manner to the MySQL database.
 * @author Mark Schacht 
 * 
 */
public class WriterSQL extends SponsorsAndAgenciesWriter{
	private final String AGENCY_INSERT = "INSERT INTO sponsors_agencies.agencies (worldID, creationDate, name," +  
			"risk,eyesight) " +  
			"VALUES (?,?,?,?,?)";
	private final String AGENCY_IT_INSERT = "INSERT INTO sponsors_agencies.agency_iterations (worldID, name," +  
			"chosenSponsor,budget,moneyNeeded," + 
			"savings,position_x,position_y," + 
			"payout,cutdown,iteration, percentageCut) " +  
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	private final String SPONSOR_INSERT = "INSERT INTO sponsors_agencies.sponsors (worldID, creationDate, name," + 
			"position_x,position_y,money)" +
			"VALUES(?,?,?,?,?,?)";
	private final String SPONSOR_IT_INSERT = "INSERT INTO sponsors_agencies.sponsor_iterations (worldID, name," + 
			"payoff,iteration)" + 
			"VALUES(?,?,?,?)";
	private final String WORLD_INSERT = "INSERT INTO sponsors_agencies.worlds(worldID,creationDate,initialNumberOfSponsors,"+
			"initialNumberOfAgencies,cutDownModel,worldSize,sponsorSigmaFactor,sponsorMoney,agencyMoney," +
			"agencyMoneyReserveFactor,agencySigmaFactor,agencyRequirementNeed,agencyRequirementSigma,sightOfAgency," + 
			"moveRate,pickRandomSponsor,numberOfIterations,baserisk)" +
			"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private String ConnURL; 
	private boolean sponsorWritten = false;
	private int lastAgencyWritten = -1;
	Connection conn = null;
	PreparedStatement stmt = null;
	PreparedStatement stmt2 = null;
	//ResultSet rs = null; Used for retrieving data or generated keys


	public void setup(){
	}

	@Override
	public void writeStatistics(World world, Statistics statistics){
		
	} // writeStatistics
	
	@Override
	public void writeData(World world, ArrayList<Agency> lagency, ArrayList<Sponsor> lsponsor, int iteration){
		try{
			connect(world);
			writeAgency(lagency, iteration);
			writeSponsorIterations(lsponsor, iteration);
			conn.commit();

		}
		catch(Exception e){}
		finally{
			if (conn != null)
				try{
					conn.close();

				}
			catch(SQLException sqlex){};
		}
	} // writeData

	private void connect(World world){
		try {
			if (conn ==null || conn.isClosed()){
				ConnURL = world.getSettings().getConnectionUrl();

				Class.forName(world.getSettings().getdbConnector()).newInstance();
				String connectionUser = world.getSettings().getuser();
				String connectionPassword = world.getSettings().getpw(); 
				conn = DriverManager.getConnection(ConnURL, connectionUser, connectionPassword);
				conn.setAutoCommit(false);
			}
		}catch(Exception e){e.printStackTrace();}
	} // connect
	
	public void closeConnection(){
		try {
			if (conn !=null && !conn.isClosed()){
				conn.close();
			}
		}catch(Exception e){e.printStackTrace();}
	} // closeConnection

	/** Opens up a connection against the database and writes a single world record.
	 *  It leaves the connection open, so either invoke writeData or use the public method to close the 
	 *  connection (for testing purposes.)
	 */
	@Override
	public void prepare(World world){
		connect(world);
		try{ 
			stmt = conn.prepareStatement(WORLD_INSERT);
			stmt.setString(1, world.getWorldID());
			stmt.setString(2, world.getCreationDate().toString());
			stmt.setInt(3, world.getInitialNumberOfSponsors());
			stmt.setInt(4, world.getInitialNumberOfAgencies());
			stmt.setString(5, world.getCutDownModel().name());
			stmt.setString(6, world.getWorldSize()[0] + "," + world.getWorldSize()[1]);
			stmt.setDouble(7, world.getSponsorSigmaFactor());
			stmt.setDouble(8, world.getSponsorMoney());
			stmt.setDouble(9, world.getAgencyMoney());
			stmt.setInt(10, world.getAgencyMoneyReserveFactor());
			stmt.setDouble(11, world.getAgencySigmaFactor());
			stmt.setDouble(12, world.getAgencyRequirementNeed());
			stmt.setDouble(13, world.getAgencyRequirementSigma());
			stmt.setDouble(14, world.getSightOfAgency());
			stmt.setDouble(15, world.getMoveRate());
			stmt.setInt(16, world.isPickRandomSponsor() ? 1:0);
			stmt.setInt(17, world.getNumberOfIterations());
			stmt.setDouble(18, world.getBaseRisk());
			stmt.execute();	
			conn.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try{
				if (stmt != null)
					stmt.close();

			} catch(SQLException sqlex){sqlex.printStackTrace(); try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}};
		}
	} // prepare
	
	private void writeSponsorIterations(ArrayList<Sponsor> lsponsor, int iteration){
		Sponsor sponsor;
		//stmt = conn.prepareStatement(InsertQRY, Statement.RETURN_GENERATED_KEYS); // used if keys are interesting
		try {
			 // 
			if (!sponsorWritten){ // to ensure sponsors are only written once.
				stmt = conn.prepareStatement(SPONSOR_INSERT);
				for (int i=0;i<lsponsor.size();i++){
					sponsor = lsponsor.get(i);
					stmt.setString(1,sponsor.getWorldID().toString());
					stmt.setTimestamp(2, sponsor.getCreationDate());
					stmt.setInt   (3,sponsor.getName());
					stmt.setDouble(4, sponsor.getPosition()[0]);
					stmt.setDouble(5, sponsor.getPosition()[1]);
					stmt.setDouble(6, sponsor.getMoney());
					stmt.addBatch();
				}
				sponsorWritten = true; 
				stmt.executeBatch();
			}
			stmt = conn.prepareStatement(SPONSOR_IT_INSERT);
				for (int i=0;i<lsponsor.size();i++){
					sponsor = lsponsor.get(i);
				stmt.setString(1,sponsor.getWorldID().toString());
				stmt.setInt   (2,sponsor.getName());
				stmt.setDouble(3, sponsor.getPayoff());
				stmt.setInt(4, iteration);
				stmt.addBatch();
			}
			stmt.executeBatch();
		}
		catch (Exception e){e.printStackTrace();
		}
		finally {
			try{
				if (stmt != null)
					stmt.close();
			} catch(SQLException sqlex){sqlex.printStackTrace();}
		}	
	} // writeSponsorIterations
	
	private void writeAgency(ArrayList<Agency> lagency, int iteration){
		Agency agency;
		try {stmt = conn.prepareStatement(AGENCY_INSERT); 
			 stmt2 = conn.prepareStatement(AGENCY_IT_INSERT);
			for (int i=0;i<lagency.size();i++){
				agency = lagency.get(i);
				if (agency.getName() > lastAgencyWritten){
					stmt.setString    (1,agency.getWorldID().toString());
					stmt.setTimestamp (2, agency.getCreationDate());
					stmt.setInt       (3,agency.getName());
					stmt.setDouble    (4, agency.getRisk());
					stmt.setDouble    (5, agency.getEyesight());
					lastAgencyWritten = agency.getName();
					stmt.addBatch();
				}		// The agencies were written. Now follows the iteration-specific data
				int sponsorname = -1;
				if (agency.getSponsor() == null)
					sponsorname = -1;
				else
					sponsorname = agency.getSponsor().getName();
				int cutDown = agency.getCutDown() ?  1 : 0;
				stmt2.setString (1,agency.getWorldID().toString());
				stmt2.setInt     (2,agency.getName());
				stmt2.setInt     (3, sponsorname );	
				stmt2.setDouble  (4, agency.getBudget());
				stmt2.setDouble  (5, agency.getMoneyNeeded());
				stmt2.setDouble  (6, agency.getSavings());
				stmt2.setDouble  (7, agency.getPosition()[0]);
				stmt2.setDouble  (8, agency.getPosition()[1]);
				stmt2.setDouble  (9, agency.getPayout());
				stmt2.setInt    (10, cutDown);
				stmt2.setInt    (11,iteration);
				stmt2.setDouble(12, agency.getSavingsdiff());
				//stmt.setTimestamp(12, beggar.getWorldID());
				stmt2.addBatch();
			}
			stmt.executeBatch();
			stmt2.executeBatch();


		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally {
			try{
				if (stmt != null)
					stmt.close();
				if(stmt2 != null)
					stmt2.close();
			} catch(SQLException sqlex){sqlex.printStackTrace();};
		}

	} // writeAgency

}// Class WriterSQL