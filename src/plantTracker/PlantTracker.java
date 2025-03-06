package plantTracker;

import java.util.InputMismatchException;
import java.util.Scanner;

public class PlantTracker {

	static Scanner input = new Scanner(System.in);

	public void start() {

		int choice = -1;

		do {
			System.out.println("\n\nWelcome to the plant tracker. What would you like to do?");
			System.out.println("\t1 - Add a plant");
			System.out.println("\t2 - Edit a plant");
			System.out.println("\t3 - Set reminders");
			System.out.println("\t4 - View reminders");
			System.out.println("\t5 - View graph");
			System.out.println("\t0 - Quit");

			System.out.print("\nEnter your choice: ");
			choice = getWholeNumberInput();

			if (choice == 1) {
				addPlant();
			} else if (choice == 2) {
				editPlant();
			} else if (choice == 3) {
				setReminders();
			} else if (choice == 4) {
				listReminders();
			} else if (choice == 5) {
				displayGraph();
			} else if (choice == 0) {
				break;
			} else {
				System.out.println("Plase enter a number between 0 and 5");
			}

		} while (true);

	}

	public void addPlant() {
		System.out.println("What sort of plant would you like to add?");
		// Instantiate a new plant with user input
	}

	public void editPlant() {
		System.out.println("Which plant would you like to change?");
		// List plants
	}

	public void listReminders() {
		// Access the reminders list in case you want to update or change reminders
	}

	public void setReminders() {
		System.out.println("What sort of reminder would you like to add?");
		// Should we make a Reminder function class related to plant objects, individual
		// Reminder objects, or should we make reminders here?
		// Water(Plant plant, Calendar calendar)
		// Fertilize(Plant plant, Calendar calendar)
		// Re-pot(Plant plant, Calendar calendar)
		// Move(Plant plant, Calendar calendar)
		// Harvest(Plant plant, Calendar calendar)

	}

	public void displayReminders() {
		// list active reminders
		// we should have a different method for
	}

	public void displayGraph() {
		// We should have a Graph class for tracking plant data
	}

	public int getWholeNumberInput() {

		Integer toReturn = -1;

		while (toReturn < 0) {

			try {
				toReturn = input.nextInt();

				if (toReturn < 0) {
					System.out.println("Please enter a positive number or 0 to quit.");
				}

			} catch (InputMismatchException e) {
				System.out.print("Invalid input, please enter an integer: ");
				input.next(); // Clear the invalid input from the scanner
			}
		}
		return toReturn;
	}

}
