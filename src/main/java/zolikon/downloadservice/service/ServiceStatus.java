package zolikon.downloadservice.service;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.time.LocalTime;

@JsonInclude(Include.NON_NULL)
@SuppressWarnings("unused")
public class ServiceStatus {

    private String id;
    private long delayInSeconds;
    private long rateInSeconds;
    private String lastRun;
    private int iterationCount=0;
    private boolean isServiceRunning;

    ServiceStatus(String id, long delay, long rate) {
        this.id = id;
        this.delayInSeconds = delay;
        this.rateInSeconds = rate;
    }

    void addIteration(LocalTime time){
        lastRun = time.toString();
        iterationCount++;
    }

    ServiceStatus snapshot(boolean isServiceRunning){
        this.isServiceRunning = isServiceRunning;
        return this;
    }

    public long getDelayInSeconds() {
        return delayInSeconds;
    }

    public void setDelayInSeconds(long delayInSeconds) {
        this.delayInSeconds = delayInSeconds;
    }

    public long getRateInSeconds() {
        return rateInSeconds;
    }

    public void setRateInSeconds(long rateInSeconds) {
        this.rateInSeconds = rateInSeconds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isServiceRunning() {
        return isServiceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) {
        isServiceRunning = serviceRunning;
    }

    public String getLastRun() {
        return lastRun;
    }

    public void setLastRun(String lastRun) {
        this.lastRun = lastRun;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }
}
