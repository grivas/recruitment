package backend.repository

import backend.entity.UserScore
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.ConcurrentHashMap

public class ScoreRepositorySpec extends Specification {
    @Unroll
    def """When registered scores are #userScores, the list of high scores
           should be #highScores"""(){
        setup:
        def levelid = 1;
        ScoreRepository scoreRepository = new ScoreRepository(new ConcurrentHashMap<Integer, List<UserScore>>(),5)
        def expectedHighScores = highScores.collect {
            userid, score -> UserScore.newBuilder().withUserid(userid).withScore(score).build()
        }
        when:
        userScores.each{
            userid, score -> scoreRepository.register(userid,levelid,score)
        }
        def actualHighscoreList = scoreRepository.highestScores(levelid).orElse(new ArrayList<UserScore>())

        then:
        actualHighscoreList == expectedHighScores
        where:
        userScores                                  |highScores
        []                                          |[]
        [1:20,1:30]                                 |[1:30]
        [1:20,2:30]                                 |[2:30,1:20]
        [1:15,2:30,3:20,4:5,5:10]                   |[2:30,3:20,1:15,5:10,4:5]
        [1:15,2:30,3:20,4:5,5:10,6:50,7:25]         |[6:50,2:30,7:25,3:20,1:15]
    }

    def "Concurrency test"(){
        def highScoreListMaxLength = 15
        setup:
        ScoreRepository scoreRepository = new ScoreRepository(new ConcurrentHashMap<Integer, List<UserScore>>(), highScoreListMaxLength)
        def levelid = 1;
        def limit = 10000
        def expected = (0..<highScoreListMaxLength).collect() {
            userid -> UserScore.newBuilder().withUserid(userid).withScore(limit - userid).build()
        }
        when:
        (0..limit).parallelStream().forEach{
            userid ->
                scoreRepository.register(userid,levelid, limit-userid)
        }
        def highScores = scoreRepository.highestScores(levelid).get()
        then:
        highScores.size() == highScoreListMaxLength
        highScores == expected
    }
}
