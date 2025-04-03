public class HarvestReminder extends Reminder {
    private String harvestPart;
    private String useFor;

    public HarvestReminder(String plantName, String date, String harvestPart, String useFor) {
        super(plantName, date);
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
        return "Harvest " + harvestPart + " from " + getPlantName() + " for " + useFor;
    }
}