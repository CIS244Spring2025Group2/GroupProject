package plantTracker;

import java.util.Date;

public class FertilizeReminder extends Reminder {
	private String fertilizerType;
	private double amount;

	public FertilizeReminder(int plantId, Date date, String fertilizerType, double amount) {
		super(plantId, date);
		this.fertilizerType = fertilizerType;
		this.amount = amount;
	}

	public String getFertilizerType() {
		return fertilizerType;
	}

	public double getAmount() {
		return amount;
	}

	@Override
	public String getDescription() {
		return "Fertilize " + getPlantId() + " with " + amount + " of " + fertilizerType;
	}
}
