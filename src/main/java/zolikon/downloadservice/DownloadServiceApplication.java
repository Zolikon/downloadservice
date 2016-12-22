package zolikon.downloadservice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import zolikon.downloadservice.configuration.ApplicationConfiguration;
import zolikon.downloadservice.resources.SeriesResource;
import zolikon.downloadservice.resources.ServiceStatusResource;
import zolikon.downloadservice.service.SeriesService;


public class DownloadServiceApplication extends Application<ApplicationConfiguration>{

    private Injector injector = Guice.createInjector(new InjectorModule());

    public static void main(String[] args) throws Exception {
        new DownloadServiceApplication().run(args);

    }

    @Override
    public void run(ApplicationConfiguration configuration, Environment environment) throws Exception {
        final ServiceStatusResource serviceStatusResource = injector.getInstance(ServiceStatusResource.class);
        final SeriesResource addSeriesResource = injector.getInstance(SeriesResource.class);
        environment.jersey().register(serviceStatusResource);
        environment.jersey().register(addSeriesResource);
        SeriesService seriesService = injector.getInstance(SeriesService.class);
        seriesService.startAsync();
    }

}
