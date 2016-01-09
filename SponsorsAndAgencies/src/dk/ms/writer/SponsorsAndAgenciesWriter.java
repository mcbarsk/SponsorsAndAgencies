package dk.ms.writer;
import java.util.ArrayList;

import dk.ms.SponsorsAndAgencies.Agency;
import dk.ms.SponsorsAndAgencies.Sponsor;



public abstract class SponsorsAndAgenciesWriter {
		abstract public void setup() throws Exception;
		abstract public void writeData(ArrayList<Agency> agency, ArrayList<Sponsor> sponsor, int iteration);
		abstract public void connect(String a);
			
		
		// public void createSt(String a){};
		
		
			
//			finally {
//			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
//			try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
//			try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
//			}
}




