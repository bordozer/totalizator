package betmen.rests.common;

public class RestTestUser {
    private int userId;

    public RestTestUser(final int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(final int userId) {
        this.userId = userId;
    }
}
