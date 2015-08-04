package totalizator.app.controllers.rest.app;

public class LanguageDTO {

	private final String name;
	private final String country;

	public LanguageDTO( final String name, final String country ) {
		this.name = name;
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public String getCountry() {
		return country;
	}

	@Override
	public String toString() {
		return String.format( "%s: %s", name, country );
	}
}
