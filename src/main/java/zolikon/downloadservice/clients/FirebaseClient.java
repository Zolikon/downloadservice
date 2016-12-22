package zolikon.downloadservice.clients;


import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Singleton
public class FirebaseClient {

    public static final int OK_STATUS = 200;
    public static final String URL = "/movie.json";
    public static final String EMPTY_REQUEST = "\"\"";

    private final HttpClient client;
    private final HttpHost httpHost;
    private final HttpRequest getRequest;
    private final HttpPut updateRequest;

    public FirebaseClient(){
        client = HttpClientBuilder.create().build();
        HttpEntity entity = new ByteArrayEntity(EMPTY_REQUEST.getBytes());
        getRequest = new HttpGet(URL);
        updateRequest = new HttpPut(URL);
        updateRequest.setEntity(entity);
        httpHost = HttpHost.create("https://download-ad2a9.firebaseio.com");
    }

    public List<String> getMoviesToDownload(){
        try {
            HttpResponse response = client.execute(httpHost, getRequest);
            List<String> result;
            if(response.getStatusLine().getStatusCode()== OK_STATUS){
                HttpEntity httpEntity = response.getEntity();
                String apiOutput = EntityUtils.toString(httpEntity);
                result = Arrays.asList(apiOutput.replaceAll("\"","").split("%!%"));
            } else {
                result=Collections.emptyList();
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public void emptyMovies(){
        try {
            HttpResponse response = client.execute(httpHost, updateRequest);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
