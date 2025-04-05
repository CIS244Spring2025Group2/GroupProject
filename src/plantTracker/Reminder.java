package plantTracker;

import java.util.Calendar;

public abstract class Reminder {
	private Plant plant;
	private Calendar calendar;

	public Reminder(Plant plant, Calendar calendar) {
		this.plant = plant;
		this.calendar = calendar;
	}

	public void setFrequency() {
		// working on how to do this
	}

	public Plant getPlant() {
		return plant;
	}

	public void setPlant(Plant plant) {
		this.plant = plant;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

}
