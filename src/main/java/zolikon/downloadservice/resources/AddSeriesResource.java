package zolikon.downloadservice.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.mongodb.MongoWriteException;
import zolikon.downloadservice.clients.SeriesInformationClient;
import zolikon.downloadservice.dao.SeriesDao;
import zolikon.downloadservice.model.Episode;
import zolikon.downloadservice.model.Series;
import zolikon.downloadservice.model.SeriesApiInformation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/addSeries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AddSeriesResource {


    private ObjectMapper mapper;
    private SeriesDao seriesDao;
    private SeriesInformationClient seriesInformationClient;

    @Inject
    public AddSeriesResource(ObjectMapper mapper, SeriesDao seriesDao, SeriesInformationClient seriesInformationClient) {

        this.mapper = mapper;
        this.seriesDao = seriesDao;
        this.seriesInformationClient = seriesInformationClient;
    }

    @POST
    public Response addSeries(Series newSeries){
        if(newSeries.getName()==null){
            return createResponse(ResponseStatus.MISSING_NAME);
        }
        Optional<SeriesApiInformation> seriesApiInformationOptional = seriesInformationClient.getSeriesInfo(newSeries);
        if(!seriesApiInformationOptional.isPresent()){
            return createResponse(ResponseStatus.UNKOWN_NAME);
        }
        newSeries.update(seriesApiInformationOptional.get());
        if(newSeries.getLastEpisode()==null){
            newSeries.setLastEpisode(new Episode(0,0));
        }
        try {
            seriesDao.save(newSeries);
        } catch (MongoWriteException exc){
            return createResponse(ResponseStatus.DUPLICATED_NAME);
        }
        return createResponse(ResponseStatus.OK);
    }

    private Response createResponse(ResponseStatus response){
        ObjectNode node = mapper.createObjectNode();
        node.put("status",response.name());
        return Response.status(response.getStatus()).entity(node).build();
    }

    private enum ResponseStatus{
        OK(200),
        MISSING_NAME(400),
        UNKOWN_NAME(400),
        DUPLICATED_NAME(400);

        private int status;

        ResponseStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }

}
