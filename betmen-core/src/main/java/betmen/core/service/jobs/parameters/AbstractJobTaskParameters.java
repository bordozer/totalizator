package betmen.core.service.jobs.parameters;

import com.google.gson.Gson;

public class AbstractJobTaskParameters {

    @Override
    public String toString() {
        return new Gson().toJson(this, this.getClass());
    }
}
