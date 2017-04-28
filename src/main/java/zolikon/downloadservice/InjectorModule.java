package zolikon.downloadservice;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.yaml.snakeyaml.Yaml;
import zolikon.downloadservice.configuration.MongoConfiguration;
import zolikon.downloadservice.configuration.SeriesConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class InjectorModule implements Module {

    public static final String URL_COLLECTION = "urlCollection";
    public static final String SERIES_COLLECTION = "seriesCollection";


    @Override
    public void configure(Binder binder) {
    }

    @Provides
    public ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        SimpleModule customModule = new SimpleModule("customModule");
        customModule.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                gen.writeString(value.format(DateTimeFormatter.BASIC_ISO_DATE));
            }
        });
        customModule.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                LocalDate result;
                try {
                    result = LocalDate.parse(p.getText());
                } catch (Exception exc) {
                    result = null;
                }
                return result;
            }
        });
        objectMapper.registerModule(customModule);
        return objectMapper;
    }


    @Provides
    public MongoClient createClient(MongoConfiguration mongoConfiguration) {
        return new MongoClient(mongoConfiguration.getHost());
    }

    @Provides
    @Singleton
    @Named(URL_COLLECTION)
    public MongoCollection<Document> getTorrentCollection(MongoClient client, MongoConfiguration mongoConfiguration) {
        MongoDatabase database = client.getDatabase(mongoConfiguration.getUrlDatabase());
        return database.getCollection(mongoConfiguration.getUrlCollection());
    }

    @Provides
    @Singleton
    @Named(SERIES_COLLECTION)
    public MongoCollection<Document> getSeriesCollection(MongoClient client, MongoConfiguration mongoConfiguration) {
        MongoDatabase database = client.getDatabase(mongoConfiguration.getSeriesDatabase());
        return database.getCollection(mongoConfiguration.getSeriesCollection());
    }

    @Provides
    @Singleton
    public MongoConfiguration getMongoConfiguration() {
        return getConfiguration(MongoConfiguration.class);
    }

    @Provides
    @Singleton
    public SeriesConfiguration getSeriesConfiguration() {
        return getConfiguration(SeriesConfiguration.class);
    }


    private <T> T getConfiguration(Class<T> clazz) {
        Yaml yaml = new Yaml();
        String name = clazz.getSimpleName().replace("Configuration", "").toLowerCase() + ".yaml";
        T result = null;
        try {
            try (InputStream in = Files.newInputStream(Paths.get(ClassLoader.getSystemResource(name).toURI()))) {
                result = yaml.loadAs(in, clazz);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }


}
