package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CannotTreatException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.IncompatibleTargetException;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Evacuator;
import model.units.Unit;
import simulation.Rescuable;
import simulation.Simulator;
import view.RescueSimulationView;


public class CommandCenter implements SOSListener, ActionListener {

	private static final int EXIT_ON_CLOSE = 0;
	private static Simulator engine;
	private static ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private ArrayList<Unit> emergencyUnits;
	private static RescueSimulationView rescueSimulationView;

	public CommandCenter() throws Exception {
		engine = new Simulator(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();
		rescueSimulationView = new RescueSimulationView();

		rescueSimulationView.getNextCycleButton().addActionListener(this);
		rescueSimulationView.getRespond().addActionListener(this);

		for (int i = 0; i < rescueSimulationView.getGridButtons().size(); i++) {
			rescueSimulationView.getGridButtons().get(i).addActionListener(this);
		}
		rescueSimulationView.addAvailableUnits(emergencyUnits);
	//	System.out.println(emergencyUnits.size());

		for (int i = 0; i < rescueSimulationView.getUnitsButtons().size(); i++) {
			rescueSimulationView.getUnitsButtons().get(i).addActionListener(this);

		}
		

	}
	

	public static void main(String[] args) throws Exception {
		CommandCenter center = new CommandCenter();
	//	System.out.println(visibleBuildings.size());

	}

	public ArrayList<ResidentialBuilding> getVisibleBuildings() {
		return visibleBuildings;
	}

	public ArrayList<Citizen> getVisibleCitizens() {
		return visibleCitizens;
	}

	@Override
	public void receiveSOSCall(Rescuable r) {

		if (r instanceof ResidentialBuilding) {

			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);

		} else {

			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == rescueSimulationView.getNextCycleButton()) {
			try {
				engine.nextCycle();

			} catch (CitizenAlreadyDeadException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());

			} catch (BuildingAlreadyCollapsedException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());

			} catch (CannotTreatException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());

			}
			rescueSimulationView.revalidate();
			rescueSimulationView.repaint();
			rescueSimulationView.updateAvailableUnits1(emergencyUnits);
			rescueSimulationView.updateActivatedDisaster(engine.getExecutedDisasters());
			rescueSimulationView.updateCurrentCycle(engine.getCurrentCycle());
			rescueSimulationView.updateCasualities(engine.calculateCasualties());
			rescueSimulationView.updateCitizens(visibleCitizens);
			rescueSimulationView.updateBuildings(visibleBuildings);
		//	System.out.println(visibleBuildings.size());
			rescueSimulationView.revalidate();
			rescueSimulationView.repaint();

			if (engine.checkGameOver() == true) {
				JOptionPane.showMessageDialog(null, "Game Over :(" +"\n"+ "Your Score is " + engine.calculateCasualties());

			}

		}

		for (int i = 0; i < rescueSimulationView.getGridButtons().size(); i++) {

			
			if (e.getSource() == rescueSimulationView.getGridButtons().get(i)) {
				if (rescueSimulationView.getGridButtons().get(i).getText().equals("Building")) {
					for (int j = 0; j < visibleBuildings.size(); j++) {
						if (rescueSimulationView.getGridButtons().get(i).getToolTipText()
								.equals(visibleBuildings.get(j).getLocation().toString())) {
							 
							rescueSimulationView.updateInfo(visibleBuildings.get(j));
							
						}

					}
				} else {
					for (int j = 0; j < visibleCitizens.size(); j++) {
						if (rescueSimulationView.getGridButtons().get(i).getToolTipText()
								.equals(visibleCitizens.get(j).getLocation().toString())) {
							
							rescueSimulationView.updateInfo(visibleCitizens.get(j));

						}
					}

				}

			}

		}
		for (int i = 0; i < rescueSimulationView.getUnitsButtons().size(); i++) {

			if (e.getSource() == rescueSimulationView.getUnitsButtons().get(i)) {
				// System.out.println(1);

				// System.out.println(loadScreen.rescueSimulationView.getUnitsButtons().size());

				for (int j = 0; j < emergencyUnits.size(); j++) {
					if (rescueSimulationView.getUnitsButtons().get(i).getToolTipText()
							.equals(emergencyUnits.get(j).getUnitID())) {
						// System.out.println(2);
						rescueSimulationView.updateUnitsButtons(emergencyUnits.get(j));
						if(emergencyUnits.get(j) instanceof Evacuator)
							rescueSimulationView.updatePassengers((Evacuator)emergencyUnits.get(j));
					}
				}
			}

		}
		for (int i = 0; i < rescueSimulationView.getOccupantsButtons().size(); i++) {
			rescueSimulationView.getOccupantsButtons().get(i).addActionListener(this);

		}
		for (int i = 0; i < rescueSimulationView.getOccupantsButtons().size(); i++) {
			if (e.getSource() == rescueSimulationView.getOccupantsButtons().get(i)) {
				for (int j = 0; j < rescueSimulationView.getOccupantsArray().size(); j++) {
					if (rescueSimulationView.getOccupantsArray().get(j).getName()
							.equals(rescueSimulationView.getOccupantsButtons().get(i).getText()))
						rescueSimulationView.updateInfo(rescueSimulationView.getOccupantsArray().get(j));
				}

			}
		}
		if(e.getSource() == rescueSimulationView.getRespond()) {
			try {
				
				rescueSimulationView.getCurrentUnit().respond(rescueSimulationView.getCurrentTarget());
			//	loadScreen.rescueSimulationView.updateAvailableUnits(loadScreen.rescueSimulationView.getCurrentUnit());
			}
			catch(NullPointerException e1) {
				JOptionPane.showMessageDialog(null, "No Units or Targets are selected");

			}
			catch(CannotTreatException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());

			}
			catch(IncompatibleTargetException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());

			}
		
		}
		

	}
}
