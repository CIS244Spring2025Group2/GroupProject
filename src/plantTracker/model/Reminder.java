package plantTracker.model;

import java.time.LocalDate;

public abstract class Reminder {
	private String plantName;
	private LocalDate date;
	private boolean recurring;
	private int intervals;
	private boolean complete;
	private String reminderType;
	private int reminderId;

	public Reminder(String plantName, LocalDate date, boolean recurring, int interval, String reminderType) {
		this.plantName = plantName;
		this.date = date;
		this.recurring = recurring;
		this.intervals = interval;
		this.complete = false;
		this.reminderType = reminderType;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(Boolean complete) {
		this.complete = complete;
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
				+ (complete ? "Completed" : "Pending");
	}

	public int getReminderId() {
		return reminderId;
	}

	public void setReminderId(int reminderId) {
		this.reminderId = reminderId;
	}
}