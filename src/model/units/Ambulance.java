package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getBloodLoss() > 0) {
			target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
			if (target.getBloodLoss() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getBloodLoss() == 0)

			heal();

	}
	public String tString() {
		String result = "";

		if (getTarget() != null) {
			if (getTarget() instanceof Citizen) {
				result = "Unit ID : " + getUnitID() + "\n" + "Type : Ambulance" + "\n" + "Location : " + getLocation() + "\n"
						+ "Steps Per Cycle : " + getStepsPerCycle() + "\n" + "Target : "
						+ ((Citizen) getTarget()).getName() + "\n" + "Target Location : " + getTarget().getLocation()
						+ "\n" + "State : " + getState() + "\n";
			}
		} else {
			result = "Unit ID : " + getUnitID() + "\n" + "Type : Ambulance"+ "\n" + "Location : " + getLocation() + "\n"
					+ "Steps Per Cycle : " + getStepsPerCycle() + "\n" + "State : " + getState() + "\n";
		}
		return result;
	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		super.respond(r);

		if (r instanceof ResidentialBuilding)
			throw new IncompatibleTargetException(this, r, "Building is not compatible with this unit!");

		if (getTarget() != null && ((Citizen) getTarget()).getBloodLoss() > 0 && getState() == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);
	}

}
