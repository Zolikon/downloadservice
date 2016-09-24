package zolikon.downloadservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Series{

    private String apiId;
    private String name;
    private Episode lastEpisode;
    private Episode nextEpisode;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

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

    public void update(SeriesApiInformation information){
        this.name = information.getName();
        this.apiId = information.getApiId();
    }

    public Episode nextEpisode(){
        if(nextEpisode==null){
            return new Episode(lastEpisode.getSeason(),lastEpisode.getNumber()+1);
        }
        return nextEpisode;
    }

    public Episode nextSeason(){
        if(nextEpisode==null){
            return new Episode(lastEpisode.getSeason()+1,1);
        }
        return nextEpisode;
    }

    public void saveSuccessful(){
        lastEpisode=nextEpisode;
        nextEpisode=null;
    }

    public String createSearchStringForNextEpisode(){
        return String.format("%s s%02de%02d",name,nextEpisode.getSeason(),nextEpisode.getNumber());
    }

    public String createSavePathForNextEpisode(){
        return String.format("%s\\Season %d",name,nextEpisode.getSeason());
    }

    @Override
    public String toString() {
        return "Series{" +
                "apiId='" + apiId + '\'' +
                ", name='" + name + '\'' +
                ", lastEpisode=" + lastEpisode +
                ", nextEpisode=" + nextEpisode +
                '}';
    }
}
