package backend.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalUnit;

import static java.time.LocalDateTime.now;

public class Session {
    private static final TemporalUnit MILLIS = ChronoField.MILLI_OF_SECOND.getBaseUnit();
    private final int userId;
    private final LocalDateTime created;

    private Session(Builder builder) {
        created = builder.created;
        userId = builder.userId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int getUserId() {
        return userId;
    }

    public boolean isValid(long validityThreshold){
        return now().minus(validityThreshold, MILLIS).isBefore(created);
    }

    public static final class Builder {
        private LocalDateTime created;
        private int userId;

        private Builder() {
        }

        public Builder withCreated(LocalDateTime val) {
            created = val;
            return this;
        }

        public Builder withUserId(int val) {
            userId = val;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }
}
