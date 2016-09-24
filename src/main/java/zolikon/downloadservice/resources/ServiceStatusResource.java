package zolikon.downloadservice.resources;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import zolikon.downloadservice.service.ServiceRegistry;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class ServiceStatusResource {


    private ServiceRegistry registry;

    @Inject
    public ServiceStatusResource(ServiceRegistry registry) {
        this.registry = registry;
    }

    @GET
    public JsonNode getStatus() {
        return registry.getStatus();
    }

}
