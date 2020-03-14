package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;

	public Unit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}

	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}
	public boolean canTreat(Rescuable r) {
		if (r instanceof Citizen) {
			if (((Citizen) r).getBloodLoss() == 0 && ((Citizen) r).getToxicity() == 0)
				return false;
		}
		if (r instanceof ResidentialBuilding) {
			if (((ResidentialBuilding) r).getFireDamage() == 0 && ((ResidentialBuilding) r).getGasLevel() == 0
					&& ((ResidentialBuilding) r).getFoundationDamage() == 0)
				return false;
		}
		return true;

	}

	@Override
	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException {
		if (!canTreat(r))
			throw new CannotTreatException(this, r, "The target is SAFE!");

		if (r instanceof Citizen) {
			if (this instanceof Evacuator || this instanceof FireTruck || this instanceof FireUnit
					|| this instanceof GasControlUnit || this instanceof PoliceUnit)
				throw new IncompatibleTargetException(this, r, "Citizen is not compatible with this unit!");
		}
		if (r instanceof ResidentialBuilding) {
			if (this instanceof MedicalUnit)
				throw new IncompatibleTargetException(this, r, "Building is not compatible with this unit!");
		}
		if (this instanceof FireTruck && !(r.getDisaster() instanceof Fire))
			throw new CannotTreatException(this, r, "Wrong Unit chosen!");
		if (this instanceof GasControlUnit && !(r.getDisaster() instanceof GasLeak))
			throw new CannotTreatException(this, r, "Wrong Unit chosen!");
		if (this instanceof Evacuator && !(r.getDisaster() instanceof Collapse))
			throw new CannotTreatException(this, r, "Wrong Unit chosen!");
		if (this instanceof Ambulance && !(r.getDisaster() instanceof Injury))
			throw new CannotTreatException(this, r, "Wrong Unit chosen!");
		if (this instanceof DiseaseControlUnit && !(r.getDisaster() instanceof Infection))
			throw new CannotTreatException(this, r, "Wrong Unit chosen!");

		if (target != null && state == UnitState.TREATING)
			reactivateDisaster();
		finishRespond(r);

	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
	}

	public void finishRespond(Rescuable r) {
		target = r;
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX())
				+ Math.abs(t.getY() - location.getY());

	}

	public abstract void treat() throws CannotTreatException;

	public void cycleStep() throws CannotTreatException {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}

	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;

	}
}
