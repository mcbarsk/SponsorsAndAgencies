package dk.ms.writer;
import java.util.ArrayList;

import dk.ms.SponsorsAndAgencies.Agency;
import dk.ms.SponsorsAndAgencies.Sponsor;
import dk.ms.SponsorsAndAgencies.World;
import dk.ms.Statistics.Statistics;



public abstract class SponsorsAndAgenciesWriter {
	abstract public void writeData(World world, ArrayList<Agency> agency, ArrayList<Sponsor> sponsor, int iteration);
	abstract public void writeStatistics(World world, Statistics statistics);
	abstract public void prepare(World world);
}// class SponsorsAndAgenciesWriter




