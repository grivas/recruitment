package impression.campain.resource;

import com.codahale.metrics.annotation.Timed;
import impression.campain.core.CampaignOfferingAlgorithm;
import impression.campain.model.CampaignOffersRequest;
import impression.campain.model.CampaignOffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/campaign")
@Produces(MediaType.APPLICATION_JSON)
public class CampaignResource {
    private static final Logger logger = LoggerFactory.getLogger(CampaignResource.class);

    private final CampaignOfferingAlgorithm campainOfferingAlgorithm;

    public CampaignResource(CampaignOfferingAlgorithm campainOfferingAlgorithm) {
        this.campainOfferingAlgorithm = campainOfferingAlgorithm;
    }

    @POST
    @Timed
    public CampaignOffers calculateCampaignOffers(CampaignOffersRequest request) {
        return campainOfferingAlgorithm.generateCampaignOffers(request.getSpecifications(), request.getMonthlyInventory());
    }

}
