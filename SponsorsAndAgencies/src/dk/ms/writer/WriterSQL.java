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
import dk.ms.writer.SponsorsAndAgenciesWriter;

/**
 * @author Mark
 *
 * This class connects to a MySQL database and handles all database access.
 * Similar classes can be created if other databases are used, or maybe just writing to a file.
 * It commits directly to the database as the data is merely used for reporting purposes, and data consistency is not 
 * a prioritized topic in this case. 
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
	private static String CONNURL = "jdbc:mysql://localhost:3306/sponsors_agencies"  + "?useSSL=false"; 
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;


	public void setup(){
	}

	public void writeData(ArrayList<Agency> lagency, ArrayList<Sponsor> lsponsor, int iteration){
		try{
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




	public void prepare(){
		try {
			//		new com.mysql.jdbc.Driver();
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdatabase?user=testuser&password=testpassword");
			//String connectionUrl = "jdbc:mysql://localhost:3306/testdatabase";
			String connectionUser = "root";
			String connectionPassword = "1064"; //"?Hard2type!";
			conn = DriverManager.getConnection(CONNURL, connectionUser, connectionPassword);
			conn.setAutoCommit(false);


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void writeSponsor(ArrayList<Sponsor> lsponsor, int iteration){
		try {

			Sponsor sponsor;

			String InsertQRY = SPONSOR_INSERT;

			stmt = conn.prepareStatement(InsertQRY, Statement.RETURN_GENERATED_KEYS); // returns id of new record
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

			} catch(SQLException sqlex){};
		}	
	}
	public void writeAgency(ArrayList<Agency> lagency, int iteration){
		try {

			Agency agency;

			String InsertQRY = AGENCY_INSERT;

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


		catch (Exception e){e.printStackTrace();
		}
		finally {
			try{
				if (stmt != null)
					stmt.close();
			} catch(SQLException sqlex){};
		}

	} // writeAgency

}// Class WriterSQL