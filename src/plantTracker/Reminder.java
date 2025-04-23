package plantTracker;

import java.util.Date;

public abstract class Reminder {
	private int plantId;
	private Date date;
	private boolean completed;

	public Reminder(int plantId, Date date) {
		this.plantId = plantId;
		this.date = date;
		this.completed = false;
	}

	public void setFrequency() {
		// working on how to do this
	}

	public int getPlantId() {
		return plantId;
	}

	public void setPlantId(int plantId) {
		this.plantId = plantId;
	}

	public Date getCalendar() {
		return date;
	}

	public void setCalendar(Date date) {
		this.date = date;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void markCompleted() {
		this.completed = true;
	}

	public abstract String getDescription();

	@Override
	public String toString() {
		return getDescription() + " - Plant: " + plantId + " - Date: " + date + " - Status: "
				+ (completed ? "Completed" : "Pending");
	}
}