package riskanalizer.service.dto;

import java.util.Objects;

/**
 * @author german.rivas
 */
public class Decision {
    public static final String AMOUNT = "amount";
    public static final String DEBT = "debt";
    public static final String OK = "ok";
    public static final Decision DECISION_REJECT_AMOUNT = Decision.newBuilder().withAccepted(false).withReason(AMOUNT).build();
    public static final Decision DECISION_REJECT_DEBT = Decision.newBuilder().withAccepted(false).withReason(DEBT).build();
    public static final Decision DECISION_ACCEPT_OK = Decision.newBuilder().withAccepted(true).withReason(OK).build();

    private boolean accepted;
    private String reason;

    public Decision() {
    }

    private Decision(Builder builder) {
        accepted = builder.accepted;
        reason = builder.reason;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Decision copy) {
        Builder builder = new Builder();
        builder.accepted = copy.accepted;
        builder.reason = copy.reason;
        return builder;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Decision)) return false;
        Decision decision = (Decision) o;
        return Objects.equals(accepted, decision.accepted) &&
                Objects.equals(reason, decision.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accepted, reason);
    }

    @Override
    public String toString() {
        return "Decision{" +
                "accepted=" + accepted +
                ", reason='" + reason + '\'' +
                '}';
    }

    public static final class Builder {
        private boolean accepted;
        private String reason;

        private Builder() {
        }

        public Builder withAccepted(boolean accepted) {
            this.accepted = accepted;
            return this;
        }

        public Builder withReason(String reason) {
            this.reason = reason;
            return this;
        }

        public Decision build() {
            return new Decision(this);
        }
    }
}
