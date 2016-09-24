package zolikon.downloadservice.service;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import zolikon.downloadservice.model.Episode;

@JsonInclude(Include.NON_NULL)
public class ServiceStatus {

    private String name;
    private Episode lastEpisode;
    private Episode nextEpisode;
    private boolean isServiceRunning;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Episode getLastEpisode() {
        return lastEpisode;
    }

    public void setLastEpisode(Episode lastEpisode) {
        this.lastEpisode = lastEpisode;
    }

    public Episode getNextEpisode() {
        return nextEpisode;
    }

    public void setNextEpisode(Episode nextEpisode) {
        this.nextEpisode = nextEpisode;
    }

    public boolean isServiceRunning() {
        return isServiceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) {
        isServiceRunning = serviceRunning;
    }
}
