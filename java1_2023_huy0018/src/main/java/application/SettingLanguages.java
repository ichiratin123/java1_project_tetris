package application;

import java.util.Locale;

public class SettingLanguages {
	private static Locale currentLocale = Locale.ENGLISH;

	public static Locale getCurrentLocale() {
		return currentLocale;
	}

	public static void setCurrentLocale(Locale locale) {
		currentLocale = locale;
	}
}
