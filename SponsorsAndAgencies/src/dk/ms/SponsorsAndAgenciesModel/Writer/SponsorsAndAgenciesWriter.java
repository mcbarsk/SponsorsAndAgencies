package dk.ms.SponsorsAndAgenciesModel.Writer;
import java.util.ArrayList;

import dk.ms.SponsorsAndAgenciesControl.Agency;
import dk.ms.SponsorsAndAgenciesControl.Sponsor;
import dk.ms.SponsorsAndAgenciesControl.World;
import dk.ms.Statistics.Statistics;



public abstract class SponsorsAndAgenciesWriter {
	abstract public void writeData(World world, ArrayList<Agency> agency, ArrayList<Sponsor> sponsor, int iteration);
	abstract public void writeStatistics(World world, Statistics statistics);
	abstract public void prepare(World world);
}// class SponsorsAndAgenciesWriter




