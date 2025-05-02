package plantTracker.model;

import java.util.Date;

public class MoveReminder extends Reminder {
	private String newLocation;
	private String reason;

	public MoveReminder(String plantName, Date date, boolean recurring, int interval, String newLocation,
			String reason) {
		super(plantName, date, recurring, interval, "Move Reminder");
		this.newLocation = newLocation;
		this.reason = reason;
	}

	public String getNewLocation() {
		return newLocation;
	}

	public void setNewLocation(String newLocation) {
		this.newLocation = newLocation;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	@Override
	public String getDescription() {
		return "Move " + getPlantName() + " to " + newLocation + " (" + reason + ")";
	}
}
