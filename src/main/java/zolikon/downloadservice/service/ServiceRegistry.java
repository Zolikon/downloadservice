package zolikon.downloadservice.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.google.inject.Injector;
import zolikon.downloadservice.model.Series;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceRegistry {

    private Set<SeriesService> services= new HashSet<>();
    private Injector injector;

    @Inject
    public ServiceRegistry(Injector injector) {
        this.injector = injector;
    }

    public void add(List<Series> serviceList){
        serviceList.stream().forEach(this::add);
    }

    public void add(Series series){
        SeriesService seriesService = new SeriesService(injector,series);
        services.add(seriesService);
        seriesService.startAsync();
    }

    public void stopAll(){
        services.stream().forEach(service -> service.stopAsync());
    }

    public JsonNode getStatus(){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        for(SeriesService seriesService:services){
            ServiceStatus status = seriesService.getStatus();
            arrayNode.add(mapper.convertValue(status,JsonNode.class));
        }
        ObjectNode result = mapper.createObjectNode();
        result.set("services",arrayNode);
        return result;
    }




}
