package impression.campain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Deque;
import java.util.List;
import java.util.Set;

/**
 * Created by germanrivas on 6/10/16.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CampaignOffers {
    @JsonProperty
    private int totalNumberOfImpressions;
    @JsonProperty
    private int totalRevenue;
    @JsonProperty
    private List<CampaignOffer> offers;
}
