package model.units;

import exceptions.CannotTreatException;
import model.disasters.Collapse;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}
	public String tString() {
		String result = "";
		if (getTarget() != null) {
			result = "Unit ID : " + super.getUnitID() + "\n" + "Type : Gas Control Unit" + "\n" + "Location : "
					+ super.getLocation() + "\n" + "Steps Per Cycle : " + super.getStepsPerCycle() + "\n"
					+ "Target : Building" + "\n" + "Target Location : " + super.getTarget().getLocation() + "\n"
					+ "State : " + getState() + "\n";
		} else {
			result = "Unit ID : " + super.getUnitID() + "\n" + "Type : Gas Control Unit" + "\n" + "Location : "
					+ super.getLocation() + "\n" + "Steps Per Cycle : " + super.getStepsPerCycle() + "\n" + "State : "
					+ getState() + "\n";
		}
		return result;
	}

	public void treat() throws CannotTreatException {
		if (((ResidentialBuilding) (getTarget())).getDisaster() instanceof Collapse)
			throw new CannotTreatException(this, getTarget(), "Can not treat!");

		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getGasLevel() > 0)
			target.setGasLevel(target.getGasLevel() - 10);

		if (target.getGasLevel() == 0)
			jobsDone();

	}

}
