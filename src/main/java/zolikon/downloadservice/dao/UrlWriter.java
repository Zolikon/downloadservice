package zolikon.downloadservice.dao;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mongodb.client.MongoCollection;
import zolikon.downloadservice.InjectorModule;
import zolikon.downloadservice.model.AdditionalInformation;
import jpa.Torrent;
import org.bson.Document;

import static zolikon.downloadservice.InjectorModule.URL_COLLECTION;

@Singleton
public class UrlWriter {

    private final MongoCollection<Document> collection;

    @Inject
    public UrlWriter(@Named(URL_COLLECTION) MongoCollection<Document> collection){
        this.collection =collection;
    }

    public void saveTorrent(Torrent torrent,AdditionalInformation additionalInformation){
        Document document = new Document();
        document.append("processed",false);
        document.append("name",torrent.Name);
        document.append("url",torrent.Magnet);
        document.append("saveFolder",additionalInformation.getSaveFolder());
        document.append("isAbsolutePath",additionalInformation.isAbsolutePath());
        collection.insertOne(document);
    }



}
