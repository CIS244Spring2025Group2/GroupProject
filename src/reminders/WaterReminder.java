public class WaterReminder extends Reminder {
    private int amountInMl;

    public WaterReminder(String plantName, String date, int amountInMl) {
        super(plantName, date);
        this.amountInMl = amountInMl;
    }

    public int getAmountInMl() {
        return amountInMl;
    }

    @Override
    public String getDescription() {
        return "Water " + getPlantName() + " with " + amountInMl + "ml of water";
    }
}