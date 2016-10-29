package betmen.web.controllers.rest.admin.jobs.parameters;

import com.google.gson.Gson;

public abstract class AbstractJobTaskParametersDTO {

    @Override
    public String toString() {
        return new Gson().toJson(this, this.getClass());
    }
}
