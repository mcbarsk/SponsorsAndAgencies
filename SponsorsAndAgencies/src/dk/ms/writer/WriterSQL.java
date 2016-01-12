package dk.ms.writer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dk.ms.SponsorsAndAgencies.Agency;
import dk.ms.SponsorsAndAgencies.Sponsor;
import dk.ms.SponsorsAndAgencies.World;
import dk.ms.writer.SponsorsAndAgenciesWriter;

/**
 * @author Mark
 *
 * This class connects to a MySQL database and handles all database access.
 * Similar classes can be created if other databases are used, or maybe just writing to a file.
 * Autocommit is switched off solely for performance reasons.
 * When running the prepare method, it will open a connection to the database and write a single record into the world table.
 * The connection will remain open, as it is required for 
 * Notice it executes in a batch manner to the MySQL database.
 */
public class WriterSQL extends SponsorsAndAgenciesWriter{
	private final String AGENCY_INSERT = "INSERT INTO sponsors_agencies.agencies (worldID, creationDate, name," +  
			"chosenSponsor,budget,moneyNeeded," + 
			"savings,risk,position_x,position_y," + 
			"payout,eyesight,cutdown,iteration) " +  
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private final String SPONSOR_INSERT = "INSERT INTO sponsors_agencies.sponsors (worldID, creationDate, name," + 
			"position_x,position_y,money,payoff," + 
			"iteration)" + 
			"VALUES(?,?,?,?,?,?,?,?)";
	private final String WORLD_INSERT = "INSERT INTO sponsors_agencies.worlds(worldID,creationDate,initialNumberOfSponsors,"+
			"initialNumberOfAgencies,moveSetting,cutDownModel,worldSize,sponsorSigmaFactor,sponsorMoney,agencyMoney," +
			"agencyMoneyReserveFactor,agencySigmaFactor,agencyRequirementNeed,agencyRequirementSigma,sightOfAgency," + 
			"moveRate,pickRandomSponsor,numberOfIterations)" +
			"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private String ConnURL; 
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;


	public void setup(){
	}

	@Override
	public void writeData(World world, ArrayList<Agency> lagency, ArrayList<Sponsor> lsponsor, int iteration){
		try{
			connect(world);
			writeAgency(lagency, iteration);
			writeSponsor(lsponsor, iteration);
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
				ConnURL = world.getConnectionUrl();

				Class.forName(world.getdbConnector()).newInstance();
				String connectionUser = world.getuser();
				String connectionPassword = world.getpw(); //"?Hard2type!";
				conn = DriverManager.getConnection(ConnURL, connectionUser, connectionPassword);
				conn.setAutoCommit(false);
			}
		}catch(Exception e){e.printStackTrace();}


	} // connect

	@Override
	public void prepare(World world){
		connect(world);
		try{ 
			stmt = conn.prepareStatement(WORLD_INSERT);
			stmt.setString(1, world.getWorldID());
			stmt.setString(2, world.getCreationDate().toString());
			stmt.setInt(3, world.getInitialNumberOfSponsors());
			stmt.setInt(4, world.getInitialNumberOfAgencies());
			stmt.setString(5, world.getMoveSetting().name());
			stmt.setString(6, world.getCutDownModel().name());
			stmt.setString(7, world.getWorldSize()[0] + "," + world.getWorldSize()[1]);
			stmt.setDouble(8, world.getSponsorSigmaFactor());
			stmt.setDouble(9, world.getSponsorMoney());
			stmt.setDouble(10, world.getAgencyMoney());
			stmt.setInt(11, world.getAgencyMoneyReserveFactor());
			stmt.setDouble(12, world.getAgencySigmaFactor());
			stmt.setDouble(13, world.getAgencyRequirementNeed());
			stmt.setDouble(14, world.getAgencyRequirementSigma());
			stmt.setDouble(15, world.getSightOfAgency());
			stmt.setDouble(16, world.getMoveRate());
			stmt.setInt(17, world.isPickRandomSponsor() ? 1:0);
			stmt.setInt(18, world.getNumberOfIterations());
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}};
		}

	}
	public void writeSponsor(ArrayList<Sponsor> lsponsor, int iteration){
		Sponsor sponsor;
		//stmt = conn.prepareStatement(InsertQRY, Statement.RETURN_GENERATED_KEYS); // used if keys are interesting
		try {
			stmt = conn.prepareStatement(SPONSOR_INSERT); // 
			for (int i=0;i<lsponsor.size();i++){
				sponsor = lsponsor.get(i);
				stmt.setString(1,sponsor.getWorldID().toString());
				stmt.setTimestamp(2, sponsor.getCreationDate());
				stmt.setInt   (3,sponsor.getName());
				stmt.setDouble(4, sponsor.getPosition()[0]);
				stmt.setDouble(5, sponsor.getPosition()[1]);
				stmt.setDouble(6, sponsor.getMoney());
				stmt.setDouble(7, sponsor.getPayoff());
				stmt.setInt(8, iteration);
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
	}
	public void writeAgency(ArrayList<Agency> lagency, int iteration){
		Agency agency;
		String InsertQRY = AGENCY_INSERT;
		try {
			stmt = conn.prepareStatement(InsertQRY, Statement.RETURN_GENERATED_KEYS); // returns id of new record
			for (int i=0;i<lagency.size();i++){
				agency = lagency.get(i);
				stmt.setString(1,agency.getWorldID().toString());

				int sponsorname = -1;
				if (agency.getSponsor() == null)
					sponsorname = -1;
				else
					sponsorname = agency.getSponsor().getName();
				int cutDown = agency.getCutDown() ?  1 : 0;
				stmt.setTimestamp(2, agency.getCreationDate());
				stmt.setInt   (3,agency.getName());
				stmt.setInt(4, sponsorname );	
				stmt.setDouble(5, agency.getBudget());
				stmt.setDouble(6, agency.getMoneyNeeded());
				stmt.setDouble(7, agency.getSavings());
				stmt.setDouble(8, agency.getRisk());
				stmt.setDouble(9, agency.getPosition()[0]);
				stmt.setDouble(10, agency.getPosition()[1]);
				stmt.setDouble(11, agency.getPayout());
				stmt.setDouble(12, agency.getEyesight());
				stmt.setInt(13, cutDown);
				stmt.setInt   (14,iteration);
				//stmt.setTimestamp(12, beggar.getWorldID());
				stmt.addBatch();
			}
			stmt.executeBatch();


		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally {
			try{
				if (stmt != null)
					stmt.close();
			} catch(SQLException sqlex){};
		}

	} // writeAgency

}// Class WriterSQL