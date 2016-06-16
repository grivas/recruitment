package impression.campain;

import com.codahale.metrics.health.HealthCheck;
import impression.campain.core.CampaignOfferingImpl;
import impression.campain.core.CampaignOfferingAlgorithm;
import impression.campain.resource.CampaignResource;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

/**
 * Created by germanrivas on 6/10/16.
 */
public class ImpressionCampaignApplication extends Application<Configuration>{
    public static void main(String[] args) throws Exception {
        new ImpressionCampaignApplication().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        CampaignOfferingAlgorithm campainOfferingAlgorithm = new CampaignOfferingImpl();

        environment.jersey().register(new CampaignResource(campainOfferingAlgorithm));
        environment.healthChecks().register("Default", new HealthCheck(){

            @Override
            protected Result check() throws Exception {
                return HealthCheck.Result.healthy();
            }
        });
    }
}
