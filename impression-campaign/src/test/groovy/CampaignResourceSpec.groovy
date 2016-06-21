import impression.campain.ImpressionCampaignApplication
import impression.campain.model.CampaignOffers
import io.dropwizard.Configuration
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.testing.DropwizardTestSupport
import io.dropwizard.testing.ResourceHelpers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.ws.rs.client.Client
import javax.ws.rs.client.Entity

@Unroll
public class CampaignResourceSpec extends Specification {
    @Shared
    DropwizardTestSupport<Configuration> jersey = new DropwizardTestSupport<>(ImpressionCampaignApplication.class,
            ResourceHelpers.resourceFilePath("test-config.yaml"))


    def "When scenario #scenario"(scenario) {
        setup:
        Client client = new JerseyClientBuilder(jersey.getEnvironment()).build("test client");
        def expected = scenario.response
        when:
        def response = client.target("http://localhost:${jersey.localPort}/campaign")
                .request().post(Entity.json(scenario.request))
        def actual = response.readEntity(Map.class)
        then:
        response.status == 200
        actual.totalRevenue == expected.totalRevenue
        actual.totalNumberOfImpressions == expected.totalNumberOfImpressions
        actual.offers.sort() == expected.offers.sort()

        where:
        scenario << [
                [request : [
                        monthlyInventory: 32356000,
                        specifications  : [
                                [customer: "Acme", impressions: 2000000, price: 200],
                                [customer: "Lorem", impressions: 3500000, price: 400],
                                [customer: "Ipsum", impressions: 2300000, price: 210],
                                [customer: "Dolor", impressions: 8000000, price: 730],
                                [customer: "SIT", impressions: 10000000, price: 1000],
                                [customer: "Amet", impressions: 1500000, price: 160],
                                [customer: "Mauris", impressions: 1000000, price: 100]
                        ]],
                 response: [
                         totalNumberOfImpressions: 32000000,
                         totalRevenue            : 3620,
                         offers                  : [
                                 [customer: "Acme", campaigns: 0, impressions: 0, revenue: 0],
                                 [customer: "Lorem", campaigns: 8, impressions: 28000000, revenue: 3200],
                                 [customer: "Ipsum", campaigns: 0, impressions: 0, revenue: 0],
                                 [customer: "Dolor", campaigns: 0, impressions: 0, revenue: 0],
                                 [customer: "SIT", campaigns: 0, impressions: 0, revenue: 0],
                                 [customer: "Amet", campaigns: 2, impressions: 3000000, revenue: 320],
                                 [customer: "Mauris", campaigns: 1, impressions: 1000000, revenue: 100]
                         ]
                 ]],
                [request : [
                        monthlyInventory: 50000000,
                        specifications  : [
                                [customer: "Acme", impressions: 2000000, price: 200],
                                [customer: "Lorem", impressions: 2, price: 2],
                                [customer: "Ipsum", impressions: 3, price: 2],
                                [customer: "Dolor", impressions: 70000, price: 71000],
                                [customer: "Mauris", impressions: 49000000, price: 50000000]
                        ]],
                 response: [
                         totalNumberOfImpressions: 50000000,
                         totalRevenue            : 51014000,
                         offers                  : [
                                 [customer: "Acme", campaigns: 0, impressions: 0, revenue: 0],
                                 [customer: "Lorem", campaigns: 10000, impressions: 20000, revenue: 20000],
                                 [customer: "Ipsum", campaigns: 0, impressions: 0, revenue: 0],
                                 [customer: "Dolor", campaigns: 14, impressions: 980000, revenue: 994000],
                                 [customer: "Mauris", campaigns: 1, impressions: 49000000, revenue: 50000000]
                         ]
                 ]],
                [request : [
                        monthlyInventory: 2000000000,
                        specifications  : [
                                [customer: "Acme", impressions: 1000000, price: 5000],
                                [customer: "Lorem", impressions: 2000000, price: 9000],
                                [customer: "Ipsum", impressions: 3000000, price: 20000]
                        ]],
                 response: [
                         totalNumberOfImpressions: 2000000000,
                         totalRevenue            : 13330000,
                         offers                  : [
                                 [customer: "Acme", campaigns: 2, impressions: 2000000, revenue: 10000],
                                 [customer: "Lorem", campaigns: 0, impressions: 0, revenue: 0],
                                 [customer: "Ipsum", campaigns: 666, impressions: 1998000000, revenue: 13320000]
                         ]
                 ]
                ]
        ]

    }

    void setup() {
        jersey.before();
    }

    void cleanup() {
        jersey.after()
    }
}
