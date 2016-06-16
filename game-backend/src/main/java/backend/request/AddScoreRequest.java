package backend.request;

import backend.server.Request;
import backend.util.ParamUtils;

import java.util.Map;

import static backend.util.ParamUtils.asUnsignedInt;
import static backend.util.ParamUtils.notNull;

/**
 * Created by germanrivas on 5/7/16.
 */
public class AddScoreRequest implements Request{

    private int levelid;
    private String sessionkey;
    private int score;

    public int getLevelid() {
        return levelid;
    }

    public int getScore() {
        return score;
    }

    public String getSessionkey() {
        return sessionkey;
    }

    @Override
    public void load(String requestBody, Map<String, String> pathParams, Map<String, String> queryParams) {
        levelid = asUnsignedInt(pathParams, "levelid");
        sessionkey = notNull(queryParams, "sessionkey");
        score = ParamUtils.asUnsignedInt(requestBody, "score");
    }
}
