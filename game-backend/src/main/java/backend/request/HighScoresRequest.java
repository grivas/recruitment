package backend.request;

import backend.server.Request;

import java.util.Map;

import static backend.util.ParamUtils.asUnsignedInt;

/**
 * Created by germanrivas on 5/7/16.
 */
public class HighScoresRequest implements Request{

    private int levelid;

    public int getLevelid() {
        return levelid;
    }

    @Override
    public void load(String requestBody, Map<String, String> pathParams, Map<String, String> queryParams) {
        levelid = asUnsignedInt(pathParams, "levelid");
    }
}
