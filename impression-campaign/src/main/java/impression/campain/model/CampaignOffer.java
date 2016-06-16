package impression.campain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by germanrivas on 6/10/16.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CampaignOffer {
    @JsonProperty
    private String customer;
    @JsonProperty
    private int campaigns;
    @JsonProperty
    private int impressions;
    @JsonProperty
    private int revenue;
}
