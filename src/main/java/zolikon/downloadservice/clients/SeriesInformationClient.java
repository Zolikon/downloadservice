package zolikon.downloadservice.clients;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import zolikon.downloadservice.model.Episode;
import zolikon.downloadservice.model.Series;
import zolikon.downloadservice.model.SeriesApiInformation;

import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Optional;

@Singleton
public class SeriesInformationClient {

    private final HttpHost httpHost;
    private final ObjectMapper mapper;

    @Inject
    public SeriesInformationClient(ObjectMapper mapper){
        httpHost = HttpHost.create("http://api.tvmaze.com");
        this.mapper = mapper;
    }


    public Optional<SeriesApiInformation> getSeriesInfo(Series series){
        String seriesName=series.getName().replaceAll("\\W","+");
        HttpRequest request = new HttpGet("/singlesearch/shows?q="+seriesName);
        JsonNode callResult = callEndpoint(request);
        Optional<SeriesApiInformation> result = Optional.empty();
        if(callResult!=null){
            result=Optional.of(mapper.convertValue(callResult,SeriesApiInformation.class));
        }
        return result;
    }

    public Optional<Episode> getEpisodeInfo(String id, Episode episode){
        String url = String.format("/shows/%s/episodebynumber?season=%d&number=%d",id,episode.getSeason(),episode.getNumber());
        HttpRequest request = new HttpGet(url);
        JsonNode callResult = callEndpoint(request);
        Optional<Episode> result = Optional.empty();
        if(callResult!=null){
            Episode value = mapper.convertValue(callResult, Episode.class);
            if (value.getAirdate()!=null){
                result=Optional.of(value);
            }
        }
        return result;
    }

    private JsonNode callEndpoint(HttpRequest request){
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(httpHost,request);
            JsonNode result;
            if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                HttpEntity httpEntity = response.getEntity();
                result = mapper.readTree(EntityUtils.toString(httpEntity));
            } else {
                result= null;
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }






}
