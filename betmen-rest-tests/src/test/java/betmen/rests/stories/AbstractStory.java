package betmen.rests.stories;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.CandidateSteps;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.Test;

import java.util.List;

public abstract class AbstractStory extends JUnitStory {

    public AbstractStory() {
        configuredEmbedder()
                .embedderControls()
                .doGenerateViewAfterStories(false)
                .doIgnoreFailureInStories(false)
                .doIgnoreFailureInView(false)
                .doFailOnStoryTimeout(true)
                .useThreads(1);
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryPathResolver(embeddableClass -> String.format("stories/%s.story", embeddableClass.getSimpleName()))
                .useStoryReporterBuilder(new StoryReporterBuilder().withFormats(Format.CONSOLE));
    }

    @Override
    public List<CandidateSteps> candidateSteps() {
        return new InstanceStepsFactory(configuration(), this).createCandidateSteps();
    }

    @Override
    @Test
    public void run() throws Throwable {
        super.run();
    }

    /*@Override
    public Configuration configuration() {
        return new MostUsefulConfiguration().useStoryPathResolver(embeddableClass -> {
            final String storyName = embeddableClass.getSimpleName();
            return String.format("stories/%s.story", storyName);
        });
    }*/

    /*@Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new PortalPageCupsStory());
    }*/
}
