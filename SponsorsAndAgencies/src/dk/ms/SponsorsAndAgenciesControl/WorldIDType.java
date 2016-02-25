package dk.ms.SponsorsAndAgenciesControl;

public class WorldIDType {
/* just a simple class to contain both creation date and timestamp
 * 
 */
	private String worldID;
	private String creationDate;
	private String name;
	public WorldIDType(String worldID, String creationDate, String name){
		this.worldID = worldID;
		this.creationDate = creationDate;
		this.name =name;
	}
	public String getWorldID () {return worldID;}
	public String getCreationDate() {return creationDate;}
	@Override
	public String toString(){
		return creationDate + "--" + name;
	}
}

