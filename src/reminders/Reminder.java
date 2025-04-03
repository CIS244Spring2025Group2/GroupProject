public abstract class Reminder {
    private String plantName;
    private String date;
    private boolean completed;

    public Reminder(String plantName, String date) {
        this.plantName = plantName;
        this.date = date;
        this.completed = false;
    }

    public String getPlantName() {
        return plantName;
    }

    public String getDate() {
        return date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true;
    }

    public abstract String getDescription();

    @Override
    public String toString() {
        return getDescription() + " - Plant: " + plantName + " - Date: " + date + 
               " - Status: " + (completed ? "Completed" : "Pending");
    }
}