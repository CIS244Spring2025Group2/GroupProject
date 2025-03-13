import java.time.LocalDate;

//  Reminder class
abstract class Reminder {
    protected String message;
    protected LocalDate reminderDate;

    public Reminder(String message, LocalDate reminderDate) {
        this.message = message;
        this.reminderDate = reminderDate;
    }

    public abstract void triggerReminder();

    public String getMessage() {
        return message;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }
}

// Child class
class FertilizeReminder extends Reminder {
    private String plantType;

    public FertilizeReminder(String message, LocalDate reminderDate, String plantType) {
        super(message, reminderDate);
        this.plantType = plantType;
    }

    @Override
    public void triggerReminder() {
        System.out.println("Reminder: " + message + " for " + plantType + " on " + reminderDate);
    }

    public String getPlantType() {
        return plantType;
    }
}
