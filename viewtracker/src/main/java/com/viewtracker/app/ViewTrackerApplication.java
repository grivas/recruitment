package com.viewtracker.app;

import com.viewtracker.repository.ViewTrackerRepository;
import com.viewtracker.resources.ViewTrackerResource;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;


/**
 * Created by gerriv on 28/08/2014.
 */
public class ViewTrackerApplication extends Application<ViewTrackerConfiguration> {
    public static void main(String[] args) throws Exception {
        new ViewTrackerApplication().run(args);
    }

    @Override
    public String getName() {
        return "view-tracker";
    }

    @Override
    public void initialize(Bootstrap<ViewTrackerConfiguration> bootstrap) {

    }

    @Override
    public void run(ViewTrackerConfiguration configuration,
                    Environment environment) throws ClassNotFoundException {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final ViewTrackerRepository repository = jdbi.onDemand(ViewTrackerRepository.class);
        setUpDb(repository);
        environment.jersey().register(new ViewTrackerResource(repository));
    }

    private void setUpDb(ViewTrackerRepository repo) {
        repo.createViewTable();
        repo.createViewedProfileIndex();
    }
}
