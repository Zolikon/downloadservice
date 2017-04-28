package zolikon.downloadservice.service;


import com.google.common.base.Joiner;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;
import com.google.inject.Injector;
import jpa.Torrent;
import org.apache.log4j.Logger;
import zolikon.downloadservice.clients.PiratebaySearchClient;
import zolikon.downloadservice.clients.SeriesInformationClient;
import zolikon.downloadservice.configuration.SeriesConfiguration;
import zolikon.downloadservice.dao.SeriesDao;
import zolikon.downloadservice.dao.UrlWriter;
import zolikon.downloadservice.model.AdditionalInformation;
import zolikon.downloadservice.model.Episode;
import zolikon.downloadservice.model.Series;
import zolikon.downloadservice.model.SeriesReport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.UUID.randomUUID;
import static zolikon.downloadservice.model.ReportColouring.*;

public class SeriesService extends AbstractScheduledService {

    private static final Logger LOG = Logger.getLogger(SeriesService.class);

    private final Scheduler scheduler;
    private SeriesDao seriesDao;
    private String id = randomUUID().toString().substring(0, 5);
    private ServiceStatus status;

    private final UrlWriter urlWriter;
    private final PiratebaySearchClient piratebaySearchClient;
    private final SeriesInformationClient seriesInformationClient;
    private final SeriesConfiguration seriesConfiguration;

    @Inject
    public SeriesService(Injector injector) {
        this.seriesDao = injector.getInstance(SeriesDao.class);
        this.urlWriter = injector.getInstance(UrlWriter.class);
        this.piratebaySearchClient = injector.getInstance(PiratebaySearchClient.class);
        this.seriesInformationClient = injector.getInstance(SeriesInformationClient.class);
        this.seriesConfiguration = injector.getInstance(SeriesConfiguration.class);

        long rate = seriesConfiguration.getServicesIterationRateInSeconds();
        long delay = seriesConfiguration.getServiceStartDelayInSeconds();
        System.out.printf("service %s created, delay: %d, rate:%d\n", id, delay, rate);
        this.scheduler = Scheduler.newFixedRateSchedule(delay, rate, TimeUnit.SECONDS);
        status = new ServiceStatus(id, delay, rate);
    }


    public ServiceStatus getStatus() {
        return status.snapshot(this.isRunning());
    }

    @Override
    protected void runOneIteration() throws Exception {
        List<Series> seriesList = seriesDao.getSeries();
        LOG.info(String.format("Iteration started at %s", LocalDateTime.now().toString()));
        for (Series series : seriesList) {
            Optional<Episode> optionalEpisode = getNextEpisode(series);
            SeriesReport report = new SeriesReport(series.getName());
            if (optionalEpisode.isPresent()) {
                Episode nextEpisode = optionalEpisode.get();
                series.setNextEpisode(nextEpisode);

                report.addReport(BLUE,"next episode %s", series.createSearchStringForNextEpisode());
                LocalDate airDate = nextEpisode.getAirdate();
                LocalDate now = LocalDate.now();
                if (now.isAfter(airDate)) {
                    searchForUrl(series);
                } else {
                    report.addReport(GREEN,"next airdate is %d days away", DAYS.between(now,airDate));
                }
                seriesDao.update(series);
            } else {
                report.addReport(RED,"no next episode for %s", series.getName());
            }
        }
        status.addIteration(LocalTime.now());
        List<String> seriesNames = seriesList.stream().map(series -> series.getName()).collect(Collectors.toList());
        LOG.info(String.format("Run comlepete, included in iteration: %s", Joiner.on(", ").join(seriesNames)));
    }

    @Override
    protected Scheduler scheduler() {
        return scheduler;
    }

    @Override
    protected void shutDown() throws Exception {
        LOG.error("Something went wrong, scheduling shutting down");
        super.shutDown();
    }

    private void searchForUrl(Series series) {
        Optional<Torrent> torrentOptional = piratebaySearchClient.search(series.createSearchStringForNextEpisode());
        if (torrentOptional.isPresent()) {
            Torrent torrent = torrentOptional.get();
            AdditionalInformation additionalInformation = new AdditionalInformation(seriesConfiguration.getSeriesSaveFolder() + series.createSavePathForNextEpisode(), true);
            urlWriter.saveTorrent(torrent, additionalInformation);
            LOG.info(String.format("saving url for episode %s", series.createSearchStringForNextEpisode()));
            series.saveSuccessful();
        } else {
            LOG.warn(String.format("no url found for episode %s", series.createSearchStringForNextEpisode()));
        }
    }

    private Optional<Episode> getNextEpisode(Series series) {
        Optional<Episode> optionalEpisode = seriesInformationClient.getEpisodeInfo(series.getApiId(), series.nextEpisode());
        if (!optionalEpisode.isPresent()) {
            series.nextSeason();
            optionalEpisode = seriesInformationClient.getEpisodeInfo(series.getApiId(), series.nextSeason());
        }
        return optionalEpisode;
    }

}
