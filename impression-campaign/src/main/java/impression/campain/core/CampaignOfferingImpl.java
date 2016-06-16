package impression.campain.core;

import impression.campain.model.CampaignOffer;
import impression.campain.model.CampaignOffers;
import impression.campain.model.CampaignSpecification;

import java.util.ArrayList;
import java.util.List;

import static impression.campain.model.CampaignSpecification.BY_IMPRESSION_UNIT_PRICE;

public class CampaignOfferingImpl implements CampaignOfferingAlgorithm {
    @Override
    public CampaignOffers generateCampaignOffers(List<CampaignSpecification> specifications, int totalNumberOfImpressions) {
        specifications.sort(BY_IMPRESSION_UNIT_PRICE.reversed());
        int impressionsLeft = totalNumberOfImpressions;
        List<CampaignOffer> firstRoundOffers = new ArrayList<>();
        //Creates a list of offer candidates. Each offer will be as high as the maximum number of campaigns -1.
        for (CampaignSpecification specification : specifications) {
            int maxCampaigns = specification.campaignsFrom(impressionsLeft);
            int campaigns = maxCampaigns == 0 || specification.getPrice()==0 ? 0 : maxCampaigns - 1;
            int impressions = campaigns * specification.getImpressions();
            impressionsLeft -= impressions;
            firstRoundOffers.add(CampaignOffer.builder().impressions(impressions)
                    .revenue(campaigns * specification.getPrice())
                    .campaigns(campaigns).customer(specification.getCustomer()).build());
        }

        //Performs a second round using impressions left giving priority to most profitable offer specifications and
        //Aggregates final results.
        int totalImpressions = 0;
        int totalRevenue = 0;
        List<CampaignOffer> finalOffers = new ArrayList<>();
        for (int i = 0; i < specifications.size(); i++) {
            CampaignOffer campaignOffer = firstRoundOffers.get(i);
            CampaignSpecification specification = specifications.get(i);
            int campaigns = specification.campaignsFrom(impressionsLeft);
            int impressions = campaigns * specification.getImpressions();
            impressionsLeft -= impressions;
            CampaignOffer offer = CampaignOffer.builder().impressions(campaignOffer.getImpressions() + impressions)
                    .revenue(campaignOffer.getRevenue() + (campaigns * specification.getPrice()))
                    .campaigns(campaignOffer.getCampaigns() + campaigns)
                    .customer(specification.getCustomer()).build();
            finalOffers.add(offer);
            totalImpressions += offer.getImpressions();
            totalRevenue += offer.getRevenue();
        }

        return CampaignOffers.builder().totalNumberOfImpressions(totalImpressions).totalRevenue(totalRevenue)
                .offers(finalOffers).build();
    }


}
