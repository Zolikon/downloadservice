package zolikon.downloadservice.configuration;

public class MongoConfiguration {

    private String host;
    private String urlDatabase;
    private String urlCollection;

    private String seriesDatabase;
    private String seriesCollection;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public String getUrlCollection() {
        return urlCollection;
    }

    public void setUrlCollection(String urlCollection) {
        this.urlCollection = urlCollection;
    }

    public String getSeriesCollection() {
        return seriesCollection;
    }

    public void setSeriesCollection(String seriesCollection) {
        this.seriesCollection = seriesCollection;
    }

    public String getUrlDatabase() {
        return urlDatabase;
    }

    public void setUrlDatabase(String urlDatabase) {
        this.urlDatabase = urlDatabase;
    }

    public String getSeriesDatabase() {
        return seriesDatabase;
    }

    public void setSeriesDatabase(String seriesDatabase) {
        this.seriesDatabase = seriesDatabase;
    }

    @Override
    public String toString() {
        return "MongoConfiguration{" +
                "host='" + host + '\'' +
                ", urlDatabase='" + urlDatabase + '\'' +
                ", urlCollection='" + urlCollection + '\'' +
                ", seriesDatabase='" + seriesDatabase + '\'' +
                ", seriesCollection='" + seriesCollection + '\'' +
                '}';
    }
}
