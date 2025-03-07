package plantTracker;

public class FruitingPlant extends Plant {
	public String name;
	public String scientificName;
	public String fruitType;
	public String sunlightRequirement;
	public String waterRequirement;
	public String growthSeason;

	public FruitingPlant(String name, String scientificName, String fruitType, String sunlightRequirement,
			String waterRequirement, String growthSeason) {
		this.name = name;
		this.scientificName = scientificName;
		this.fruitType = fruitType;
		this.sunlightRequirement = sunlightRequirement;
		this.waterRequirement = waterRequirement;
		this.growthSeason = growthSeason;
	}

	public void displayInfo() {
		System.out.println("Fruiting Plant: " + name);
		System.out.println("Scientific Name: " + scientificName);
		System.out.println("Fruit Type: " + fruitType);
		System.out.println("Sunlight Requirement: " + sunlightRequirement);
		System.out.println("Water Requirement: " + waterRequirement);
		System.out.println("Growth Season: " + growthSeason);
		System.out.println("---------------------------");
	}
}