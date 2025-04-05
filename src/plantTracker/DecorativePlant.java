package plantTracker;

import java.util.Date;

public class DecorativePlant extends Plant {

	DecorativePlant() {
		this("N/A", "N/A", new Date());
	}

	DecorativePlant(String name, String species, Date date) {
		super(name, species, date);
	}

}
