package zolikon.downloadservice.service;


import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import jpa.Torrent;
import zolikon.downloadservice.InjectorModule;
import zolikon.downloadservice.clients.PiratebaySearchClient;
import zolikon.downloadservice.clients.SeriesInformationClient;
import zolikon.downloadservice.configuration.SeriesConfiguration;
import zolikon.downloadservice.dao.SeriesDao;
import zolikon.downloadservice.dao.UrlWriter;
import zolikon.downloadservice.model.AdditionalInformation;
import zolikon.downloadservice.model.Episode;
import zolikon.downloadservice.model.Series;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SeriesService extends AbstractScheduledService {

    private final Scheduler scheduler;
    private Series series;
    private final SeriesDao seriesDao;
    private final UrlWriter urlWriter;
    private final PiratebaySearchClient piratebaySearchClient;
    private final SeriesInformationClient seriesInformationClient;
    private final SeriesConfiguration seriesConfiguration;

    public SeriesService(Injector injector, Series series) {
        super();
        this.series = series;
        this.seriesDao = injector.getInstance(SeriesDao.class);
        this.urlWriter = injector.getInstance(UrlWriter.class);
        this.piratebaySearchClient = injector.getInstance(PiratebaySearchClient.class);
        this.seriesInformationClient = injector.getInstance(SeriesInformationClient.class);
        this.seriesConfiguration = injector.getInstance(SeriesConfiguration.class);
        long rate = seriesConfiguration.getServicesIterationRateInSeconds();
        long delay = createDelay(rate);
        System.out.printf("service %s created, delay: %d, rate:%d\n", series.getName(), delay, rate);
        this.scheduler = Scheduler.newFixedRateSchedule(delay, rate, TimeUnit.SECONDS);
    }

    private int createDelay(long rate) {
        return (int) (new Random().nextInt((int) (rate / 10)) * (rate / 10));
    }

    public ServiceStatus getStatus() {
        ServiceStatus result = new ServiceStatus();
        Optional<Series> optionalSeries = seriesDao.getSeries(series.getName());
        Series series = optionalSeries.get();
        result.setName(series.getName());
        result.setLastEpisode(series.getLastEpisode());
        result.setServiceRunning(this.isRunning());
        return result;
    }

    @Override
    protected void runOneIteration() throws Exception {
        Optional<Episode> optionalEpisode = getNextEpisode();
        if (!optionalEpisode.isPresent()) {
            return;
        }
        Episode nextEpisode = optionalEpisode.get();
        series.setNextEpisode(nextEpisode);
        System.out.println(String.format("next episode %s", series.createSearchStringForNextEpisode()));

        LocalDate airdate = nextEpisode.getAirdate();
        LocalDate now = LocalDate.now();
        if (now.isAfter(airdate)) {
            searchForUrl();
        } else {
            System.out.printf("next airdate is %d days away, shutting down service", now.until(airdate).getDays());
            stopAsync();
        }
        seriesDao.updateSeries(series);
    }

    private void searchForUrl() {
        Optional<Torrent> torrentOptional = piratebaySearchClient.search(series.createSearchStringForNextEpisode());
        if (torrentOptional.isPresent()) {
            Torrent torrent = torrentOptional.get();
            AdditionalInformation additionalInformation = new AdditionalInformation(seriesConfiguration.getSeriesSaveFolder() + series.createSavePathForNextEpisode(), true);
            urlWriter.saveTorrent(torrent, additionalInformation);
            System.out.println(String.format("saving url for episode %s", series.createSearchStringForNextEpisode()));
            series.saveSuccessful();
        } else {
            System.out.println(String.format("no url found for episode %s", series.createSearchStringForNextEpisode()));
        }
    }

    private Optional<Episode> getNextEpisode() {
        Optional<Episode> optionalEpisode = seriesInformationClient.getEpisodeInfo(series.getApiId(), series.nextEpisode());
        if (!optionalEpisode.isPresent()) {
            series.nextSeason();
            optionalEpisode = seriesInformationClient.getEpisodeInfo(series.getApiId(), series.nextSeason());
        }
        return optionalEpisode;
    }


    @Override
    protected Scheduler scheduler() {
        return scheduler;
    }
}
