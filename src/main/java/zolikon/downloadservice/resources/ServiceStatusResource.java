package zolikon.downloadservice.resources;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import zolikon.downloadservice.dao.SeriesDao;
import zolikon.downloadservice.service.SeriesService;
import zolikon.downloadservice.service.ServiceStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class ServiceStatusResource {


    private SeriesService seriesService;
    private SeriesDao seriesDao;
    private ObjectMapper mapper;

    @Inject
    public ServiceStatusResource(SeriesService servicePool, SeriesDao seriesDao, ObjectMapper mapper) {
        this.seriesService = servicePool;
        this.seriesDao = seriesDao;
        this.mapper = mapper;
    }

    @GET
    public JsonNode getStatus() {
        ServiceStatus serviceStatus = seriesService.getStatus();
        ArrayNode seriesArray = mapper.createArrayNode();
        ObjectNode result = mapper.createObjectNode();
        result.set("service",mapper.convertValue(serviceStatus,JsonNode.class));
        result.set("series",seriesArray);
        return result;
    }

}
