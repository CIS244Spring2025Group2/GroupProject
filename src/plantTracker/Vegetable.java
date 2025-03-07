package plantTracker;

public class Vegetable extends Plant {
	private String vegetable;

	public Vegetable() {
		super();
		this.vegetable = "";
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

	public int getAge() {
		return 0;
	}
}
