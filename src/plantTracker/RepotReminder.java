package plantTracker;

import java.util.Date;

public class RepotReminder extends Reminder {
	private String newPotSize;
	private String soilType;

	public RepotReminder(int plantId, Date date, String newPotSize, String soilType) {
		super(plantId, date);
		this.newPotSize = newPotSize;
		this.soilType = soilType;
	}

	public String getNewPotSize() {
		return newPotSize;
	}

	public String getSoilType() {
		return soilType;
	}

	@Override
	public String getDescription() {
		return "Re-pot " + getPlantId() + " into " + newPotSize + " pot with " + soilType + " soil";
	}
}
