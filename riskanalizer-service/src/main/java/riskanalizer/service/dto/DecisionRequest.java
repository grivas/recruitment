package riskanalizer.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

/**
 * @author german.rivas
 */
public class DecisionRequest {
    @NotEmpty
    @Email
    private String email;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @Min(1)
    private int amount;

    public DecisionRequest() {
    }

    private DecisionRequest(Builder builder) {
        email = builder.email;
        firstName = builder.firstName;
        lastName = builder.lastName;
        amount = builder.amount;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(DecisionRequest copy) {
        Builder builder = new Builder();
        builder.email = copy.email;
        builder.firstName = copy.firstName;
        builder.lastName = copy.lastName;
        builder.amount = copy.amount;
        return builder;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAmount() {
        return amount;
    }

    public static final class Builder {
        private String email;
        private String firstName;
        private String lastName;
        private int amount;

        private Builder() {
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public DecisionRequest build() {
            return new DecisionRequest(this);
        }
    }
}
