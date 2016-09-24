package zolikon.downloadservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Episode {

    private Integer season;
    private Integer number;
    private LocalDate airdate;

    public Episode(Integer season, Integer number) {
        this.season = season;
        this.number = number;
    }

    public Episode(){

    }


    public LocalDate getAirdate() {
        return airdate;
    }

    public void setAirdate(LocalDate airdate) {
        this.airdate = airdate;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void nextEpisode(){
        number++;
    }

    public void nextSeason(){
        season++;
        number = 1;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "season=" + season +
                ", number=" + number +
                ", airdate=" + airdate +
                '}';
    }
}
