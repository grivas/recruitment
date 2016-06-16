package impression.campain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Comparator;

@Getter
@Setter
@ToString
public class CampaignSpecification {
    public static final Comparator<CampaignSpecification> BY_IMPRESSION_UNIT_PRICE = (a, b)->a.priceOfSingleImpression().compareTo(b.priceOfSingleImpression());

    @JsonProperty
    @NotEmpty
    private String customer;
    @JsonProperty
    private int impressions;
    @JsonProperty
    private int price;

    public Double priceOfSingleImpression() {
        return (double) price / impressions;
    }

    public int campaignsFrom(int totalImpressions) {
        return totalImpressions/impressions;
    }
}
