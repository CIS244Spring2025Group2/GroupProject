public class PlantCareDemo {
    public static void main(String[] args) {
        // Create some reminders
        WaterReminder wateringTask = new WaterReminder("Monstera", "2025-04-04", 250);
        FertilizeReminder fertilizeTask = new FertilizeReminder("Orchid", "2025-04-10", "Liquid fertilizer", 15);
        RepotReminder repotTask = new RepotReminder("Snake Plant", "2025-04-15", "8-inch", "Cactus mix");
        MoveReminder moveTask = new MoveReminder("Fiddle Leaf Fig", "2025-04-05", "East window", "More light needed");
        HarvestReminder harvestTask = new HarvestReminder("Basil", "2025-04-07", "leaves", "Pasta dish");
        
        // Print reminders
        System.out.println(wateringTask);
        System.out.println(fertilizeTask);
        System.out.println(repotTask);
        System.out.println(moveTask);
        System.out.println(harvestTask);
        
        // Mark a task as completed
        wateringTask.markCompleted();
        System.out.println("\nAfter completion:");
        System.out.println(wateringTask);
    }
}