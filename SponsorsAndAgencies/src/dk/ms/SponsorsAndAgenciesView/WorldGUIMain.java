package dk.ms.SponsorsAndAgenciesView;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import dk.ms.SponsorsAndAgenciesControl.*;
import dk.ms.SponsorsAndAgenciesModel.Reader.Reader;

import java.awt.Color;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.SystemColor;
import javax.swing.UIManager;

public class WorldGUIMain {

	private JFrame frame;
	private static JTextField Inp_iterations;
	private static JTextField Inp_sponsors;
	private JTextField Inp_world_Y;
	private JTextField Inp_world_X;
	private JTextField Inp_Agencies;
	private JTextField Inp_sponsor_money;
	private JTextField Inp_sponsor_Sigma;
	private JTextField Inp_agencyMoney;
	private JTextField Inp_agencySigma;
	private JTextField Inp_reserveFactor;
	private JTextField Inp_requirementNeed;
	private JTextField Inp_requirementSigma;
	private JTextField Inp_eyesight;
	private JTextField Inp_moveRate;
	private JTextField Inp_budgetIncrease;
	private JProgressBar progressBar;
	private JComboBox<CutDownModel> combo_CutdownModel;
	private JComboBox<WriteMethod> combo_writeMethod;
	private JComboBox<String> combo_WorldID;

	private JButton btnRunSimulation;
	private Settings settings;
	JTextArea textArea;
	ArrayList<String> worldIDList;
	private JTextField Inp_baseRisk;
	private boolean combo_worldIDActionAllowed = false;

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
		settings = new Settings();
		frame = new JFrame();
		frame.setBounds(100, 100, 1017, 775);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));

		JPanel panel_1 = new JPanel();
		panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_1.setBackground(UIManager.getColor("ToolBar.background"));
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		frame.getContentPane().add(panel_1);

		JLabel lblWorldId = new JLabel("World ID");

		combo_WorldID = new JComboBox<String>();
		combo_WorldID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (combo_worldIDActionAllowed)
					readWorld(combo_WorldID.getSelectedItem().toString());
			}
		});
		combo_WorldID.setBackground(new Color(250, 250, 210));
		combo_WorldID.setToolTipText("Select a world ID to see the settings for a previous run. This function is only relevant if a connection to a database has been established.");

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(UIManager.getColor("ToolBar.background"));
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.CYAN, null));

		JPanel panel_3 = new JPanel();
		panel_3.setForeground(SystemColor.inactiveCaptionBorder);
		panel_3.setBackground(UIManager.getColor("ToolBar.background"));
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.CYAN, null));

		progressBar = new JProgressBar();
		progressBar.setMaximum(100);

		JLabel lblProgress = new JLabel("Progress");

		btnRunSimulation = new JButton("Run simulation");
		btnRunSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				worker wrk = new worker();
				try{
					wrk.execute();
					btnRunSimulation.setEnabled(false);} // doInBackground();}
			catch(Exception e ){}


				//progressBar.;

			}
		});

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(UIManager.getColor("ToolBar.background"));
		panel_4.setForeground(SystemColor.inactiveCaptionBorder);
		panel_4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.CYAN, null));

		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 329, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblProgress)
							.addContainerGap(547, Short.MAX_VALUE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblWorldId)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(combo_WorldID, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)
							.addGap(157))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(304, Short.MAX_VALUE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(btnRunSimulation)
							.addContainerGap(486, Short.MAX_VALUE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
							.addGap(150))))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWorldId)
						.addComponent(combo_WorldID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)
					.addGap(48)
					.addComponent(btnRunSimulation)
					.addGap(46)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(442)
					.addComponent(lblProgress)
					.addGap(2))
		);

		JLabel lblIterations = new JLabel("# Iterations");

		JLabel lblSponsors = new JLabel("# Sponsors");

		JLabel lblAgencies = new JLabel("# Agencies");

		Inp_iterations = new JTextField();
		Inp_iterations.setToolTipText("Specify the number of iterations for a given simulation.");
		Inp_iterations.setColumns(6);

		Inp_sponsors = new JTextField();
		Inp_sponsors.setToolTipText("Specify the fixed number of sponsors.");
		Inp_sponsors.setHorizontalAlignment(SwingConstants.LEFT);
		Inp_sponsors.setColumns(10);

		Inp_Agencies = new JTextField();
		Inp_Agencies.setToolTipText("<html><p width=\\\"200\\\"> Specify the initial number of agencies. <BR\\>\r\nThe number of agencies will change during the simulation as some agencies die out,<BR\\> whilst others are created.</p></html>");
		Inp_Agencies.setHorizontalAlignment(SwingConstants.LEFT);
		Inp_Agencies.setColumns(10);

		JLabel lblWorldSize = new JLabel("World size");

		Inp_world_X = new JTextField();
		Inp_world_X.setToolTipText("The X-axis of the world.");
		Inp_world_X.setColumns(10);

		Inp_world_Y = new JTextField();
		Inp_world_Y.setToolTipText("The Y-axis of the world.");
		Inp_world_Y.setColumns(10);
		
				combo_CutdownModel = new JComboBox<CutDownModel>();
				combo_CutdownModel.setBackground(new Color(250, 250, 210));
				combo_CutdownModel.setToolTipText("Specifies the cut down model for the simulation. \r\n");
				combo_CutdownModel.setAlignmentX(Component.LEFT_ALIGNMENT);
				combo_CutdownModel.setAlignmentY(Component.TOP_ALIGNMENT);
				combo_CutdownModel.setModel(new DefaultComboBoxModel<CutDownModel>(CutDownModel.values()));
		
				JLabel lblCutDownModel = new JLabel("Cut down");
		
				JLabel lblWriteMethod = new JLabel("Write method");
		
				combo_writeMethod = new JComboBox<WriteMethod>();
				combo_writeMethod.setBackground(new Color(250, 250, 210));
				combo_writeMethod.setAlignmentX(Component.LEFT_ALIGNMENT);
				combo_writeMethod.setAlignmentY(Component.TOP_ALIGNMENT);
				combo_writeMethod.setModel(new DefaultComboBoxModel<WriteMethod>(WriteMethod.values()));
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addComponent(lblIterations)
						.addComponent(lblSponsors)
						.addComponent(lblAgencies)
						.addComponent(lblWorldSize)
						.addComponent(lblCutDownModel)
						.addComponent(lblWriteMethod))
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(9)
							.addGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(Alignment.LEADING, gl_panel_4.createSequentialGroup()
									.addComponent(Inp_world_X, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(Inp_world_Y, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
								.addComponent(Inp_sponsors, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
								.addComponent(Inp_iterations, Alignment.LEADING)
								.addComponent(Inp_Agencies, Alignment.LEADING, 0, 0, Short.MAX_VALUE)))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
								.addComponent(combo_CutdownModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(combo_writeMethod, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGap(319))
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIterations)
						.addComponent(Inp_iterations, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(Inp_sponsors, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(12)
							.addComponent(lblSponsors)))
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(Inp_Agencies, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(12)
							.addComponent(lblAgencies)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWorldSize)
						.addComponent(Inp_world_X, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(Inp_world_Y, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCutDownModel)
						.addComponent(combo_CutdownModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWriteMethod)
						.addComponent(combo_writeMethod, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(18, Short.MAX_VALUE))
		);
		panel_4.setLayout(gl_panel_4);

		JLabel lblSponsorMoney = new JLabel("Sponsor money");

		Inp_sponsor_money = new JTextField();
		Inp_sponsor_money.setToolTipText("The MU value for the sponsor money.");
		Inp_sponsor_money.setColumns(10);

		Inp_sponsor_Sigma = new JTextField();
		Inp_sponsor_Sigma.setToolTipText("The Sigma value for sponsor money.");
		Inp_sponsor_Sigma.setColumns(10);

		JLabel lblSigma = new JLabel("Sigma");
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
				gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblSponsorMoney)
						.addGap(10)
						.addComponent(Inp_sponsor_money, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
						.addComponent(lblSigma)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(Inp_sponsor_Sigma, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
						.addGap(27))
				);
		gl_panel_3.setVerticalGroup(
				gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
								.addComponent(Inp_sponsor_money, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSponsorMoney)
								.addComponent(Inp_sponsor_Sigma, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSigma))
						.addContainerGap(31, Short.MAX_VALUE))
				);
		panel_3.setLayout(gl_panel_3);

		JLabel lblAgencyMoney = new JLabel("Agency money");

		Inp_agencyMoney = new JTextField();
		Inp_agencyMoney.setToolTipText("The MU value for agency money.");
		Inp_agencyMoney.setColumns(10);

		JLabel lblSigma_1 = new JLabel("Sigma");

		Inp_agencySigma = new JTextField();
		Inp_agencySigma.setToolTipText("The Sigma value for agency money.");
		Inp_agencySigma.setColumns(10);

		JLabel lblReserve = new JLabel("Reserve factor");

		Inp_reserveFactor = new JTextField();
		Inp_reserveFactor.setToolTipText("<html><p width=\\\"500\\\">The reserve factor, which calculates the agency's initial saving. <BR\\> For instance if the factor is 5, the agency savings would be 5 * agency budget.</p></html>");
		Inp_reserveFactor.setColumns(10);

		JLabel lblRequirementNeed = new JLabel("Requirement");

		Inp_requirementNeed = new JTextField();
		Inp_requirementNeed.setToolTipText("The MU value for the agency's real requirement.");
		Inp_requirementNeed.setColumns(10);

		JLabel lblReqyuirementSigma = new JLabel("Need");

		Inp_requirementSigma = new JTextField();
		Inp_requirementSigma.setToolTipText("The Sigma value for the agency's real requirement.");
		Inp_requirementSigma.setColumns(10);

		JLabel lblEyesight = new JLabel("Eyesight");

		Inp_eyesight = new JTextField();
		Inp_eyesight.setColumns(10);

		JLabel lblMoverate = new JLabel("Moverate");

		Inp_moveRate = new JTextField();
		Inp_moveRate.setColumns(10);

		JLabel lblNewLabel = new JLabel("Sigma");

		JLabel lblBudgetIncrease = new JLabel("Budget increase");

		Inp_budgetIncrease = new JTextField();
		Inp_budgetIncrease.setColumns(10);

		JLabel lblBaseRisk = new JLabel("Base risk");

		Inp_baseRisk = new JTextField();
		Inp_baseRisk.setColumns(10);
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panel_2.createSequentialGroup()
									.addComponent(lblBudgetIncrease)
									.addGap(12)
									.addComponent(Inp_budgetIncrease, 0, 0, Short.MAX_VALUE))
								.addGroup(gl_panel_2.createSequentialGroup()
									.addComponent(lblRequirementNeed)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblReqyuirementSigma)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(Inp_requirementNeed, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNewLabel))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
									.addComponent(lblSigma_1)
									.addComponent(lblAgencyMoney))
								.addComponent(lblReserve))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(Inp_agencyMoney, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(Inp_reserveFactor, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
									.addComponent(Inp_agencySigma, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)))
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_2.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
										.addComponent(lblEyesight)
										.addComponent(lblMoverate)))
								.addGroup(gl_panel_2.createSequentialGroup()
									.addGap(12)
									.addComponent(lblBaseRisk)))))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(Inp_requirementSigma, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addComponent(Inp_baseRisk, 0, 0, Short.MAX_VALUE)
						.addComponent(Inp_moveRate, 0, 0, Short.MAX_VALUE)
						.addComponent(Inp_eyesight, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
					.addContainerGap(45, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAgencyMoney)
						.addComponent(Inp_agencyMoney, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEyesight)
						.addComponent(Inp_eyesight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSigma_1)
						.addComponent(Inp_agencySigma, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMoverate)
						.addComponent(Inp_moveRate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
								.addComponent(Inp_reserveFactor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(Inp_baseRisk, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(18)
							.addComponent(lblReserve))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(18)
							.addComponent(lblBaseRisk)))
					.addGap(21)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRequirementNeed)
						.addComponent(lblReqyuirementSigma)
						.addComponent(Inp_requirementNeed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel)
						.addComponent(Inp_requirementSigma, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(Inp_budgetIncrease, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(18)
							.addComponent(lblBudgetIncrease)))
					.addContainerGap(7, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
		panel_1.setLayout(gl_panel_1);

		JPanel panel = new JPanel();
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		frame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton = new JButton("Clear");
		panel.add(btnNewButton, BorderLayout.SOUTH);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText(null);
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("New menu");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
		mnNewMenu.add(mntmNewMenuItem);
		initScreen();   // Sets up default values for the different text fields.
		readWorldIDs(); // populates the combo box for world IDs by reading data from the database (if connected.)
	} // initialize

	class worker extends SwingWorker<Integer, String>{

		int numberOfIterations			= 30;
		int initialNumberOfSponsors 	= 50;
		int initialNumberOfAgencies 	= 100;
		// CutDownModel cm 				= CutDownModel.SAME_PERCENTAGE_RATE;
		CutDownModel cm 				= CutDownModel.PROBABILITY_CALCULATION;
		AllocationMethod am				= AllocationMethod.CLOSEST_DISTANCE;
		int[] ws 						= {5,5};
		double sponsorSigmaFactor 		= 8; 
		double sponsorMoney 			= 50;
		double agencyMoney 				= 10 ;
		int agencyMoneyReserveFactor 	= 5;
		double agencySigmaFactor 		= 1;
		double agencyRequirementNeed	= 0.92;
		double agencyRequirementSigma	= 0.2;
		double sightOfAgency 			= 3;
		boolean pickRandomSponsor 		= false;
		double moveRate					= 0.5;
		double budgetIncrease			= 1.02;
		double baseRisk					= 0.25;
		int stat = 0;
		What listen;
		WriteMethod writeMethod = WriteMethod.NONE;
		// WriteMethod writeMethod = WriteMethod.TO_DATABASE;
		// WriteMethod writeMethod = WriteMethod.TO_FILE;

		final class What extends Thread implements publishProgress {


			public void getProgress(String msg){

				publish(msg);
			};
		} // Class What

		public worker(){
			numberOfIterations			= Integer.parseInt(Inp_iterations.getText());
			initialNumberOfSponsors 	= Integer.parseInt(Inp_sponsors.getText());
			initialNumberOfAgencies 	= Integer.parseInt(Inp_Agencies.getText());
			cm 							= (CutDownModel)combo_CutdownModel.getSelectedItem();
			writeMethod					= (WriteMethod)combo_writeMethod.getSelectedItem();
			ws[0] 						= Integer.parseInt(Inp_world_X.getText());
			ws[1] 						= Integer.parseInt(Inp_world_Y.getText());
			sponsorSigmaFactor 			= Double.parseDouble(Inp_sponsor_Sigma.getText()); 
			sponsorMoney 				= Double.parseDouble(Inp_sponsor_money.getText());
			agencyMoney 				= Double.parseDouble(Inp_agencyMoney.getText()) ;
			agencyMoneyReserveFactor 	= Integer.parseInt(Inp_reserveFactor.getText());
			agencySigmaFactor 			= Double.parseDouble(Inp_agencySigma.getText());
			agencyRequirementNeed		= Double.parseDouble(Inp_requirementNeed.getText());
			sightOfAgency 				= Double.parseDouble(Inp_eyesight.getText());
			boolean pickRandomSponsor 	= false;
			moveRate					= Double.parseDouble(Inp_moveRate.getText());
			budgetIncrease				= Double.parseDouble(Inp_budgetIncrease.getText());
			baseRisk					= Double.parseDouble(Inp_baseRisk.getText());
			//TODO add allocation method am							= 


			listen = new What();
			listen.run();
			progressBar.setValue(0);
			progressBar.setMaximum(numberOfIterations -1);

		} // constructor
		@Override
		protected void process(List<String> chunks) {
			// This operation runs whenever something has been published via the listener. Notice, as this is a threaded operation, the timing will cause some of these updates to be invisible for the user. 
			// So the progressbar will most likely not increment for each new published value, which is why this routine only looks at the last published item (chunks.size -1). 
			// If all items were important, it would be necessary to iterate over all items in the list named chunks. <-- the design decisions 
			String i = chunks.get(chunks.size()-1);
			progressBar.setValue(Integer.parseInt(i)); 	// note a progressbar is not a good idea if the simulation is allowed to invoke more than one simulation at a time. (This could become a future topic, if 
														// the need for several iterations arise. In that case, disable progressbar for an iterative run and report the single results in the done() operation instead,
														// for instance by spitting out results in the textArea.
		} // process
		@Override 
		protected void done(){
			// this is the final operation invoked, when the threaded execution completes. This is normally where all the results are gathered and exposed for the user.
			// for future enhancements, this could very well be the place to output some more thorough statistics regarding a given simulation. (how many agencies were created, how many were cut in average per iteration etc. etc.)
			btnRunSimulation.setEnabled(true);
			textArea.append("ID:" + world.getWorldID() + "|Created:" + world.getCreationDate() + "\n");
			textArea.append("Mean:" + "\t" + world.getMean() + "\n");
			textArea.append("lcv" + "\t" + world.getLcv() + "\n");
			textArea.append("skewness" + "\t" + world.getSkewness() + "\n");
			textArea.append("kurtosis" + "\t" + world.getKurtosis() + "\n");
			textArea.append("L_lcv" + "\t" + world.getL_lcv() + "\n");
			textArea.append("L_skewness" + "\t" + world.getL_skewness() + "\n");
			textArea.append("L_kurtosis" + "\t" + world.getL_kurtosis() + "\n");

			readWorldIDs();

		} // done
		World world;
		protected Integer doInBackground() throws Exception{
			world = new World(numberOfIterations, initialNumberOfSponsors,initialNumberOfAgencies, 
					cm, ws,sponsorSigmaFactor, sponsorMoney, agencyMoney,agencyMoneyReserveFactor,
					agencySigmaFactor,agencyRequirementNeed,
					agencyRequirementSigma,sightOfAgency, writeMethod, am,moveRate,
					budgetIncrease,baseRisk,settings);
			world.addListener(listen);
			world.orchestrateWorld();


			return 10;

		} // doInBackground
	} // Class worker

	private void initScreen(){
		Inp_iterations.setText("100");
		Inp_sponsors.setText("50");
		Inp_Agencies.setText("100");
		combo_CutdownModel.setSelectedIndex(0);
		combo_writeMethod.setSelectedIndex(0);
		Inp_world_X.setText("5");
		Inp_world_Y.setText("5");
		Inp_sponsor_Sigma.setText("6"); 
		Inp_sponsor_money.setText("50");
		Inp_agencyMoney.setText("10") ;
		Inp_reserveFactor.setText("5");
		Inp_agencySigma.setText("6");
		Inp_requirementNeed.setText("0.92");
		Inp_requirementSigma.setText("0.2");
		Inp_eyesight.setText("3");
		Inp_moveRate.setText("0.5");
		Inp_budgetIncrease.setText("1.02");
		Inp_baseRisk.setText("0.25");
	}
	private void readWorldIDs(){
		// connects to the database and reads what has been run so far. It catches any SQL exception, so it is invisible for the user if no connection is established. 
		combo_worldIDActionAllowed = false;
		Reader reader = new Reader(settings);
		combo_WorldID.removeAllItems();
		try{
			worldIDList = reader.worldIDs(settings);
			for (int i=0;i<worldIDList.size();i++){
				combo_WorldID.addItem(worldIDList.get(i));
			}
		}
		catch(SQLException e){e.printStackTrace();}
		combo_worldIDActionAllowed = true;
	}
	private void readWorld(String worldID){
		Reader reader = new Reader(settings);
		try{
			World world = reader.readWorld(settings, worldID);
			Inp_iterations.setText(world.getNumberOfIterations() +"");
			Inp_sponsors.setText(world.getInitialNumberOfSponsors() +"");
			Inp_Agencies.setText(world.getInitialNumberOfAgencies() +"");
			combo_CutdownModel.setSelectedItem(world.getCutDownModel());
			combo_writeMethod.setSelectedItem(WriteMethod.NONE);
			Inp_world_X.setText(world.getWorldSize()[0] +"" );
			Inp_world_Y.setText(world.getWorldSize()[1] +"");
			Inp_sponsor_Sigma.setText(world.getSponsorSigmaFactor() +""); 
			Inp_sponsor_money.setText(world.getSponsorMoney() + "");
			Inp_agencyMoney.setText(world.getAgencyMoney() +"") ;
			Inp_reserveFactor.setText(world.getAgencyMoneyReserveFactor()+"");
			Inp_agencySigma.setText(world.getAgencySigmaFactor()+"");
			Inp_requirementNeed.setText(world.getAgencyRequirementNeed()+"");
			Inp_requirementSigma.setText(world.getAgencyRequirementSigma()+"");
			Inp_eyesight.setText(world.getSightOfAgency()+"");
			Inp_moveRate.setText(world.getMoveRate()+"");
			Inp_budgetIncrease.setText(world.getBudgetIncrease()+"");
			Inp_baseRisk.setText(world.getBaseRisk()+"");
		}
		catch(SQLException e){e.printStackTrace();}
	} // readWorld
}
