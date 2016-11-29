package betmen.core.entity;

import org.testng.annotations.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CupEntityTest {

    @Test
    public void twoCupsShouldBeEqualsIfTheirIdsAreTheSame() {
        // given
        SportKind sportKind1 = new SportKind();
        sportKind1.setSportKindName("Basket");

        Category category1 = new Category();
        category1.setId(15);
        category1.setCategoryName("The league");
        category1.setSportKind(sportKind1);

        PointsCalculationStrategy strategy = new PointsCalculationStrategy();
        strategy.setId(12);
        strategy.setStrategyName("Soccer strategy");

        Cup cup1 = new Cup();
        cup1.setId(3);
        cup1.setCategory(category1);
        cup1.setCupName("One");
        cup1.setCupImportId("IMP-ID");
        cup1.setCupStartTime(LocalDateTime.now());
        cup1.setPublicCup(true);
        cup1.setWinnersCount(2);
        cup1.setPointsCalculationStrategy(strategy);

        Cup cup2 = new Cup();
        cup2.setId(3);
        cup2.setCupName("Another");

        // then
        assertThat(cup1, is(cup2));
    }
}