package backend.server;

import java.util.Map;

/**
 * Created by germanrivas on 5/7/16.
 */
public interface Request {
    void load(String requestBody, Map<String, String> pathParams, Map<String, String> queryParams);
}
