package zolikon.downloadservice.dao;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import zolikon.downloadservice.InjectorModule;
import zolikon.downloadservice.clients.PiratebaySearchClient;
import zolikon.downloadservice.clients.SeriesInformationClient;
import zolikon.downloadservice.model.Episode;
import zolikon.downloadservice.model.Series;
import org.bson.Document;
import zolikon.downloadservice.model.SeriesApiInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static zolikon.downloadservice.InjectorModule.SERIES_COLLECTION;

@Singleton
public class SeriesDao {

    private ObjectMapper mapper;
    private MongoCollection<Document> collection;

    @Inject
    public SeriesDao(ObjectMapper mapper, @Named(SERIES_COLLECTION) MongoCollection<Document> collection) {
        this.mapper = mapper;
        this.collection = collection;
    }

    public List<Series> getSeries(){
        List<Series> result = new ArrayList<>();
        MongoIterable<Document> iterable = collection.find();
        for(Document doc:iterable){
            Series series = convert(doc);
            result.add(series);
        }
        return  result;
    }

    public Optional<Series> getSeries(String name){
        Optional<Series> result = Optional.empty();
        Document doc = collection.find(Filters.eq("_id", name)).first();
        if(doc!=null){
            result = Optional.of(convert(doc));
        }
        return result;
    }

    public void updateSeries(Series series){
        Document doc = convert((Series) series);
        collection.replaceOne(Filters.eq("_id",series.getName()),doc);
    }

    public void addSeries(Series series){
        collection.insertOne(convert(series));
    }

    private Series convert(Document doc) {
        Series series = mapper.convertValue(doc,Series.class);
        series.setName(doc.get("_id").toString());
        return series;
    }

    private Document convert(Series series) {
        Document doc = new Document("_id",series.getName()).append("apiId",series.getApiId());
        doc.append("lastEpisode",convert(series.getLastEpisode()));
        if(series.getNextEpisode()!=null){
            doc.append("nextEpisode",convert(series.getNextEpisode()));
        }
        return doc;
    }

    private Document convert(Episode episode) {
        Document doc =  new Document("season",episode.getSeason()).append("number",episode.getNumber());
        if(episode.getAirdate()!=null){
            doc.append("airdate",episode.getAirdate().toString());
        }
        return doc;
    }

}
