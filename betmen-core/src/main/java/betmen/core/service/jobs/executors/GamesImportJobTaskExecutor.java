package betmen.core.service.jobs.executors;

import betmen.core.entity.Cup;
import betmen.core.service.CupService;
import betmen.core.service.jobs.JobTask;
import betmen.core.service.jobs.parameters.RemoteGamesImportJobTaskParameters;
import betmen.core.service.jobs.results.GamesImportJobLogResultJSON;
import betmen.core.service.jobs.results.ImportedRemoteGame;
import betmen.core.service.matches.imports.ImportGameResult;
import betmen.core.service.matches.imports.RemoteGame;
import betmen.core.service.matches.imports.RemoteGameDataImportService;
import betmen.core.service.utils.DateRangeService;

import java.time.LocalDateTime;
import java.util.Set;

public class GamesImportJobTaskExecutor extends AbstractJobTaskExecutor {

    private RemoteGameDataImportService remoteGameDataImportService;
    private CupService cupService;
    private DateRangeService dateRangeService;

    public GamesImportJobTaskExecutor(final JobTask jobTask, final LocalDateTime jobTaskInternalTime) {
        super(jobTask, jobTaskInternalTime);
    }

    @Override
    public void execute() {

        final GamesImportJobLogResultJSON jobLogResult = (GamesImportJobLogResultJSON) this.jobLogResultJSON;

        final RemoteGamesImportJobTaskParameters parametersHolder = (RemoteGamesImportJobTaskParameters) jobTask.getJobTaskParameters();

        final Cup cup = cupService.load(parametersHolder.getCupId());

        try {
            final Set<RemoteGame> remoteGames = remoteGameDataImportService.preloadRemoteGames(dateRangeService.getDateFrom(parametersHolder.getTimePeriod(), jobTaskInternalTime), dateRangeService.getDateTo(parametersHolder.getTimePeriod(), jobTaskInternalTime), cup);

            setTotal(remoteGames.size());

            for (final RemoteGame remoteGame : remoteGames) {

                if (isStoppedByAnyReason()) {
                    return;
                }

                if (!remoteGame.isLoaded()) {
                    remoteGameDataImportService.loadRemoteGame(remoteGame, cup);
                }

                final ImportGameResult importGameResult = importGame(cup, remoteGame);

                final ImportedRemoteGame importedRemoteGame = new ImportedRemoteGame(remoteGame, importGameResult.getMatchId(), importGameResult.isMatchCreated());

                jobLogResult.addImportedRemoteGame(importedRemoteGame);
            }

            executionStateFinished();


        } catch (final Exception e) {
            // TODO: report about error
            executionStateError();
        } finally {
            afterJobDone();
        }
    }

    public void setRemoteGameDataImportService(final RemoteGameDataImportService remoteGameDataImportService) {
        this.remoteGameDataImportService = remoteGameDataImportService;
    }

    public void setCupService(final CupService cupService) {
        this.cupService = cupService;
    }

    public void setDateRangeService(final DateRangeService dateRangeService) {
        this.dateRangeService = dateRangeService;
    }

    private ImportGameResult importGame(final Cup cup, final RemoteGame remoteGame) {

        final ImportGameResult importGameResult = remoteGameDataImportService.importGame(cup, remoteGame);

        increase();

        return importGameResult;
    }
}
