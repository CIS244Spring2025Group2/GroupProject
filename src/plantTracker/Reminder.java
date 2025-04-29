package plantTracker;

import java.util.Date;

public abstract class Reminder {
	private String plantName;
	private Date date;
	private boolean recurring;
	private int intervals;
	private boolean completed;
	private String reminderType;

	public Reminder(String plantName, Date date, boolean recurring, int interval, String reminderType) {
		this.plantName = plantName;
		this.date = date;
		this.recurring = recurring;
		this.intervals = interval;
		this.completed = false;
		this.reminderType = reminderType;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
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

	public boolean isRecurring() {
		return recurring;
	}

	public void setRecurring(boolean recurring) {
		this.recurring = recurring;
	}

	public int getIntervals() {
		return intervals;
	}

	public void setIntervals(int intervals) {
		this.intervals = intervals;
	}

	public String getReminderType() {
		return reminderType;
	}

	public void setReminderType(String reminderType) {
		this.reminderType = reminderType;
	}

	@Override
	public String toString() {
		return getDescription() + " - Plant: " + plantName + " - Date: " + date + " - Status: "
				+ (completed ? "Completed" : "Pending");
	}
}