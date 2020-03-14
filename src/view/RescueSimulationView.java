package view;

import static org.junit.Assert.assertNotEquals;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Rectangle;
import java.lang.reflect.Array;
import java.nio.file.attribute.AclEntry.Builder;
import java.util.ArrayList;
import controller.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import controller.CommandCenter;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;
import simulation.Address;
import simulation.Rescuable;

public class RescueSimulationView extends JFrame {
	private static JButton nextCycleButton;
	private static JButton respond;
	
	private static JTextArea currentCycle1;
	private static JTextArea casualities1;
	private static JPanel availableUnits;
	private static JPanel activatedDisaster;
	private static JPanel idle;
	private static JPanel responding;
	private static JPanel treating;
	private static JPanel occupants;
	private static JPanel center;
	private static JPanel passengers;
	private static JLabel uJLabel;
	private static JLabel tJLabel;
	

	private static Unit currentUnit;
	private static Rescuable currentTarget;

	private static JTextArea t;
	private static JTextArea ta3;
	private static JTextArea ta2;
	private static JTextArea ta1;

	private static ArrayList<Disaster> ex = new ArrayList<>();
	private static ArrayList<Unit> units = new ArrayList<>();
	private static ArrayList<Citizen> citizens = new ArrayList<>();
	private static ArrayList<ResidentialBuilding> buildings = new ArrayList<>();
	private static ArrayList<JButton> unitsButtons = new ArrayList<>();
	private static ArrayList<Citizen> occupantsArray = new ArrayList<>();
	private static ArrayList<JButton> occupantsButtons = new ArrayList<>();
	private static ArrayList<Citizen> passengersEva = new ArrayList<>();


	private static ArrayList<JButton> gridButtons = new ArrayList<>();

	private static JPanel right;

	public RescueSimulationView() {
		setTitle("Rescue Simulation");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(0, 0, 5000, 1200);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		

		/////////////////////////////////////////// JPANEL
		/////////////////////////////////////////// RIGHT/////////////////////////////////////////////////////////////////
		right = new JPanel();
		right.setPreferredSize(new Dimension(300, 900));
		right.setLayout(new GridLayout(2, 1));

		///////////// Available Units

		availableUnits = new JPanel();
		availableUnits.setBorder(new TitledBorder("Available Units"));
		availableUnits.setLayout(new GridLayout(3, 1));
		availableUnits.setBackground(Color.LIGHT_GRAY);


		responding = new JPanel();
		responding.setBorder(new TitledBorder(null, "RESPONDING Units",0, 0, null,Color.white));
		responding.setBackground(Color.gray);
		
		availableUnits.add(responding);
		treating = new JPanel();
		treating.setBorder(new TitledBorder(null, "TREATING Units",0, 0, null,Color.white));
		treating.setBackground(Color.gray);

		availableUnits.add(treating);
		idle = new JPanel();
		idle.setBorder(new TitledBorder("IDLE Units"));
		idle.setBorder(new TitledBorder(null, "IDLE Units",0, 0, null,Color.white));
		idle.setBackground(Color.gray);

		availableUnits.add(idle);

		right.add(availableUnits, BorderLayout.NORTH);

		///////////// Activated Disaster

		activatedDisaster = new JPanel();
		activatedDisaster.setBorder(new TitledBorder("Activated Disasters"));
		t = new JTextArea("", 20, 20);
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		t.setEditable(false);
		t.setFont(new Font("", Font.BOLD, 20));
		t.setBackground(Color.LIGHT_GRAY);

		activatedDisaster.add(new JScrollPane(t));
		activatedDisaster.setLayout(new GridLayout(1, 1));
		right.add(activatedDisaster, BorderLayout.SOUTH);
		add(right, BorderLayout.EAST);

		/////////////////////////////////////////// JPANEL
		/////////////////////////////////////////// LEFT/////////////////////////////////////////////////////////////

		JPanel left = new JPanel();
		left.setPreferredSize(new Dimension(230, 900));
		left.setLayout(new GridLayout(3, 1));
		////////// Units,Citizen,Buildings Info
		JPanel unitinfo = new JPanel();
		unitinfo.setLayout(new GridLayout(1, 1));

		unitinfo.setBorder(new TitledBorder(null, "Unit Information",0, 0, null,Color.white));
		ta1 = new JTextArea();
		ta1.setLineWrap(true);
		ta1.setWrapStyleWord(true);
		ta1.setEditable(false);
		ta1.setFont(new Font("", Font.BOLD, 15));
		ta1.setBackground(Color.gray);
	
		unitinfo.setBackground(Color.lightGray);
		ta1.setForeground(Color.orange);


		unitinfo.add(new JScrollPane(ta1));
		left.add(unitinfo);

		JPanel citizeninfo = new JPanel();
		citizeninfo.setLayout(new GridLayout(1, 1));
		citizeninfo.setBorder(new TitledBorder(null, "Citizen Information",0, 0, null,Color.white));
		ta2 = new JTextArea();
		ta2.setLineWrap(true);
		ta2.setWrapStyleWord(true);
		ta2.setEditable(false);
		ta2.setFont(new Font("", Font.BOLD, 15));
		ta2.setBackground(Color.gray);
		ta2.setForeground(Color.orange);

		
		citizeninfo.setBackground(Color.lightGray);
		citizeninfo.add(new JScrollPane(ta2));
		left.add(citizeninfo);

		JPanel buildinginfo = new JPanel();
		buildinginfo.setLayout(new GridLayout(1, 1));

		buildinginfo.setBorder(new TitledBorder(null, "Building Information",0, 0, null,Color.white));
		ta3 = new JTextArea();
		ta3.setLineWrap(true);
		ta3.setWrapStyleWord(true);
		ta3.setEditable(false);
		ta3.setFont(new Font("", Font.BOLD, 15));
		ta3.setBackground(Color.gray);
		ta3.setForeground(Color.orange);
		buildinginfo.setBackground(Color.lightGray);

		buildinginfo.add(new JScrollPane(ta3));
		left.add(buildinginfo);

		add(left, BorderLayout.WEST);

		///////////////////////////////////////// JPANEL
		///////////////////////////////////////// CENTER/////////////////////////////////////////////////////////////

		center = new JPanel();

		//////////// Occupants

		occupants = new JPanel();
		occupants.setBorder(new TitledBorder("Occupants"));
		occupants.setPreferredSize(new Dimension(1400, 60));
		occupants.setBackground(Color.LIGHT_GRAY);
		center.add(occupants, BorderLayout.NORTH);

		passengers = new JPanel();
		passengers.setBorder(new TitledBorder(null, "Passengers",0, 0, null,Color.white));
		passengers.setPreferredSize(new Dimension(1400, 60));
		passengers.setBackground(Color.gray);

		center.add(passengers, BorderLayout.NORTH);

		//////////// Grid

		JPanel grid = new JPanel();
		grid.setPreferredSize(new Dimension(1400, 850));
		grid.setBorder(new TitledBorder(null, "Grid",0, 0, null,Color.white));
		grid.setLayout(new GridLayout(10, 10, 7, 7));
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				JButton x = new JButton();
				x.setToolTipText("(" + i + "," + j + ")");
				x.setBackground(Color.orange);
				grid.add(x);
				gridButtons.add(x);
			}
		}
		grid.setBackground(Color.DARK_GRAY);
		
		center.add(grid, BorderLayout.CENTER);

		revalidate();
		repaint();
		/////////// Casualities,CurrentCycle,NextCycle

		JPanel x = new JPanel();
		x.setLayout(new GridLayout(1, 5));
		x.setPreferredSize(new Dimension(800, 80));
		x.setBackground(Color.pink);

		JPanel x1=new JPanel();
		uJLabel= new JLabel("Current Unit :     -");
		tJLabel=new JLabel("Current Target :    -");
		x1.setLayout(new GridLayout(2, 1));
		x1.add(uJLabel,BorderLayout.NORTH);
		x1.add(tJLabel,BorderLayout.SOUTH);
		x.add(x1);
		
		////////// Casualities

		JPanel casualities = new JPanel();
		casualities.setLayout(new GridLayout(1, 1));

		casualities.setBorder(new TitledBorder("Casualities :"));
		casualities.setPreferredSize(new Dimension(5, 5));

		casualities1 = new JTextArea();
		casualities1.setPreferredSize(new Dimension(200, 49));
		casualities1.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 30));
		casualities1.setEditable(false);
		casualities.add(casualities1);
		x.add(casualities);
		
		
		
		revalidate();
		repaint();
		
		respond = new JButton("Respond");
		respond.setSize(new Dimension(20, 20));
		x.add(respond);
		respond.setBackground(Color.red);
		respond.setForeground(Color.orange);
		
		
		///////// CurrentCycle

		JPanel currentCycle = new JPanel();
		currentCycle.setLayout(new GridLayout(1, 1));

		currentCycle.setBorder(new TitledBorder("Current Cycle :"));

		currentCycle1 = new JTextArea();
		currentCycle1.setPreferredSize(new Dimension(250, 49));
		currentCycle1.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 30));
		currentCycle1.setEditable(false);
		currentCycle.add(currentCycle1);
		x.add(currentCycle);

		///////// NextCycle
		


		revalidate();
		repaint();
		
		
		
		nextCycleButton = new JButton("Next Cycle");
		nextCycleButton.setToolTipText("Next Cycle!");
		nextCycleButton.setSize(new Dimension(20, 20));
		x.add(nextCycleButton);
		nextCycleButton.setBackground(Color.red);
		nextCycleButton.setForeground(Color.orange);


	


		center.add(x, BorderLayout.SOUTH);
		add(center, BorderLayout.CENTER);

		//////////////////////////////////////////////////////////////////////////////////////////////////////

		this.setVisible(true);

	}

	public void updateUnitsButtons(Unit unit) {
		if (unit instanceof Ambulance) {
			Ambulance ambulance = (Ambulance) unit;
			ta1.setText(ambulance.tString());
			uJLabel.setText("Current Unit:    AMB");
		}
		if (unit instanceof Evacuator) {
			Evacuator ev = (Evacuator) unit;
			ta1.setText(ev.tString());
			uJLabel.setText("Current Unit:    EVC");
		}
		if (unit instanceof FireTruck) {
			FireTruck fireTruck = (FireTruck) unit;
			ta1.setText(fireTruck.tString());
			uJLabel.setText("Current Unit :    FTK");

		}
		if (unit instanceof GasControlUnit) {
			GasControlUnit gasControlUnit = (GasControlUnit) unit;
			ta1.setText(gasControlUnit.tString());
			uJLabel.setText("Current Unit:    GCU");

		}
		if (unit instanceof DiseaseControlUnit) {
			DiseaseControlUnit diseaseControlUnit = (DiseaseControlUnit) unit;
			ta1.setText(diseaseControlUnit.tString());
			uJLabel.setText("Current Unit:    DCU");

		}
		currentUnit = unit;
		revalidate();
	}
	

	

	public void addAvailableUnits(ArrayList<Unit> arr) {
		for (int i = 0; i < arr.size(); i++) {
			Unit unit = arr.get(i);
			JButton button = new JButton(getUnitName(unit));
			button.setToolTipText(unit.getUnitID());
			button.setBackground(Color.RED);
			button.setForeground(Color.ORANGE);
			unitsButtons.add(button);
			idle.add(button);
			availableUnits.add(idle, BorderLayout.NORTH);
			if (unit instanceof Ambulance)
				units.add(new Ambulance(unit.getUnitID(), unit.getLocation(), unit.getStepsPerCycle(),
						unit.getWorldListener()));
			if (unit instanceof FireTruck)
				units.add(new FireTruck(unit.getUnitID(), unit.getLocation(), unit.getStepsPerCycle(),
						unit.getWorldListener()));
			if (unit instanceof GasControlUnit)
				units.add(new GasControlUnit(unit.getUnitID(), unit.getLocation(), unit.getStepsPerCycle(),
						unit.getWorldListener()));
			if (unit instanceof Evacuator)
				units.add(new Evacuator(unit.getUnitID(), unit.getLocation(), unit.getStepsPerCycle(),
						unit.getWorldListener(), ((Evacuator) unit).getMaxCapacity()));
			if (unit instanceof DiseaseControlUnit)
				units.add(new DiseaseControlUnit(unit.getUnitID(), unit.getLocation(), unit.getStepsPerCycle(),
						unit.getWorldListener()));

		}
		add(right, BorderLayout.EAST);
		revalidate();
	}
	

	

	public static Unit getCurrentUnit() {
		return currentUnit;
	}

	public static Rescuable getCurrentTarget() {
		return currentTarget;
	}

	public void updateAvailableUnits1(ArrayList<Unit> arr) {
		for (int i = 0; i < arr.size(); i++) {
			for (int j = 0; j < units.size(); j++) {

				if (arr.get(i).getUnitID().equals(units.get(j).getUnitID())) {
					// System.out.println(1);
					if (!(arr.get(i).getState().equals(units.get(j).getState()))) {

						// System.out.println(1);
						JButton button = null;
						for (int k = 0; k < unitsButtons.size(); k++) {
							if (arr.get(i).getUnitID().equals(unitsButtons.get(k).getToolTipText()))
								button = unitsButtons.get(k);
						}
						if (units.get(i).getState() == UnitState.IDLE) {
							idle.remove(button);
							revalidate();
							repaint();

							if (arr.get(i).getState() == UnitState.TREATING) {
								treating.add(button);
								revalidate();
							}
							if (arr.get(i).getState() == UnitState.RESPONDING) {
								responding.add(button);
								revalidate();
							}
						}
						if (units.get(j).getState() == UnitState.TREATING) {
							treating.remove(button);
							revalidate();
							repaint();
							if (arr.get(i).getState() == UnitState.IDLE) {
								idle.add(button);
								revalidate();
							}
							if (arr.get(i).getState() == UnitState.RESPONDING) {
								responding.add(button);
								revalidate();
							}
						}
						if (units.get(j).getState() == UnitState.RESPONDING) {
							responding.remove(button);
							revalidate();
							repaint();

							if (arr.get(i).getState() == UnitState.TREATING) {
								treating.add(button);
								revalidate();
							}
							if (arr.get(i).getState() == UnitState.IDLE) {
								idle.add(button);
								revalidate();
							}
						}
						units.get(j).setState(arr.get(i).getState());
					}
				}

			}
		}
		revalidate();

	}

	public void updateAvailableUnits(Unit unit) {
		JButton button = null;
		for (int i = 0; i < unitsButtons.size(); i++) {
			if (unit.getUnitID().equals(unitsButtons.get(i).getToolTipText())) {
				button = unitsButtons.get(i);
			}

		}
		idle.remove(button);
		responding.add(button);
		revalidate();

	}

	// public JButton getUnitJButton() {

	// }

	/*
	 * public void updateAvailableUnits(ArrayList<Unit> arr) { boolean f = true;
	 * 
	 * for (int i = 0; i < arr.size(); i++) { Unit unit = arr.get(i); f = true; if
	 * (units.size() == 0) { if (unit.getState() == UnitState.IDLE) {
	 * units.add(unit); JButton button = new JButton(getUnitName(unit));
	 * button.setToolTipText(unit.getUnitID()); unitsButtons.add(button);
	 * idle.add(button); availableUnits.add(idle, BorderLayout.NORTH); } if
	 * (unit.getState() == UnitState.RESPONDING) { units.add(unit); JButton button =
	 * new JButton(getUnitName(unit)); button.setToolTipText(unit.getUnitID());
	 * unitsButtons.add(button);
	 * 
	 * responding.add(button); availableUnits.add(responding, BorderLayout.CENTER);
	 * } if (unit.getState() == UnitState.TREATING) { units.add(unit); JButton
	 * button = new JButton(getUnitName(unit));
	 * button.setToolTipText(unit.getUnitID()); unitsButtons.add(button);
	 * 
	 * treating.add(button); availableUnits.add(treating, BorderLayout.SOUTH); }
	 * 
	 * } else { for (int j = 0; j < units.size(); j++) { if (unit == units.get(j)) f
	 * = false; } if (f) { if (unit.getState() == UnitState.IDLE) { units.add(unit);
	 * JButton button = new JButton(getUnitName(unit));
	 * button.setToolTipText(unit.getUnitID()); unitsButtons.add(button);
	 * 
	 * idle.add(button); availableUnits.add(idle, BorderLayout.NORTH); } if
	 * (unit.getState() == UnitState.RESPONDING) { units.add(unit); JButton button =
	 * new JButton(getUnitName(unit)); button.setToolTipText(unit.getUnitID());
	 * unitsButtons.add(button);
	 * 
	 * responding.add(button); availableUnits.add(responding, BorderLayout.CENTER);
	 * } if (unit.getState() == UnitState.TREATING) { units.add(unit); JButton
	 * button = new JButton(getUnitName(unit));
	 * button.setToolTipText(unit.getUnitID()); unitsButtons.add(button);
	 * 
	 * treating.add(button); availableUnits.add(treating, BorderLayout.SOUTH); } }
	 * 
	 * } } right.add(availableUnits); add(right, BorderLayout.EAST);
	 * 
	 * }
	 */

	public static ArrayList<JButton> getUnitsButtons() {
		return unitsButtons;
	}

	public String getUnitName(Unit unit) {
		String x = "";
		if (unit instanceof Ambulance)
			x = "Ambulance";
		if (unit instanceof DiseaseControlUnit)
			x = "Disease Control Unit";
		if (unit instanceof Evacuator)
			x = "Evacuator";
		if (unit instanceof FireTruck)
			x = "Fire Truck";
		if (unit instanceof GasControlUnit)
			x = "Gas Control Unit";

		return x;

	}

	public void updateActivatedDisaster(ArrayList<Disaster> arr) {
		boolean f = true;
		Citizen c = null;
		ResidentialBuilding b = null;
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getTarget() instanceof Citizen)
				c = (Citizen) arr.get(i).getTarget();
			else
				b = (ResidentialBuilding) arr.get(i).getTarget();

			f = true;
			if (ex.size() == 0) {
				ex.add(arr.get(i));
				if (arr.get(i) instanceof Injury)
					t.setText(t.getText() + "Injury :" + c.getName() + " " +c.getLocation() + "\n");
				if (arr.get(i) instanceof Infection)
					t.setText(t.getText() + "Infection :" + c.getName() + " " +c.getLocation() +"\n");
				if (arr.get(i) instanceof Collapse)
					t.setText(t.getText() + "Collapse :" + b.getLocation() + "\n");
				if (arr.get(i) instanceof Fire)
					t.setText(t.getText() + "Fire : Building " + b.getLocation() + "\n");
				if (arr.get(i) instanceof GasLeak)
					t.setText(t.getText() + "GasLeak : Building " + b.getLocation() + "\n");

			} else {
				for (int j = 0; j < ex.size(); j++) {
					if (arr.get(i) == ex.get(j))
						f = false;
				}
				if (f) {
					ex.add(arr.get(i));
					if (arr.get(i) instanceof Injury)
						t.setText(t.getText() + "Injury : " + c.getName() + " " +c.getLocation() +"\n");
					if (arr.get(i) instanceof Infection)
						t.setText(t.getText() + "Infection : " + c.getName() + " " +c.getLocation() +"\n");
					if (arr.get(i) instanceof Collapse)
						t.setText(t.getText() + "Collapse : " + b.getLocation() + "\n");
					if (arr.get(i) instanceof Fire)
						t.setText(t.getText() + "Fire : Building " + b.getLocation() + "\n");
					if (arr.get(i) instanceof GasLeak)
						t.setText(t.getText() + "GasLeak : Building " + b.getLocation() + "\n");

				}
			}
		}
		right.add(activatedDisaster);
		add(right, BorderLayout.EAST);
		revalidate();

	}

	public void updateCurrentCycle(int c) {
		currentCycle1.setText("" + c);

	}

	public void updateCasualities(int c) {
		casualities1.setText("" + c);
	}

	public void updateCitizens(ArrayList<Citizen> arr) {
		boolean f = true;
		for (int i = 0; i < arr.size(); i++) {
			f = true;
			Citizen citizen = arr.get(i);
			JButton button = getButton(citizen.getLocation().toString());

			if (citizens.size() == 0) {
				citizens.add(citizen);
				getButton(citizen.getLocation().toString()).setText(citizen.getName());
				button.setBackground(Color.RED);
				button.setForeground(Color.WHITE);

			} else {
				for (int j = 0; j < citizens.size(); j++) {
					if (arr.get(i) == citizens.get(j))
						f = false;
				}
				if (f) {
					citizens.add(citizen);
					getButton(citizen.getLocation().toString()).setText(citizen.getName());
					button.setBackground(Color.RED);
					button.setForeground(Color.WHITE);
				}

			}
		}
		revalidate();
		repaint();

	}

	public void updateBuildings(ArrayList<ResidentialBuilding> arr) {

		boolean f = true;
		for (int i = 0; i < arr.size(); i++) {
			ResidentialBuilding building = arr.get(i);
	//		System.out.println(building.getLocation().toString());
			JButton button = getButton(building.getLocation().toString());
			
	//		System.out.println(getButton("2 2"));
			f = true;
		//	System.out.println(buildings.size());
			if (buildings.isEmpty()) {
				building = arr.get(i);
				buildings.add(building);
				button.setText("Building");
				button.setBackground(Color.RED);
				button.setForeground(Color.WHITE);

			} else {
				for (int j = 0; j < buildings.size(); j++) {
					if (arr.get(i) == buildings.get(j))
						f = false;
				}
				if (f) {
					button.setText("Building");
					button.setForeground(Color.WHITE);
					button.setBackground(Color.RED);

				}
			}
		}
		revalidate();
		repaint();

	}

	public JButton getNextCycleButton() {
		return nextCycleButton;
	}

	public static JButton getRespond() {
		return respond;
	}

	public static JButton getButton(String text) {
		for (int i = 0; i < gridButtons.size(); i++) {
			if (gridButtons.get(i).getToolTipText().equals(text))
				return gridButtons.get(i);
		}
		System.out.println(gridButtons.size());
		return null;
	}

	public static ArrayList<JButton> getGridButtons() {
		return gridButtons;
	}

	public static ArrayList<JButton> getOccupantsButtons() {
		return occupantsButtons;
	}

	public static ArrayList<Citizen> getOccupantsArray() {
		return occupantsArray;
	}

	public static JTextArea getTa3() {
		return ta3;
	}

	public static JTextArea getTa2() {
		return ta2;
	}

	public static JTextArea getTa1() {
		return ta1;
	}
	public void updatePassengers(Evacuator evacuator) {
	//	System.out.println(1);
		while(!passengersEva.isEmpty()) {
			passengersEva.remove(0);
			passengers.remove(0);
			revalidate();
			repaint();
		}
		
		for(int i=0;i<evacuator.getPassengers().size();i++) {
		
			passengers.add(occupantsButtons.get(i));
			passengersEva.add(evacuator.getPassengers().get(i));
			revalidate();
			repaint();
		}
	}
	public void updateInfo(Rescuable r) {
		ResidentialBuilding building = null;
		Citizen citizen = null;
		if (r instanceof ResidentialBuilding) {
			currentTarget = r;

			building = (ResidentialBuilding) r;
			ta3.setText(building.toString());
			while (!occupantsArray.isEmpty()) {

				occupantsArray.remove(0);
				occupants.removeAll();
				

				occupantsButtons.remove(0);
				revalidate();
				repaint();
			}
			for (int i = 0; i < building.getOccupants().size(); i++) {

				JButton x = new JButton(building.getOccupants().get(i).getName());
				x.setBackground(Color.red);
				x.setForeground(Color.ORANGE);
				occupants.add(x);
				occupantsArray.add(building.getOccupants().get(i));
				occupantsButtons.add(x);
				revalidate();
				repaint();

			}
			tJLabel.setText("Current Target : Building" );

		}
		if (r instanceof Citizen) {
			citizen = (Citizen) r;
			ta2.setText(citizen.toString());
			currentTarget = r;
			tJLabel.setText("Current Target : " + ((Citizen)(currentTarget)).getName());


		}
		revalidate();
		repaint();
	}
}
