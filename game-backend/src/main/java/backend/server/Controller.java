package backend.server;

@FunctionalInterface
public interface Controller<T> {
    Response execute(T request);
}
