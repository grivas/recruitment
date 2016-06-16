package backend

import backend.entity.Session
import backend.entity.UserScore
import backend.repository.ScoreRepository
import backend.repository.SessionRepository
import backend.request.AddScoreRequest
import backend.request.HighScoresRequest
import backend.request.LoginRequest
import spock.lang.Specification

import static java.util.Optional.empty
import static java.util.Optional.of

public class GameControllerSpec  extends Specification {
    def "When session #existText and score #savedText response should be #responseCode"(){
        def sessionRepository = Mock(SessionRepository)
        def scoreRepository = Mock(ScoreRepository)
        def sessionKey = UUID.randomUUID().toString()
        def userid = 1
        def levelid = 2
        def score = 30

        setup:
        def session = sessionExist?of(Session.newBuilder().withUserId(userid).build()):empty()
        GameController gameController = new GameController(sessionRepository, scoreRepository);

        when:
        def request = new AddScoreRequest()
        request.load("$score".toString(),[levelid: "$levelid".toString()],[sessionkey: sessionKey])
        def result = gameController.score(request)

        then:
        result.code == responseCode
        1 * sessionRepository.findBy(sessionKey) >> session
        if(sessionExist) 1 * scoreRepository.register(userid,levelid,score) >> scoreSaved

        where:
        sessionExist|scoreSaved |responseCode
        false       |_          |403
        true        |false      |500
        true        |true       |200
    }

    def "When #highscores returned list should be #returnedList"(){
        def scoreRepository = Mock(ScoreRepository)
        def highscoresOptional = highscores != null ? of(highscores.collect() {
            userid, score -> UserScore.newBuilder().withUserid(userid).withScore(score).build()
        }) : empty()
        def levelid = 2
        setup:
        GameController gameController = new GameController(Mock(SessionRepository), scoreRepository);
        when:
        def request = new HighScoresRequest()
        request.load(null,[levelid: "$levelid".toString()],null)
        def response = gameController.highscorelist(request)
        then:
        response.body == returnedList
        1 * scoreRepository.highestScores(levelid) >> highscoresOptional
        where:
        highscores                                  |returnedList
        null                                        |""
        []                                          |""
        [1:30]                                      |"1=30"
        [1:30,2:28,4:18,5:12,10:7,3:5]              |"1=30,2=28,4=18,5=12,10=7,3=5"
    }

    def "Login"(){
        def sessionRepository = Mock(SessionRepository)
        def scoreRepository = Mock(ScoreRepository)
        given:
        GameController gameController = new GameController(sessionRepository, scoreRepository);
        when:
        LoginRequest request = new LoginRequest()
        request.load(null,[userid: "1"],null)
        def response = gameController.login(request)
        then:
        response.code == 200
        !response.body.isEmpty()
    }
}
