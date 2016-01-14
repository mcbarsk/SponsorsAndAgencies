package dk.ms.writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import dk.ms.SponsorsAndAgencies.Agency;
import dk.ms.SponsorsAndAgencies.Sponsor;
import dk.ms.SponsorsAndAgencies.World;
import dk.ms.writer.SponsorsAndAgenciesWriter;

public class WriterFile extends SponsorsAndAgenciesWriter{
	private String headerFile; // filename of header
	private String dataFile; // filename of actual data
	private final char DELIMITER = ';'; 
	private String header = "worldID" + DELIMITER + 
			                "creationDate" + DELIMITER +
			                "initialNumberOfSponsors" + DELIMITER +
			                "initialNumberOfAgencies" + DELIMITER + 
			                "moveSetting" + DELIMITER +
			                "cutDownModel" +  DELIMITER +
			                "worldSize" + DELIMITER +
			                "sponsorSigmaFactor" + DELIMITER +
			                "sponsorMoney" + DELIMITER +
			                "agencyMoney" +DELIMITER +
			                "agencyMoneyReserveFactor" + DELIMITER +
			                "agencySigmaFactor" + DELIMITER +
			                "agencyRequirementNeed" + DELIMITER +
			                "agencyRequirementSigma" + DELIMITER +
			                "sightOfAgency" + DELIMITER +
			                "moveRate" + DELIMITER +
			                "pickRandomSponsor" + DELIMITER +
			                "numberOfIterations";
	private String headerLine; 
	private PrintWriter pw;
	

	@Override
	public void writeData(World world, ArrayList<Agency> agency, ArrayList<Sponsor> sponsor, int iteration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare(World world) {
		/* writes a header file with the world id as name
		 * and opens up a file for creating data. 
		 */
		if (world == null)
			throw new NullPointerException("Missing world object");
		headerFile = world.getPath() + world.getWorldID() + " - Header" + ".csv";
		dataFile = world.getPath() + world.getWorldID() + " - Data" + ".csv";
			
		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(headerFile, true)))) {
			this.pw = writer;
		    pw.println(header);
		    createHeaderLine(world);
		    pw.println(headerLine);
		    
		}catch (IOException e) {
		    System.err.println(e);
		}
		
	}
	
	private void createHeaderLine(World world){
		headerLine = world.getWorldID() + DELIMITER + 
                world.getCreationDate() + DELIMITER +
                world.getInitialNumberOfSponsors() + DELIMITER +
                world.getInitialNumberOfAgencies() + DELIMITER + 
                world.getCutDownModel().name() +  DELIMITER +
                world.getWorldSize()[0] + ',' + world.getWorldSize()[1] + DELIMITER +
                world.getSponsorSigmaFactor() + DELIMITER +
                world.getSponsorMoney() + DELIMITER +
                world.getAgencyMoney() +DELIMITER +
                world.getAgencyMoneyReserveFactor() + DELIMITER +
                world.getAgencySigmaFactor() + DELIMITER +
                world.getAgencyRequirementNeed() + DELIMITER +
                world.getAgencyRequirementSigma() + DELIMITER +
                world.getSightOfAgency() + DELIMITER +
                world.getMoveRate() + DELIMITER +
                (world.isPickRandomSponsor() ? "1" : "0") + DELIMITER +
                world.getNumberOfIterations();
	}
	

}
