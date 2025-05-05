package plantTracker.model;

import java.util.Date;

public class FloweringPlant extends Plant {

	public FloweringPlant() {
		this("NA", "NA", new Date());
	}

	public FloweringPlant(String name, String species, Date date) {
		super(name, species, date);
	}

}
