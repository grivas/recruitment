package backend.server;

import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static backend.server.Response.badRequest;
import static backend.server.Response.serverError;
import static backend.util.ParamUtils.asString;
import static backend.util.ParamUtils.pathParamsOf;
import static backend.util.ParamUtils.queryParamsOf;


public class RequestMapper<T extends Request> {
    private static final String GET = "GET";
    private static final String POST = "POST";

    private static final Pattern pathParamPattern = Pattern.compile("\\(\\?<(?<paramName>[a-zA-Z][a-zA-Z0-9]*)>");

    Controller<T> controller;
    String method;
    Pattern path;
    List<String> pathParamNames;
    Class<T> requestType;

    public RequestMapper(Class<T> requestType,Controller<T> controller, String method, String path) {
        this.controller = controller;
        this.method = method;
        this.path = Pattern.compile(path);
        Matcher matcher = pathParamPattern.matcher(path);
        this.pathParamNames = new ArrayList<>();
        this.requestType = requestType;
        while (matcher.find()) {
            pathParamNames.add(matcher.group("paramName"));
        }
    }

    public static <T extends Request> RequestMapper<T> GET(String path, Class<T> requestType, Controller<T> controller) {
        return new RequestMapper<>(requestType, controller, GET, path);
    }

    public static <T extends Request> RequestMapper POST(String path, Class<T> requestType, Controller<T> controller) {
        return new RequestMapper<>(requestType, controller, POST, path);
    }

    /**
     * Verifies whether or not the request matches this mapper
     * @param httpExchange
     * @return
     */
    boolean matches(HttpExchange httpExchange) {
        String requestMethod = httpExchange.getRequestMethod();
        String requestPath = httpExchange.getRequestURI().getPath();
        return requestMethod.equals(method) && this.path.matcher(requestPath).matches();
    }

    /**
     * Invokes the controller and returns the response
     * @param httpExchange
     * @return
     */
    Response invokeController(HttpExchange httpExchange) {
        try {
            String requestBody = asString(httpExchange.getRequestBody());
            Map<String, String> pathParams = pathParamsOf(httpExchange.getRequestURI(), path, pathParamNames);
            Map<String, String> queryParams = queryParamsOf(httpExchange.getRequestURI());
            T request = requestType.newInstance();
            request.load(requestBody, pathParams, queryParams);
            return controller.execute(request);
        } catch (InstantiationException | IllegalAccessException e) {
            return serverError("Action class must implement method execute");
        } catch (IllegalArgumentException e){
            return badRequest(e.getMessage());
        }catch (RuntimeException e){
            e.printStackTrace();
            return serverError(e.getMessage());
        }
    }
}
