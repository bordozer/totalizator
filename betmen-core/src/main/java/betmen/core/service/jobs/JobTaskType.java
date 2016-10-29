package betmen.core.service.jobs;

public enum JobTaskType {

    IMPORT_REMOTE_GAMES(1, "JobTaskType: Remote games import");

    private final int id;
    private final String name;

    JobTaskType(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static JobTaskType getById(final int id) {

        for (final JobTaskType activityStreamEntryType : JobTaskType.values()) {
            if (activityStreamEntryType.getId() == id) {
                return activityStreamEntryType;
            }
        }

        return null;
    }
}
