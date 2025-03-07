package plantTracker;

public class CarnivorousPlant extends Plant {
	private String foodType;

	public CarnivorousPlant() {
		this("N/A", "N/A");
	}

	public CarnivorousPlant(String name, String species) {
		this(name, species, "N/A");
	}

	public CarnivorousPlant(String name, String species, String foodType) {
		super(name, species);
		this.foodType = foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	public String getFoodType() {
		return foodType;
	}
}
