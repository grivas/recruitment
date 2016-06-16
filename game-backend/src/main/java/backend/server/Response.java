package backend.server;

/**
 * Created by germanrivas on 5/3/16.
 */
public class Response{


    private static final int HTTP_OK = 200;
    private static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_FORBIDDEN = 403;

    private int code;
    private String body;

    public Response(int code, String body) {
        this.body = body;
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public int getCode() {
        return code;
    }

    public static Response ok(String body){
        return new Response(HTTP_OK, body);
    }

    public static Response badRequest(String body){
        return new Response(HTTP_BAD_REQUEST, body);
    }

    public static Response forbidden(String body){
        return new Response(HTTP_FORBIDDEN, body);
    }

    public static Response notFound(){
        return new Response(HTTP_NOT_FOUND, "Not found");
    }

    public static Response serverError(String body){
        return new Response(HTTP_INTERNAL_SERVER_ERROR, body);
    }
}
