package plantTracker;

import java.util.Date;

public class FruitingPlant extends Plant {

	private String fruit;

	public FruitingPlant() {
		this("NA", "NA", new Date());
	}

	public FruitingPlant(String name, String species, Date date) {
		this(name, species, date, "NA");
	}

	public FruitingPlant(String name, String species, Date date, String fruit) {
		super(name, species, date);
		this.fruit = fruit;
	}

	public void setFruit(String fruit) {
		this.fruit = fruit;
	}

	public String getFruit() {
		return fruit;
	}

}