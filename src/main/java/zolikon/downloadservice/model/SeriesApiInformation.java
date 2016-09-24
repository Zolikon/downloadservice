package zolikon.downloadservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesApiInformation {

    private String name;
    @JsonProperty("id")
    private String apiId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    @Override
    public String toString() {
        return "SeriesApiInformation{" +
                "name='" + name + '\'' +
                ", apiId='" + apiId + '\'' +
                '}';
    }
}
