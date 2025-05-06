public class MoveReminder extends Reminder {
    private String newLocation;
    private String reason;

    public MoveReminder(String plantName, String date, String newLocation, String reason) {
        super(plantName, date);
        this.newLocation = newLocation;
        this.reason = reason;
    }

    public String getNewLocation() {
        return newLocation;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String getDescription() {
        return "Move " + getPlantName() + " to " + newLocation + " (" + reason + ")";
    }
}