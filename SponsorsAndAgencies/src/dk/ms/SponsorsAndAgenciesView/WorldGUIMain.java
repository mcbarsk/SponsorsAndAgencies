package dk.ms.SponsorsAndAgenciesView;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

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
		frame.setBounds(100, 100, 966, 618);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(211, 211, 211));
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

		JLabel lblIterations = new JLabel("# Iterations");

		Inp_iterations = new JTextField();
		Inp_iterations.setToolTipText("Specify the number of iterations for a given simulation.");
		Inp_iterations.setColumns(6);

		JLabel lblSponsors = new JLabel("# Sponsors");

		Inp_sponsors = new JTextField();
		Inp_sponsors.setToolTipText("Specify the fixed number of sponsors.");
		Inp_sponsors.setHorizontalAlignment(SwingConstants.LEFT);
		Inp_sponsors.setColumns(10);

		JLabel lblAgencies = new JLabel("# Agencies");

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

		JLabel lblCutDownModel = new JLabel("Cut down");

		combo_CutdownModel = new JComboBox<CutDownModel>();
		combo_CutdownModel.setBackground(new Color(250, 250, 210));
		combo_CutdownModel.setToolTipText("Specifies the cut down model for the simulation. \r\n");
		combo_CutdownModel.setAlignmentX(Component.LEFT_ALIGNMENT);
		combo_CutdownModel.setAlignmentY(Component.TOP_ALIGNMENT);
		combo_CutdownModel.setModel(new DefaultComboBoxModel<CutDownModel>(CutDownModel.values()));

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(211, 211, 211));
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(211, 211, 211));
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JLabel lblWriteMethod = new JLabel("Write method");

		combo_writeMethod = new JComboBox<WriteMethod>();
		combo_writeMethod.setBackground(new Color(250, 250, 210));
		combo_writeMethod.setAlignmentX(Component.LEFT_ALIGNMENT);
		combo_writeMethod.setAlignmentY(Component.TOP_ALIGNMENT);
		combo_writeMethod.setModel(new DefaultComboBoxModel<WriteMethod>(WriteMethod.values()));

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
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
				gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
										.addContainerGap()
										.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
												.addComponent(lblWorldId)
												.addComponent(lblIterations)
												.addComponent(lblSponsors)
												.addComponent(lblAgencies)
												.addComponent(lblWorldSize)
												.addComponent(lblCutDownModel)
												.addComponent(lblWriteMethod))
										.addGap(18)
										.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
												.addComponent(combo_writeMethod, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(combo_CutdownModel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
								.addGroup(gl_panel_1.createSequentialGroup()
										.addGap(95)
										.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
														.addComponent(Inp_Agencies, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
														.addComponent(Inp_sponsors, 0, 0, Short.MAX_VALUE)
														.addComponent(Inp_iterations, GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
														.addGroup(gl_panel_1.createSequentialGroup()
																.addComponent(Inp_world_X, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
																.addGap(7)
																.addComponent(Inp_world_Y, 0, 26, Short.MAX_VALUE)))
												.addComponent(combo_WorldID, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_panel_1.createSequentialGroup()
										.addGap(10)
										.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup()
										.addGap(10)
										.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(17, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblProgress)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(104))
				.addGroup(gl_panel_1.createSequentialGroup()
						.addContainerGap()
						.addComponent(btnRunSimulation)
						.addContainerGap(339, Short.MAX_VALUE))
				);
		gl_panel_1.setVerticalGroup(
				gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblWorldId)
								.addComponent(combo_WorldID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
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
								.addComponent(Inp_world_X, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(Inp_world_Y, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblCutDownModel)
								.addComponent(combo_CutdownModel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblWriteMethod)
								.addComponent(combo_writeMethod, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(27)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(btnRunSimulation)
						.addGap(5)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(lblProgress)
								.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(39))
				);

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
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(Inp_budgetIncrease, 0, 0, Short.MAX_VALUE))
												.addGroup(gl_panel_2.createSequentialGroup()
														.addComponent(lblRequirementNeed)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(lblReqyuirementSigma)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(Inp_requirementNeed, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(lblNewLabel)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(Inp_requirementSigma, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_2.createSequentialGroup()
										.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel_2.createSequentialGroup()
														.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
																.addComponent(lblSigma_1)
																.addComponent(lblAgencyMoney))
														.addPreferredGap(ComponentPlacement.RELATED)
														.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
																.addComponent(Inp_agencyMoney, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
																.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING, false)
																		.addComponent(Inp_reserveFactor, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
																		.addComponent(Inp_agencySigma, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))))
												.addComponent(lblReserve))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
												.addComponent(lblEyesight)
												.addComponent(lblMoverate)
												.addComponent(lblBaseRisk))
										.addGap(18)
										.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING, false)
												.addComponent(Inp_baseRisk, 0, 0, Short.MAX_VALUE)
												.addComponent(Inp_moveRate, 0, 0, Short.MAX_VALUE)
												.addComponent(Inp_eyesight, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))))
						.addContainerGap(59, Short.MAX_VALUE))
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
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblReserve)
								.addComponent(Inp_reserveFactor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblBaseRisk)
								.addComponent(Inp_baseRisk, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(21)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblRequirementNeed)
								.addComponent(lblReqyuirementSigma)
								.addComponent(Inp_requirementNeed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel)
								.addComponent(Inp_requirementSigma, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblBudgetIncrease)
								.addComponent(Inp_budgetIncrease, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(15, Short.MAX_VALUE))
				);
		panel_2.setLayout(gl_panel_2);
		panel_1.setLayout(gl_panel_1);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton = new JButton("Clear");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText(null);
			}
		});
		panel.add(btnNewButton, BorderLayout.SOUTH);

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
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

			String i = chunks.get(chunks.size()-1);
			progressBar.setValue(Integer.parseInt(i));
			// textArea.append(i);
		} // process
		@Override 
		protected void done(){
			//progressBar.setValue(100);
			// textArea.append("finito");
			// System.out.println("finito???");
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
					agencyRequirementSigma,sightOfAgency, pickRandomSponsor, writeMethod, am,moveRate,
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
