

package dk.ms.SponsorsAndAgenciesView;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Dimension;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.border.BevelBorder;

import dk.ms.SponsorsAndAgenciesControl.Settings;
import dk.ms.SponsorsAndAgenciesControl.WorldIDType;
import dk.ms.SponsorsAndAgenciesModel.Reader.SponsorReport;

import java.awt.Color;
import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
public class WorldGUISponsorReport  extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JPanel panel = new JPanel();
	private Settings settings;
	private JTable table;
	private SponsorReport spr;
	private boolean init = true;
	
	public WorldGUISponsorReport(ArrayList<WorldIDType> worldIDList, WorldIDType selectedWorldID) {
		setPreferredSize(new Dimension(900, 800));
		settings = new Settings();
		setLayout(new BorderLayout(0, 0));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.CYAN, null, null, null));
		add(panel, BorderLayout.NORTH);
		table = new JTable(spr);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(false);
		
		JLabel label1 = new JLabel("WorldID");
		JComboBox<WorldIDType> combo_WorldID = new JComboBox<WorldIDType>();
		combo_WorldID.setFont(new Font("Tahoma", Font.PLAIN, 10));
		for (int i = 0; i < worldIDList.size(); i++) {
			combo_WorldID.addItem(worldIDList.get(i));
		}
		
		if (init && selectedWorldID != null){ // use input ID, if it exists.
			spr = new SponsorReport(settings, selectedWorldID.getWorldID());
			table.setModel(spr);			// the table is populated
			init = false;
			combo_WorldID.setSelectedItem(selectedWorldID);
		}
				
		
		combo_WorldID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (init){
					init = false;
				}
				else{
					spr = new SponsorReport(settings, ((WorldIDType)combo_WorldID.getSelectedItem()).getWorldID());
					table.setModel(spr);
				}
			}
		});
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(label1)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(combo_WorldID, 0, 377, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(label1)
						.addComponent(combo_WorldID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		panel.setLayout(gl_panel);

		

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		//Add the scroll pane to this panel.
		add(scrollPane);
		//combo_WorldID.set
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	public void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("Average report");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		//Create and set up the content pane.
		//WorldGUIAvgReport newContentPane = new WorldGUIAvgReport();
		this.setOpaque(true);
		// newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(this);
		//frame.setContentPane(newContentPane);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}


}


