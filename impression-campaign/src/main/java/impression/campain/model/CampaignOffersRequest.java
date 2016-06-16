package impression.campain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Created by germanrivas on 6/10/16.
 */
@Getter
@Setter
@ToString
public class CampaignOffersRequest {
    @JsonProperty
    @Min(1)
    private int monthlyInventory;
    @JsonProperty
    @NotEmpty
    private List<CampaignSpecification> specifications;
}
