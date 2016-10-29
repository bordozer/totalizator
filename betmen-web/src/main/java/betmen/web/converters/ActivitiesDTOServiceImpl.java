package betmen.web.converters;

import betmen.dto.dto.ActivityStreamDTO;
import betmen.core.entity.User;
import betmen.core.entity.activities.AbstractActivityStreamEntry;
import betmen.core.entity.activities.ActivityStreamEntryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class ActivitiesDTOServiceImpl implements ActivitiesDTOService {

    @Autowired
    private ActivityDTOService activityDTOService;

    @Override
    public List<ActivityStreamDTO> transformActivities(final List<AbstractActivityStreamEntry> activities, final User currentUser) {
        return transformActivities(activities, currentUser, newArrayList());
    }

    @Override
    public List<ActivityStreamDTO> transformActivities(final List<AbstractActivityStreamEntry> activities, final User currentUser, final List<ActivityStreamEntryType> excludedTypes) {

        return activities
                .stream()
                .map(activity -> {
                    if (excludedTypes.contains(activity.getActivityStreamEntryType())) {
                        return null;
                    }
                    return activityDTOService.transformActivity(activity, currentUser);
                })
                .filter(activityStreamDTO -> activityStreamDTO != null)
                .collect(Collectors.toList());
    }
}
