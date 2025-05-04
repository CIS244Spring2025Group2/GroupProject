package plantTracker.model;

import java.time.LocalDate;

public abstract class Reminder {
	private String plantName;
	private LocalDate firstDueDate;
	private LocalDate lastDueDate;
	private LocalDate currentDueDate;
	private LocalDate nextDueDate;
	private boolean recurring;
	private int intervals;
	private boolean complete;
	private String reminderType;
	private int reminderId;

	public Reminder(String plantName, LocalDate firstDueDate, boolean recurring, int interval, String reminderType) {
		this.plantName = plantName;
		this.firstDueDate = firstDueDate;
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
		return getDescription() + " - Plant: " + plantName + " - First Due Date: " + firstDueDate
				+ " - Current Due Date: " + currentDueDate + " - Status: " + (complete ? "Completed" : "Pending");
	}

	public int getReminderId() {
		return reminderId;
	}

	public void setReminderId(int reminderId) {
		this.reminderId = reminderId;
	}

	public LocalDate getFirstDueDate() {
		return firstDueDate;
	}

	public void setFirstDueDate(LocalDate firstDueDate) {
		this.firstDueDate = firstDueDate;
	}

	public LocalDate getNextDueDate() {
		return nextDueDate;
	}

	public void setNextDueDate(LocalDate nextDueDate) {
		this.nextDueDate = nextDueDate;
	}

	public LocalDate getCurrentDueDate() {
		return currentDueDate;
	}

	public void setCurrentDueDate(LocalDate currentDueDate) {
		this.currentDueDate = currentDueDate;
	}

	public LocalDate getLastDueDate() {
		return lastDueDate;
	}

	public void setLastDueDate(LocalDate lastDueDate) {
		this.lastDueDate = lastDueDate;
	}
}