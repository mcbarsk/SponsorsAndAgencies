package dk.ms.writer;
import java.util.ArrayList;

import dk.ms.SponsorsAndAgencies.Agency;
import dk.ms.SponsorsAndAgencies.Sponsor;
import dk.ms.SponsorsAndAgencies.World;



public abstract class SponsorsAndAgenciesWriter {
	abstract public void writeData(ArrayList<Agency> agency, ArrayList<Sponsor> sponsor, int iteration);
	abstract public void prepare(World world);
}// class SponsorsAndAgenciesWriter




