package totalizator.app.translator;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public enum Language {

	NERD( 1, "nerd", "Language: Nerd", "nerd.png" )
	, RU( 3, "ru", "Language: Russian", "ru.png" )
	, UA( 4, "ua", "Language: Ukrainian", "ua.png" )
	, EN( 2, "en", "Language: English", "en.png" )
	;

	private final int id;
	private final String code;
	private final String name;
	private final String icon;

	private Language( final int id, final String code, final String name, final String icon ) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.icon = icon;
	}

	public int getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return icon;
	}

	public static Language getById( final int id ) {
		for ( final Language language : values() ) {
			if ( language.getId() == id ) {
				return language;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal language id: %d", id ) );
	}

	public static Language getByCode( final String code ) {
		for ( final Language language : values() ) {
			if ( language.getCode().equalsIgnoreCase( code ) ) {
				return language;
			}
		}

		throw new IllegalArgumentException( String.format( "Illegal language code: %s", code ) );
	}

	public static List<Language> getUILanguages() {

		final List<Language> languages = newArrayList();

		for ( final Language language : values() ) {
			if ( language != NERD ) {
				languages.add( language );
			}
		}

		return languages;
	}
}
