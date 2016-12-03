package betmen.rests.tests;

import betmen.dto.dto.UserDTO;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.CommonEndPointHandler;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class AppRestControllerRestTest {

    @Test
    public void shouldReturnWhomIForAnonymous() {
        // given

        // when
        final UserDTO user = CommonEndPointHandler.whoAmI();

        // then
        assertThat(user, notNullValue());
        assertThat(user.getUserId(), is(0));
        assertThat(user.getUserName(), is(CommonEndPointHandler.WHO_AM_I_ANONYMOUS));
    }

    @Test
    public void shouldReturnWhomIForLoggedUser() {
        // given
        final UserRegData regData = RandomUtils.randomUser();
        final UserDTO userDTO = AuthEndPointsHandler.registerNewUserAndLogin(regData);

        // when
        final UserDTO user = CommonEndPointHandler.whoAmI();

        // then
        assertThat(user, notNullValue());
        assertThat(user.getUserId(), is(userDTO.getUserId()));
        assertThat(user.getUserName(), is(userDTO.getUserName()));
    }
}
