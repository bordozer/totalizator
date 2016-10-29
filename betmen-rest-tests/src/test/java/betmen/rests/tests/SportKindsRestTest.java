package betmen.rests.tests;

import betmen.dto.dto.SportKindDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.builders.SportKindEditDtoBuilder;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.SportKindEndpointsHandler;
import betmen.rests.utils.helpers.admin.AdminSportEndPointsHandler;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

public class SportKindsRestTest {

    @BeforeMethod
    public void testcaseInit() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldNotAllowToGetSportsListForAnonymousUser() {
        SportKindEndpointsHandler.getSports(ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldNotAllowToGetSportAccessForAnonymousUser() {
        SportKindEndpointsHandler.getSport(0, ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturn422IfWrongSportIdProvided() {
        AuthEndPointsHandler.loginAsAdmin();
        SportKindEndpointsHandler.getSport(-12, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldGetSportsListSortedByName() {
        // given
        DataCleanUpUtils.cleanupAll();

        AuthEndPointsHandler.loginAsAdmin();

        SportKindEditDTO edit1 = AdminSportEndPointsHandler.create(new SportKindEditDtoBuilder()
                .withName(String.format("3-%s", RandomUtils.sportName()))
                .build());
        SportKindEditDTO edit2 = AdminSportEndPointsHandler.create(new SportKindEditDtoBuilder()
                .withName(String.format("1-%s", RandomUtils.sportName()))
                .build());
        SportKindEditDTO edit3 = AdminSportEndPointsHandler.create(new SportKindEditDtoBuilder()
                .withName(String.format("2-%s", RandomUtils.sportName()))
                .build());

        // when
        List<SportKindDTO> sports = SportKindEndpointsHandler.getSports();

        // then
        assertThat(sports, notNullValue());
        assertThat(sports, hasSize(3));
        assertThat(sports.get(0).getSportKindId(), is(edit2.getSportKindId()));
        assertThat(sports.get(1).getSportKindId(), is(edit3.getSportKindId()));
        assertThat(sports.get(2).getSportKindId(), is(edit1.getSportKindId()));

        SportKindDTO created = find(sports, edit1.getSportKindId());
        assertThat(created, notNullValue());
        assertThat(created.getSportKindName(), is(edit1.getSportKindName()));

        // given
        String newName = RandomUtils.UUID();
        edit1.setSportKindName(newName);

        AdminSportEndPointsHandler.update(edit1);

        // when
        List<SportKindDTO> sports1 = SportKindEndpointsHandler.getSports();

        // then
        assertThat(sports1, notNullValue());
        SportKindDTO userDTO1 = find(sports1, edit1.getSportKindId());
        assertThat(userDTO1, notNullValue());
        assertThat(userDTO1.getSportKindName(), is(newName));
    }

    @Test
    public void shouldGetSport() {
        // given
        AuthEndPointsHandler.loginAsAdmin();
        SportKindEditDTO editSport = AdminTestDataGenerator.createSport();

        // when
        SportKindDTO created = SportKindEndpointsHandler.getSport(editSport.getSportKindId());

        // then
        assertThat(created, notNullValue());
        assertThat(created.getSportKindName(), is(editSport.getSportKindName()));

        // given
        String newName = RandomUtils.UUID();
        editSport.setSportKindName(newName);

        // when
        AdminSportEndPointsHandler.update(editSport);
        SportKindDTO updated = SportKindEndpointsHandler.getSport(editSport.getSportKindId());

        // then
        assertThat(updated, notNullValue());
        assertThat(updated.getSportKindName(), is(newName));
    }

    private SportKindDTO find(final List<SportKindDTO> userList, final int sportKindId) {
        return userList.stream().filter(userDto -> userDto.getSportKindId() == sportKindId).findFirst().orElse(null);
    }
}
