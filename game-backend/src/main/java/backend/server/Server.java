package backend.server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import static backend.server.Response.notFound;

public class Server {
    private List<RequestMapper> mappers;
    private String address;
    private int port;

    private HttpServer httpServer;
    private HttpContext httpContext;

    private Server(Builder builder) {
        address = builder.address;
        mappers = builder.mappers;
        port = builder.port;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Starts the http server with a single context handler for all possible request.
     * @throws IOException
     */
    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(address, port), 0);
        httpContext = httpServer.createContext("/", this::requestProxy);
        httpServer.start();
        System.out.println(String.format("Running game backend on port %d",port));
    }

    /**
     * Finds a suitable request mapper and invokes the controller, sending the result back to the client
     * @param httpExchange
     * @throws IOException
     */
    void requestProxy(HttpExchange httpExchange) throws IOException {
        Response response = mappers.stream()
                .filter(mapper -> mapper.matches(httpExchange))
                .findFirst()
                .map(mapper -> mapper.invokeController(httpExchange))
                .orElse(notFound());
        httpExchange.sendResponseHeaders(response.getCode(), response.getBody().length());
        try (OutputStream out = httpExchange.getResponseBody()) {
            out.write(response.getBody().getBytes());
        }
    }

    /**
     * Removes context and stops service
     */
    public void shutdown() {
        System.out.println("Shutting server down!");
        httpServer.removeContext(httpContext);
        httpServer.stop(1);
        System.out.println("Good bye!");
    }

    public static final class Builder {
        private String address;
        private List<RequestMapper> mappers = new ArrayList<>();
        private int port;

        private Builder() {
        }

        public Builder withAddress(String val) {
            address = val;
            return this;
        }

        public Builder withMapper(RequestMapper mapper) {
            mappers.add(mapper);
            return this;
        }

        public Builder withPort(int val) {
            port = val;
            return this;
        }

        public Server build() {
            return new Server(this);
        }
    }
}
