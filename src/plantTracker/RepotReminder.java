package plantTracker;

import java.util.Date;

public class RepotReminder extends Reminder {
	private String newPotSize;
	private String soilType;

	public RepotReminder(String plantName, Date date, String newPotSize, String soilType) {
		super(plantName, date);
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
		return "Re-pot " + getPlantName() + " into " + newPotSize + " pot with " + soilType + " soil";
	}
}
