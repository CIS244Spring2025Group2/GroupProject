package plantTracker.model;

import java.time.LocalDate;

public class WaterReminder extends Reminder {
	private int amountInMl;

	public WaterReminder(String plantName, LocalDate date, boolean recurring, int interval, int amountInMl) {
		super(plantName, date, recurring, interval, "Water Reminder");
		this.amountInMl = amountInMl;
	}

	public int getAmountInMl() {
		return amountInMl;
	}

	public void setAmountInMl(int amountInMl) {
		this.amountInMl = amountInMl;
	}

	@Override
	public String getDescription() {
		return "Water " + getPlantName() + " with " + amountInMl + "ml of water";
	}
}
