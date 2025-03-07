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
		// 1 - vegetable
		// 2 - Flowering plant
		// etc.

		// if 1, get name of plant and species
		// create plant object
	}

	public void editPlant() {
		System.out.println("Which plant would you like to change?");
		// List plants
		// ArrayList<Plants>
		// Take number -1 to find the object in the array
		// Info about the plant - trigger all the Getters for the plant object give them
		// in a list
		// Give a list of edit option - list Setters
		// Take input of which Setter and new value
	}

	public void setReminders() {
		System.out.println("What sort of reminder would you like to add?");
		// Make all reminder objects
		// Water(Plant plant, Calendar calendar)
		// Fertilize(Plant plant, Calendar calendar)
		// Re-pot(Plant plant, Calendar calendar)
		// Move(Plant plant, Calendar calendar)
		// Harvest(Plant plant, Calendar calendar)
		// Give a list of reminder options
		// Take user input to create a reminder object

	}

	public void listReminders() {
		// Access the reminders list in case you want to update or change reminders
		// Show an ArrayList<Reminders>
		// Give option to update reminders
	}

	public void displayReminders() {
		// list active reminders
		// we should have a different method for
		// User java.util.Calendar to display reminders
	}

	public void displayGraph() {
		// We should have a Graph class for tracking plant data
		// to do
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
