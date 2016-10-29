package betmen.core.model;

public class UserTitle {

    private final String userTitle;
    private final String userTitleIcon;

    public UserTitle(final String userTitle, final String userTitleIcon) {
        this.userTitle = userTitle;
        this.userTitleIcon = userTitleIcon;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public String getUserTitleIcon() {
        return userTitleIcon;
    }
}
