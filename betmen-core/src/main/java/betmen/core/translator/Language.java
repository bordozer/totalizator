package betmen.core.translator;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public enum Language {

    NERD(1, "nerd", "Language: Nerd", "nerd.png", ""), RU(3, "ru", "Language: Russian", "ru.png", "ru"), UA(4, "ua", "Language: Ukrainian", "ua.png", "uk"), EN(2, "en", "Language: English", "en.png", "en");

    private final int id;
    private final String code;
    private final String name;
    private final String icon;
    private final String country;

    private Language(final int id, final String code, final String name, final String icon, final String country) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.icon = icon;
        this.country = country;
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

    public String getCountry() {
        return country;
    }

    public static Language getById(final int id) {
        for (final Language language : values()) {
            if (language.getId() == id) {
                return language;
            }
        }

        throw new IllegalArgumentException(String.format("Illegal language id: %d", id));
    }

    public static Language getByCode(final String code) {

        for (final Language language : values()) {
            if (language.getCode().equalsIgnoreCase(code)) {
                return language;
            }
        }

//		throw new IllegalArgumentException( String.format( "Illegal language code: %s", code ) );
        return null;
    }

    public static List<Language> getUILanguages() {

        final List<Language> languages = newArrayList();

        for (final Language language : values()) {
            if (language != NERD) {
                languages.add(language);
            }
        }

        return languages;
    }
}
