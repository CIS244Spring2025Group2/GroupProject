package plantTracker;

import java.util.Date;

public class FloweringPlant extends Plant {

	FloweringPlant() {
		this("NA", "NA", new Date());
	}

	FloweringPlant(String name, String species, Date date) {
		super(name, species, date);
	}

}
