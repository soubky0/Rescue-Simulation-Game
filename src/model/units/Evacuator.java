package model.units;

import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class Evacuator extends PoliceUnit {

	public Evacuator(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener, int maxCapacity) {
		super(unitID, location, stepsPerCycle, worldListener, maxCapacity);

	}
	public String tString() {
		String result = "";
		if (getTarget() != null) {
			result = "Unit ID : " + super.getUnitID() + "\n" + "Type : Evacuator" + "\n" + "Location : "
					+ super.getLocation() + "\n" + "Steps Per Cycle : " + super.getStepsPerCycle() + "\n"
					+ "Target : Building" + "\n" + "Target Location : " + super.getTarget().getLocation() + "\n"
					+ "State : " + getState() + "\n" + "Number of Passengers : " + getPassengers().size();
		} else {
			result = "Unit ID : " + super.getUnitID() + "\n" + "Type : Evacuator" + "\n" + "Location : "
					+ super.getLocation() + "\n" + "Steps Per Cycle : " + super.getStepsPerCycle() + "\n" 
					+ "State : " + getState() + "\n" + "Number of Passengers : " + getPassengers().size();
		}
		return result;
	}

	@Override
	public void treat() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0
				|| target.getOccupants().size() == 0) {
			jobsDone();
			return;
		}

		for (int i = 0; getPassengers().size() != getMaxCapacity()
				&& i < target.getOccupants().size(); i++) {
			getPassengers().add(target.getOccupants().remove(i));
			i--;
		}

		setDistanceToBase(target.getLocation().getX()
				+ target.getLocation().getY());

	}

}
