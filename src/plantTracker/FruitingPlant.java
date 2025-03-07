package plantTracker;

public class FruitingPlant extends Plant {

	public String fruit;

	public FruitingPlant() {
		this("NA", "NA", "NA");
	}

	public FruitingPlant(String name, String species) {
		this(name, species, "NA");
	}

	public FruitingPlant(String name, String species, String fruit) {
		super(name, species);
		this.fruit = fruit;
	}

	public void setFruit(String fruit) {
		this.fruit = fruit;
	}

	public String getFruit() {
		return fruit;
	}

}