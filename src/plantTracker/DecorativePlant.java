package plantTracker;

import java.util.Date;

public class DecorativePlant extends Plant {
	private Date datePlanted;

	DecorativePlant() {
		this("N/A", "N/A");
	}

	DecorativePlant(String name, String species) {
		super(name, species);
	}

}
