package backend.entity;

public class UserScore{
    private final int userid;
    private final int score;

    private UserScore(Builder builder) {
        score = builder.score;
        userid = builder.userid;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int getScore() {
        return score;
    }

    public int getUserid() {
        return userid;
    }

    public String print(){
        return String.format("%d=%d",userid, score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserScore userScore = (UserScore) o;

        if (userid != userScore.userid) return false;
        return score == userScore.score;

    }

    public static final class Builder {
        private int score;
        private int userid;

        private Builder() {
        }

        public Builder withScore(int val) {
            score = val;
            return this;
        }

        public Builder withUserid(int val) {
            userid = val;
            return this;
        }

        public UserScore build() {
            return new UserScore(this);
        }
    }
}
