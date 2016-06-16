package backend.repository;

import backend.entity.UserScore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import static java.lang.Integer.valueOf;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class ScoreRepository {
    private static final int RETRY_THRESHOLD = 5;
    private final Map<Integer, List<UserScore>> highestScoresPerLevelAndUser;
    private final int scoreListMaxSize;

    /**
     * @param highestScoresPerLevel stores a list with the highest scores of a level.
     * @param scoreListMaxSize sets the size limit of the list of high scores per level.
     */
    public ScoreRepository(ConcurrentMap<Integer, List<UserScore>> highestScoresPerLevel, int scoreListMaxSize) {
        this.highestScoresPerLevelAndUser = highestScoresPerLevel;
        this.scoreListMaxSize = scoreListMaxSize;
    }

    /**
     * Registers a new score ensuring that:
     * <ul>
     * <li>The list of high scores is sorted by score is descending order</li>
     * <li>The size of high scores' list never exceeds scoreListMaxSize</li>
     * </ul>
     * @param userId to which the score belongs to.
     * @param levelid to which the score belongs to
     * @param score to register.
     * @return
     */
    public boolean register(int userId, int levelid, int score) {
        return retry(RETRY_THRESHOLD,()->{
            synchronized (valueOf(levelid)){
                highestScoresPerLevelAndUser.putIfAbsent(levelid, new ArrayList<>());
                List<UserScore> currentScores = highestScoresPerLevelAndUser.get(levelid);

                Map<Integer, Integer> currentScoreMap = currentScores.stream()
                        .collect(toMap(UserScore::getUserid, UserScore::getScore));
                if(!currentScoreMap.containsKey(userId)||currentScoreMap.get(userId)<score){
                    currentScoreMap.put(userId,score);
                }

                Comparator<UserScore> byScore = (x, y) -> Integer.compare(x.getScore(), y.getScore());
                List<UserScore> sortedScores = currentScoreMap.entrySet().stream()
                        .map(entry->UserScore.newBuilder().withUserid(entry.getKey()).withScore(entry.getValue()).build())
                        .sorted(byScore.reversed())
                        .limit(scoreListMaxSize).collect(toList());

                return highestScoresPerLevelAndUser.replace(levelid, currentScores, sortedScores);
            }
        });
    }

    /**
     * Tries and action until it succeeds or exceeds a limit of times.
     * @param times max number of tries
     * @param action to be executed
     * @return whether the operation succeeded or not
     */
    public boolean retry(int times, Supplier<Boolean> action){
        int tries = 0;
        boolean result = action.get();
        while (!result && tries<times){
            tries++;
        }
        if (!result){
            System.out.println("Max retries exceeded");
        }
        return result;
    }

    /**
     * @param levelid of the list of scores
     * @return A optional that might contain a descending list of scores or empty if no scores where found.
     */
    public Optional<List<UserScore>> highestScores(int levelid) {
        if(highestScoresPerLevelAndUser.containsKey(levelid))
            return of(highestScoresPerLevelAndUser.get(levelid));
        return empty();
    }
}
