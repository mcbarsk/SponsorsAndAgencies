package dk.ms.SponsorsAndAgenciesControl;
// a very simple listener Interface to publish the progress of a given run
// similar listeners can be created, for instance a larger interim results object could be published and utilised by subscribing systems.
public interface publishProgress {
	public void getProgress(String str);
}
