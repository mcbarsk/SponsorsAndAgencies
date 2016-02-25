package dk.ms.SponsorsAndAgenciesModel.Reader;
import java.sql.SQLException;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import dk.ms.SponsorsAndAgenciesControl.Settings;
public class SponsorReport extends AbstractTableModel{
	private static final long serialVersionUID = -7146990124640509030L;
	private String[] header = {"iteration,","Average payoff", "Min payoff", "Max payoff", "Total payoff", "Total money", "# with reserve"};
	private List<Object[]> lines;
	
	public SponsorReport(Settings settings, String worldID){
		super();
		Reader reader = new Reader(settings);
		try {
			lines = reader.readSponsorReport(settings, worldID);
		}
		catch (SQLException sql){sql.printStackTrace();}
		
	} // AvgReport
	
	public String[] getHeader(){return header;}

	@Override
	public int getColumnCount() {
		return header.length;
	}

	@Override
	public int getRowCount() {
		return lines.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (row < 0 || row > lines.size() || col<0 || col > header.length - 1)
			return null;
		Object[] obj = lines.get(row);
		return obj[col];
	}
	
	public String getColumnName(int col){
		if (col > header.length -1)
			return "";
		return header[col];
	}
}

