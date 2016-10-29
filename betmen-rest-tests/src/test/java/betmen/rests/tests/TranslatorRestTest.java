package betmen.rests.tests;

import betmen.dto.dto.TranslationDTO;
import betmen.rests.utils.helpers.TranslatorEndPointHelper;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class TranslatorRestTest {

    private static final String KEY_1 = "administration";
    private static final String KEY_2 = "portalPage";
    private static final String NERD_1 = "Administration";
    private static final String NERD_2 = "Portal page";

    @Test
    public void shouldTranslateForAnonymousUSer() {
        Map<String, String> map = new HashMap<>();
        map.put(KEY_1, NERD_1);
        map.put(KEY_2, NERD_2);

        TranslationDTO dto = new TranslationDTO();
        dto.setTranslations(map);

        TranslationDTO res = TranslatorEndPointHelper.translate(dto);
        assertThat(res, notNullValue());
        assertThat(res.getTranslations(), notNullValue());
        assertThat(res.getTranslations().keySet(), hasSize(2));
        assertThat(res.getTranslations().get(KEY_1), is("Admin"));
        assertThat(res.getTranslations().get(KEY_2), is("Portal page"));
    }
}
