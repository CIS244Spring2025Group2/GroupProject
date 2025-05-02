package plantTracker.model;

import java.util.Date;

public class Herb extends Plant {

	Herb() {
		this("N/A", "N/A", new Date());
	}

	public Herb(String name, String species, Date date) {
		super(name, species, date);
	}

}
