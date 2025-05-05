package util;

import java.util.function.UnaryOperator;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

public class IntegerTextField extends TextField {

	public IntegerTextField() {
		UnaryOperator<TextFormatter.Change> integerFilter = change -> {
			String newText = change.getControlNewText();
			if (newText.matches("-?([1-9][0-9]*)?")) { // Allows optional negative sign and digits
				return change;
			}
			return null; // Discard the change
		};

		this.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
	}

	public void setValue(int value) {
		this.setText(String.valueOf(value));
	}

	public Integer getValue() {
		try {
			return Integer.parseInt(this.getText());
		} catch (NumberFormatException e) {
			return null; // Or a default value, or handle the error
		}
	}
}
