package betmen.core.service.matches.imports;

import java.util.EnumSet;

public enum GameImportStrategyType {

    NO_IMPORT(0, "Inaccessible", 0)
    , NBA(1, "NBA", 1)
    , UEFA(2, "UEFA", 2)
    , NHL(3, "NHL", 3)
    ;

    final public static EnumSet<GameImportStrategyType> CUP_ID_NEEDED = EnumSet.of(UEFA);

    private final int id;
    private final String name;
    private final int timePeriodType;

    GameImportStrategyType(int id, String name, final int timePeriodType) {
        this.id = id;
        this.name = name;
        this.timePeriodType = timePeriodType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTimePeriodType() {
        return timePeriodType;
    }

    public static GameImportStrategyType getById(final int id) {

        for (final GameImportStrategyType type : GameImportStrategyType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        throw new IllegalArgumentException(String.format("Unsupported GameImportStrategyType ID: %d", id));
    }
}
