package plantTracker;

public class Vegetable extends Plant {
	private String vegetable;

	public Vegetable() {
		this("NA", "NA");
	}

	public Vegetable(String name, String species) {
		this(name, species, "NA");
	}

	public Vegetable(String name, String species, String vegetable) {
		super(name, species);
		this.vegetable = vegetable;
	}

	public String getVegetable() {
		return vegetable;
	}

	public void setVegetable(String vegetable) {
		this.vegetable = vegetable;
	}
}
