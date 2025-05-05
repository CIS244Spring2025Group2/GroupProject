package plantTracker.model;

import java.util.Date;

public class DecorativePlant extends Plant {

	public DecorativePlant() {
		this("N/A", "N/A", new Date());
	}

	public DecorativePlant(String name, String species, Date date) {
		super(name, species, date);
	}

}
