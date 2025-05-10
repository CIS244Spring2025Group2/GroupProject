package util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Properties;

/**
 * ProjUtil loads properties and encrypts passwords
 */
public class ProjUtil {

	private static Properties config = null;

	public static Properties loadConfig() {
		if (config == null) {
			config = new Properties();
			try (InputStream input = ProjUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
				if (input == null) {
					System.err.println("Could not find config.properties in the classpath");
					return null;
				}
				config.load(input);
			} catch (IOException ex) {
				ex.printStackTrace();
				return null;
			}
		}
		return config;
	}

	private static Properties prop = null;

	// gets encryption property
	public static String getSHA(String input) {

		try {
			MessageDigest md = MessageDigest.getInstance(getProperty("sec.sha"));

			byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
			BigInteger number = new BigInteger(1, hash);

			// Convert message digest into hex value
			StringBuilder hex = new StringBuilder(number.toString(16));

			// Padding with zeros
			while (hex.length() < 64) {
				hex.insert(0, '0');
			}

			return hex.toString();
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return null;
	}

	// gets config.properties
	public static String getProperty(String key) {
		Properties cfg = loadConfig(); // Load the config if it's not already loaded
		if (cfg != null) {
			return cfg.getProperty(key);
		}
		return null;
	}
}
