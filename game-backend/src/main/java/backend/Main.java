package backend;

import backend.repository.ScoreRepository;
import backend.repository.SessionRepository;
import backend.request.AddScoreRequest;
import backend.request.HighScoresRequest;
import backend.request.LoginRequest;
import backend.server.Server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static backend.server.RequestMapper.GET;
import static backend.server.RequestMapper.POST;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = Integer.valueOf(System.getProperty("port","8081"));

        TimeUnit minutes = TimeUnit.MINUTES;
        long sessionValidityThreshold = minutes.toMillis(10);
        long sessionCleanupFrequency = minutes.toMillis(1);
        int scoreListMaxSize = 15;

        SessionRepository sessionRepository = new SessionRepository(new ConcurrentHashMap<>(),
                sessionValidityThreshold, sessionCleanupFrequency);
        ScoreRepository scoreRepository = new ScoreRepository(new ConcurrentHashMap<>(), scoreListMaxSize);

        GameController controller = new GameController(sessionRepository, scoreRepository);

        Server server = Server.newBuilder().withAddress("localhost").withPort(port)
                .withMapper(GET("/(?<userid>.*)/login", LoginRequest.class, controller::login))
                .withMapper(POST("/(?<levelid>.*)/score", AddScoreRequest.class, controller::score))
                .withMapper(GET("/(?<levelid>.*)/highscorelist", HighScoresRequest.class, controller::highscorelist))
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
    }
}
