package zolikon.downloadservice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import zolikon.downloadservice.configuration.ApplicationConfiguration;
import zolikon.downloadservice.dao.SeriesDao;
import zolikon.downloadservice.model.Series;
import zolikon.downloadservice.resources.AddSeriesResource;
import zolikon.downloadservice.resources.ServiceStatusResource;
import zolikon.downloadservice.service.SeriesService;
import zolikon.downloadservice.service.ServiceRegistry;

import java.util.List;

public class DownloadServiceApplication extends Application<ApplicationConfiguration>{

    private Injector injector = Guice.createInjector(new InjectorModule());

    public static void main(String[] args) throws Exception {
        new DownloadServiceApplication().run(args);
    }

    @Override
    public void run(ApplicationConfiguration configuration, Environment environment) throws Exception {
        final ServiceStatusResource serviceStatusResource = injector.getInstance(ServiceStatusResource.class);
        final AddSeriesResource addSeriesResource = injector.getInstance(AddSeriesResource.class);
        environment.jersey().register(serviceStatusResource);
        environment.jersey().register(addSeriesResource);
        startServices();
    }

    private void startServices(){
        SeriesDao dao = injector.getInstance(SeriesDao.class);
        ServiceRegistry registry = injector.getInstance(ServiceRegistry.class);
        registry.add(dao.getSeries());
    }
}
