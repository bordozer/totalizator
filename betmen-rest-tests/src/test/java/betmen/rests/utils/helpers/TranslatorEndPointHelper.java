package betmen.rests.utils.helpers;

import betmen.dto.dto.TranslationDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.SystemRoutes;
import betmen.rests.utils.RestTestConstants;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TranslatorEndPointHelper {

    public static TranslationDTO translate(final TranslationDTO dto) {
        return RequestHelper.doGet(SystemRoutes.TRANSLATOR, params(dto), ResponseStatus.OK.getCode()).as(TranslationDTO.class);
    }

    /**
     * translations[key1]=nerd1&translations[key2]=nerd2
     */
    private static Map<String, Object> params(final TranslationDTO dto) {
        Map<String, String> map = dto.getTranslations();
        List<String> parameters = map.keySet().stream().map(key -> String.format("translations[%s]=%s", key, map.get(key))).collect(Collectors.toList());
        return Collections.singletonMap(RestTestConstants.TRANSLATIONS, StringUtils.join(parameters, "&"));
    }
}
