package impression.campain.core;

import impression.campain.model.CampaignOffers;
import impression.campain.model.CampaignSpecification;

import java.util.List;
import java.util.Optional;

/**
 * Created by germanrivas on 6/14/16.
 */
public interface CampaignOfferingAlgorithm {
    /**
     * Creates campaign offers to fulfill a maximum amount of impressions. Campaigns are generated trying to achieve
     * the maximum revenue possible.
     * @param specifications Campaign specifications
     * @param impressions Max number of impressions to be delivered
     * @return Best effort campaigns offering
     */
    CampaignOffers generateCampaignOffers(List<CampaignSpecification> specifications,
                                          int impressions);
}
