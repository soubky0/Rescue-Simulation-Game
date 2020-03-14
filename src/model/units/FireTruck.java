package model.units;

import exceptions.CannotTreatException;
import model.disasters.Collapse;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}
	public String tString() {
		String result = "";
		if (getTarget() != null) {
			result = "Unit ID : " + super.getUnitID() + "\n" + "Type : FireTruck" + "\n" + "Location : "
					+ super.getLocation() + "\n" + "Steps Per Cycle : " + super.getStepsPerCycle() + "\n"
					+ "Target : Building" + "\n" + "Target Location : " + super.getTarget().getLocation() + "\n"
					+ "State : " + getState() + "\n";
		} else {
			result = "Unit ID : " + super.getUnitID() + "\n" + "Type : FireTruck" + "\n" + "Location : "
					+ super.getLocation() + "\n" + "Steps Per Cycle : " + super.getStepsPerCycle() + "\n"
					+ "State : " + getState() + "\n";
		}
		return result;
	}

	@Override
	public void treat() throws CannotTreatException {
		if(((ResidentialBuilding)(getTarget())).getDisaster() instanceof Collapse)
			throw new CannotTreatException(this, getTarget(), "Can not treat!");
		
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getFireDamage() > 0)

			target.setFireDamage(target.getFireDamage() - 10);

		if (target.getFireDamage() == 0)

			jobsDone();

	}

}
