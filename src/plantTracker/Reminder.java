package plantTracker;

import java.util.Date;

public abstract class Reminder {
	private int plantName;
	private Date date;
	private boolean completed;

	public Reminder(int plantId, Date date) {
		this.plantName = plantName;
		this.date = date;
		this.completed = false;
	}

	public void setFrequency() {
		// working on how to do this
	}

	public int getPlantName() {
		return plantName;
	}

	public void setPlantName(int plantId) {
		this.plantName = plantId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
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
		return getDescription() + " - Plant: " + plantName + " - Date: " + date + " - Status: "
				+ (completed ? "Completed" : "Pending");
	}
}