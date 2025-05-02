package plantTracker.model;

import java.util.Date;

public class Vegetable extends Plant {
	private String vegetable;

	public Vegetable() {
		this("NA", "NA", new Date());
	}

	public Vegetable(String name, String species, Date date) {
		this(name, species, date, "NA");
	}

	public Vegetable(String name, String species, Date date, String vegetable) {
		super(name, species, date);
		this.vegetable = vegetable;
	}

	public String getVegetable() {
		return vegetable;
	}

	public void setVegetable(String vegetable) {
		this.vegetable = vegetable;
	}
}
