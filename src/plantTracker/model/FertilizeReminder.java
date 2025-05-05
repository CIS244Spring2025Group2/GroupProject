package plantTracker.model;

import java.time.LocalDate;

public class FertilizeReminder extends Reminder {
	private String fertilizerType;
	private int amount;

	public FertilizeReminder(String plantName, LocalDate date, boolean recurring, int interval, String fertilizerType,
			int amount) {
		super(plantName, date, recurring, interval, "Fertilize Reminder");
		this.fertilizerType = fertilizerType;
		this.amount = amount;
	}

	public String getFertilizerType() {
		return fertilizerType;
	}

	public void setFertilizerType(String fertilizerType) {
		this.fertilizerType = fertilizerType;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	@Override
	public String getDescription() {
		return "Fertilize " + getPlantName() + " with " + amount + " of " + fertilizerType;
	}
}
