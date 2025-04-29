package plantTracker;

import java.util.Date;

public class RepotReminder extends Reminder {
	private String newPotSize;
	private String soilType;

	public RepotReminder(String plantName, Date date, boolean recurring, int interval, String newPotSize,
			String soilType) {
		super(plantName, date, recurring, interval, "Repot Reminder");
		this.newPotSize = newPotSize;
		this.soilType = soilType;
	}

	public String getNewPotSize() {
		return newPotSize;
	}

	public void setNewPotSize(String newPotSize) {
		this.newPotSize = newPotSize;
	}

	public void setSoilType(String soilType) {
		this.soilType = soilType;
	}

	public String getSoilType() {
		return soilType;
	}

	@Override
	public String getDescription() {
		return "Re-pot " + getPlantName() + " into " + newPotSize + " pot with " + soilType + " soil";
	}

}
