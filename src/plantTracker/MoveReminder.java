package plantTracker;

import java.util.Date;

public class MoveReminder extends Reminder {
	private String newLocation;
	private String reason;

	public MoveReminder(int plantId, Date date, String newLocation, String reason) {
		super(plantId, date);
		this.newLocation = newLocation;
		this.reason = reason;
	}

	public String getNewLocation() {
		return newLocation;
	}

	public String getReason() {
		return reason;
	}

	@Override
	public String getDescription() {
		return "Move " + getPlantId() + " to " + newLocation + " (" + reason + ")";
	}
}
