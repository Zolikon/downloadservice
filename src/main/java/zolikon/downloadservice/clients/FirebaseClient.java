package zolikon.downloadservice.clients;


import com.google.inject.Singleton;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Singleton
public class FirebaseClient {

    public static final int OK_STATUS = 200;

    private final HttpClient client;
    private final HttpHost httpHost;
    private final HttpRequest request;

    public FirebaseClient(){
        client = HttpClientBuilder.create().build();
        request = new HttpGet("/movies.json");
        httpHost = HttpHost.create("https://download-ad2a9.firebaseio.com");
    }

    public List<String> getMoviesToDownload(){
        try {
            HttpResponse response = client.execute(httpHost,request);
            List<String> result;
            if(response.getStatusLine().getStatusCode()== OK_STATUS){
                HttpEntity httpEntity = response.getEntity();
                String apiOutput = EntityUtils.toString(httpEntity);
                result = Arrays.asList(apiOutput.replaceAll("\"","").split("!"));
            } else {
                result=Collections.emptyList();
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }







}
