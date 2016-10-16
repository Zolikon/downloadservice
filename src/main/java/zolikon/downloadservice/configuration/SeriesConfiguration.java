package zolikon.downloadservice.configuration;


public class SeriesConfiguration {

    private String seriesSaveFolder;
    private Long servicesIterationRateInSeconds;
    private Long serviceStartDelayInSeconds;

    public Long getServiceStartDelayInSeconds() {
        return serviceStartDelayInSeconds;
    }

    public void setServiceStartDelayInSeconds(Long serviceStartDelayInSeconds) {
        this.serviceStartDelayInSeconds = serviceStartDelayInSeconds;
    }

    public String getSeriesSaveFolder() {
        return seriesSaveFolder.replaceAll("/","\\\\");
    }

    public void setSeriesSaveFolder(String seriesSaveFolder) {
        this.seriesSaveFolder = seriesSaveFolder;
    }

    public Long getServicesIterationRateInSeconds() {
        return servicesIterationRateInSeconds;
    }

    public void setServicesIterationRateInSeconds(Long servicesIterationRateInSeconds) {
        this.servicesIterationRateInSeconds = servicesIterationRateInSeconds;
    }
}
