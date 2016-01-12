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
		/* set up the filenames
		 * for header and data 
		 */
		
		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(world.getPath() + headerFile, true)))) {
			this.pw = writer;
		    pw.println("the text");
		}catch (IOException e) {
		    System.err.println(e);
		}
		
	}
	

}
