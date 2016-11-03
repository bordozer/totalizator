package betmen.core.service.admin;

import betmen.core.entity.Cup;
import betmen.core.service.CupService;
import betmen.core.service.CupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class AdminMnBServiceImpl implements AdminMnBService {

    @Autowired
    private CupService cupService;

    @Override
    public List<Cup> loadAllCups() {
        final List<Cup> result = newArrayList();
        result.addAll(sortDesc(cupService.loadPublic()));
        result.addAll(sortDesc(cupService.loadHidden()));
        return result;
    }

    @Override
    public List<Cup> loadAllCurrentCups() {
        final List<Cup> result = newArrayList();
        result.addAll(sortDesc(cupService.loadPublicCurrent()));
        result.addAll(sortDesc(cupService.loadHiddenCurrent()));
        return result;
    }

    private List<Cup> sortAsc(final List<Cup> cups) {
        Comparator<Cup> sortByCupBeginningImeDesc = CupServiceImpl.SORT_BY_CUP_BEGINNING_TIME_ASC;
        return sort(cups, sortByCupBeginningImeDesc);
    }

    private List<Cup> sortDesc(final List<Cup> cups) {
        Comparator<Cup> sortByCupBeginningImeDesc = CupServiceImpl.SORT_BY_CUP_BEGINNING_TIME_DESC;
        return sort(cups, sortByCupBeginningImeDesc);
    }

    private List<Cup> sort(final List<Cup> cups, final Comparator<Cup> sortByCupBeginningImeDesc) {
        return cups.stream().sorted(sortByCupBeginningImeDesc).collect(Collectors.toList());
    }
}
