package backend;

import backend.entity.Session;
import backend.entity.UserScore;
import backend.repository.ScoreRepository;
import backend.repository.SessionRepository;
import backend.request.AddScoreRequest;
import backend.request.HighScoresRequest;
import backend.request.LoginRequest;
import backend.server.Response;

import java.util.UUID;

import static backend.server.Response.forbidden;
import static backend.server.Response.ok;
import static backend.server.Response.serverError;
import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.joining;

/**
 * Created by germanrivas on 5/7/16.
 */
public class GameController {
    private final SessionRepository sessionRepository;
    private final ScoreRepository scoreRepository;

    public GameController(SessionRepository sessionRepository, ScoreRepository scoreRepository) {
        this.sessionRepository = sessionRepository;
        this.scoreRepository = scoreRepository;
    }

    public Response login(LoginRequest request) {
        Session session = Session.newBuilder().withCreated(now()).withUserId(request.getUserid()).build();
        String sessionKey = UUID.randomUUID().toString();
        sessionRepository.create(sessionKey, session);
        return ok(sessionKey);
    }

    public Response score(AddScoreRequest request) {
        String sessionkey = request.getSessionkey();
        return sessionRepository.findBy(sessionkey).map(session -> {
            int userId = session.getUserId();
            int levelid = request.getLevelid();
            int score = request.getScore();
            boolean success = scoreRepository.register(userId, levelid, score);
            return success ? ok("") : serverError(String.format("Failed not update score of user %d on level %d to %d",
                    userId, levelid, score));
        }).orElse(forbidden("Session not found"));
    }

    public Response highscorelist(HighScoresRequest request) {
        return scoreRepository.highestScores(request.getLevelid()).map(scores -> {
            String responseBody = scores.stream().map(UserScore::print).collect(joining(","));
            return ok(responseBody);
        }).orElse(ok(""));
    }
}
