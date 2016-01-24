package dk.ms.SponsorsAndAgenciesView;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import dk.ms.SponsorsAndAgencies.*;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.Component;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

public class WorldGUIMain {

	private JFrame frame;
	private JTextField Inp_iterations;
	private JTextField Inp_sponsors;
	private JTextField textField_1;
	private JTextField textField;
	private JTextField Inp_Agencies;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WorldGUIMain window = new WorldGUIMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WorldGUIMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 897, 527);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		frame.getContentPane().add(panel_1);
		
		JLabel lblWorldId = new JLabel("World ID");
		
		JComboBox combo_WorldID = new JComboBox();
		
		JLabel lblIterations = new JLabel("# Iterations");
		
		Inp_iterations = new JTextField();
		Inp_iterations.setColumns(6);
		
		JLabel lblSponsors = new JLabel("# Sponsors");
		
		Inp_sponsors = new JTextField();
		Inp_sponsors.setHorizontalAlignment(SwingConstants.LEFT);
		Inp_sponsors.setColumns(10);
		
		JButton btnNewButton = new JButton("New button");
		
		JLabel lblAgencies = new JLabel("# Agencies");
		
		Inp_Agencies = new JTextField();
		Inp_Agencies.setHorizontalAlignment(SwingConstants.LEFT);
		Inp_Agencies.setColumns(10);
		
		JLabel lblWorldSize = new JLabel("World size");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JLabel lblCutDownModel = new JLabel("Cut down");
		
		JComboBox combo_CutdownModel = new JComboBox();
		combo_CutdownModel.setAlignmentX(Component.LEFT_ALIGNMENT);
		combo_CutdownModel.setAlignmentY(Component.TOP_ALIGNMENT);
		combo_CutdownModel.setModel(new DefaultComboBoxModel<CutDownModel>(CutDownModel.values()));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblWorldId)
						.addComponent(lblIterations)
						.addComponent(lblSponsors)
						.addComponent(lblAgencies)
						.addComponent(lblWorldSize)
						.addComponent(lblCutDownModel))
					.addGap(27)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
							.addComponent(Inp_Agencies, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
							.addComponent(Inp_sponsors, 0, 0, Short.MAX_VALUE)
							.addComponent(Inp_iterations, GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
							.addGroup(gl_panel_1.createSequentialGroup()
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
								.addGap(7)
								.addComponent(textField_1, 0, 26, Short.MAX_VALUE)))
						.addComponent(combo_CutdownModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(combo_WorldID, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE))
					.addGap(816)
					.addComponent(btnNewButton)
					.addGap(689))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWorldId)
						.addComponent(combo_WorldID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(6)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIterations)
						.addComponent(Inp_iterations, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(Inp_sponsors, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSponsors))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAgencies)
						.addComponent(Inp_Agencies, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWorldSize)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblCutDownModel)
						.addComponent(combo_CutdownModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(212)
					.addComponent(btnNewButton)
					.addGap(96))
		);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JTextArea textArea = new JTextArea();
		textArea.setColumns(2);
		panel.add(textArea, BorderLayout.CENTER);
		
		JButton btnNewButton_1 = new JButton("New button");
		panel.add(btnNewButton_1, BorderLayout.NORTH);
	}
}
