package plantTracker.model;

import java.util.Date;

public class CarnivorousPlant extends Plant {
	private String foodType;

	public CarnivorousPlant() {
		this("N/A", "N/A", new Date());
	}

	public CarnivorousPlant(String name, String species, Date date) {
		this(name, species, date, "N/A");
	}

	public CarnivorousPlant(String name, String species, Date date, String foodType) {
		super(name, species, date);
		this.foodType = foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	public String getFoodType() {
		return foodType;
	}
}
