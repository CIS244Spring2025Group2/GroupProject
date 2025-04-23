package plantTracker;

import java.util.Date;

public class HarvestReminder extends Reminder {
	private String harvestPart;
	private String useFor;

	public HarvestReminder(int plantId, Date date, String harvestPart, String useFor) {
		super(plantId, date);
		this.harvestPart = harvestPart;
		this.useFor = useFor;
	}

	public String getHarvestPart() {
		return harvestPart;
	}

	public String getUseFor() {
		return useFor;
	}

	@Override
	public String getDescription() {
		return "Harvest " + harvestPart + " from " + getPlantId() + " for " + useFor;
	}
}
