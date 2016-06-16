package backend.request;

import backend.server.Request;

import java.util.Map;

import static backend.util.ParamUtils.asUnsignedInt;

/**
 * Created by germanrivas on 5/7/16.
 */
public class LoginRequest implements Request{
    int userid;

    public int getUserid() {
        return userid;
    }

    @Override
    public void load(String requestBody, Map<String, String> pathParams, Map<String, String> queryParams) {
        userid = asUnsignedInt(pathParams, "userid");
    }
}
