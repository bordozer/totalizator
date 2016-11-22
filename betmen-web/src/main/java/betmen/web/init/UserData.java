package betmen.web.init;

import lombok.Getter;

@Getter
public class UserData {
    private final String firstName;
    private final String lastName;
    private final String thirdName;

    UserData(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.thirdName = "";
    }

    UserData(final String firstName, final String lastName, final String thirdName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.thirdName = thirdName;
    }
}
