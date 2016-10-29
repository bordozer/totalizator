package betmen.core.service.jobs.parameters;

import betmen.core.service.utils.TimePeriod;

public class RemoteGamesImportJobTaskParameters extends AbstractJobTaskParameters {

    private int cupId;
    private TimePeriod timePeriod;

    public int getCupId() {
        return cupId;
    }

    public void setCupId(final int cupId) {
        this.cupId = cupId;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(final TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }
}
