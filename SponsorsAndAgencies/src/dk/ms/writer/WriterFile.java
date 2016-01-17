package dk.ms.writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import dk.ms.SponsorsAndAgencies.Agency;
import dk.ms.SponsorsAndAgencies.Sponsor;
import dk.ms.SponsorsAndAgencies.World;

public class WriterFile extends SponsorsAndAgenciesWriter{
	private String headerFile; // filename of header
	private String dataFile; // filename of actual data
	private final char DELIMITER = ';'; 
	private String header = "worldID" 					+ DELIMITER + 
			"creationDate" 				+ DELIMITER +
			"initialNumberOfSponsors" 	+ DELIMITER +
			"initialNumberOfAgencies" 	+ DELIMITER + 
			"moveSetting" 				+ DELIMITER +
			"cutDownModel" 				+ DELIMITER +
			"worldSize" 				+ DELIMITER +
			"sponsorSigmaFactor" 		+ DELIMITER +
			"sponsorMoney" 				+ DELIMITER +
			"agencyMoney" 				+ DELIMITER +
			"agencyMoneyReserveFactor" 	+ DELIMITER +
			"agencySigmaFactor" 		+ DELIMITER +
			"agencyRequirementNeed" 	+ DELIMITER +
			"agencyRequirementSigma" 	+ DELIMITER +
			"sightOfAgency" 			+ DELIMITER +
			"moveRate" 					+ DELIMITER +
			"pickRandomSponsor" 		+ DELIMITER +
			"numberOfIterations";
	private String headerLine;
	private String dataHeader = "worldID" 				+ DELIMITER +
			"iteration" 			+ DELIMITER + 
			"sponsorName" 			+ DELIMITER + 
			"sponsorPosX" 			+ DELIMITER + 
			"sponsorPosY" 			+ DELIMITER + 
			"agencyPosX"  			+ DELIMITER + 
			"agencyPosY"  			+ DELIMITER + 
			"sponsorMoney"			+ DELIMITER +
			"sponsorPayOff"			+ DELIMITER + 
			"agencyName"			+ DELIMITER + 
			"agencyBudget"			+ DELIMITER + 
			"agencyPayOut"			+ DELIMITER + 
			"moneyNeeded"			+ DELIMITER + 
			"savings"				+ DELIMITER + 
			"cutdown";

	private String dataLine;
	private PrintWriter pw;


	@Override
	public void writeData(World world, ArrayList<Agency> agency, ArrayList<Sponsor> sponsor, int iteration) {
		createDataLines(agency, sponsor, iteration);

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

		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(headerFile)))) {
			this.pw = writer;
			pw.println(header);
			createHeaderLine(world);
			pw.println(headerLine);
			pw.flush();

		}catch (IOException e) {
			System.err.println(e);
		}
		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, true)))) {
			this.pw = writer; // reuse writer for writing data
			pw.println(dataHeader);
		}catch (IOException e) {
			System.err.println(e);
		}

	}

	private void createHeaderLine(World world){
		headerLine = 
				world.getWorldID() 					+ DELIMITER + 
				world.getCreationDate() 			+ DELIMITER +
				world.getInitialNumberOfSponsors() 	+ DELIMITER +
				world.getInitialNumberOfAgencies() 	+ DELIMITER + 
				world.getCutDownModel().name() 		+ DELIMITER +
				world.getWorldSize()[0] + ',' + world.getWorldSize()[1] + DELIMITER +
				world.getSponsorSigmaFactor() 		+ DELIMITER +
				world.getSponsorMoney() 			+ DELIMITER +
				world.getAgencyMoney() 				+ DELIMITER +
				world.getAgencyMoneyReserveFactor() + DELIMITER +
				world.getAgencySigmaFactor() 		+ DELIMITER +
				world.getAgencyRequirementNeed() 	+ DELIMITER +
				world.getAgencyRequirementSigma() 	+ DELIMITER +
				world.getSightOfAgency() 			+ DELIMITER +
				world.getMoveRate() 				+ DELIMITER +
				(world.isPickRandomSponsor() ? "1" : "0") + DELIMITER +
				world.getNumberOfIterations();
	}

	private void createDataLines(ArrayList<Agency> lAgencies, ArrayList<Sponsor> lSponsors, int iteration){
		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, true)))) {
			this.pw = writer; // reuse writer for writing data
		
		for (int i=0; i<lAgencies.size();i++){
			Agency agency = lAgencies.get(i);
			Sponsor sponsor = agency.getSponsor();
			dataLine = 
					agency.getWorldID()		+ DELIMITER +
					iteration 				+ DELIMITER + 
					(sponsor != null ? sponsor.getName() : "") 			+ DELIMITER + 
					(sponsor != null ? sponsor.getPosition()[0] : "") 			+ DELIMITER +
					(sponsor != null ? sponsor.getPosition()[1] : "") 			+ DELIMITER +
					agency.getPosition()[0]  			+ DELIMITER +
					agency.getPosition()[1]  			+ DELIMITER +
					(sponsor != null ? sponsor.getMoney() : "")			+ DELIMITER +
					(sponsor != null ? sponsor.getPayoff() : "")			+ DELIMITER + 
					agency.getName() 					+ DELIMITER + 
					agency.getBudget() 					+ DELIMITER + 
					agency.getPayout()			+ DELIMITER + 
					agency.getMoneyNeeded()			+ DELIMITER + 
					agency.getSavings()				+ DELIMITER + 
					(agency.getCutDown() ? 1:0);
			pw.println(dataLine);
			
		}
		
		}catch (IOException e) {
			System.err.println(e);
		}
			
		
	}// createDataLines


}
