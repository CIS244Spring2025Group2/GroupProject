package plantTracker.model;

import java.time.LocalDate;

public class HarvestReminder extends Reminder {
	private String harvestPart;
	private String useFor;

	public HarvestReminder(String plantName, LocalDate date, boolean recurring, int interval, String harvestPart,
			String useFor) {
		super(plantName, date, recurring, interval, "Harvest Reminder");
		this.harvestPart = harvestPart;
		this.useFor = useFor;
	}

	public String getHarvestPart() {
		return harvestPart;
	}

	public String getUseFor() {
		return useFor;
	}

	public void setHarvestPart(String harvestPart) {
		this.harvestPart = harvestPart;
	}

	public void setUseFor(String useFor) {
		this.useFor = useFor;
	}

	@Override
	public String getDescription() {
		return "Harvest " + harvestPart + " from " + getPlantName() + " for " + useFor;
	}
}
