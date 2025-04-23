package plantTracker;

import java.util.Date;

public class WaterReminder extends Reminder {
	private int amountInMl;

	public WaterReminder(int plantId, Date date, int amountInMl) {
		super(plantId, date);
		this.amountInMl = amountInMl;
	}

	public int getAmountInMl() {
		return amountInMl;
	}

	@Override
	public String getDescription() {
		return "Water " + getPlantId() + " with " + amountInMl + "ml of water";
	}
}
