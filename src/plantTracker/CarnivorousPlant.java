package plantTracker;

public class CarnivorousPlant extends Plant {
	private String foodType;
	
	public CarnivorousPlant() {
		this("N/A", "N/A", "N/A");
	}
	
	public CarnivorousPlant(String name, String species) {
		super(name, species);
		this.foodType = "N/A";
	}
	
	public CarnivorousPlant(String name, String species, String foodType) {
		super(name, species);
		this.foodType = foodType;
	}
	
	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}
	
	public String getFoodType() {
		return this.foodType;
	}
}
