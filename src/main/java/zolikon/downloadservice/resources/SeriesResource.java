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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static zolikon.downloadservice.resources.ResponseStatus.MISSING_NAME;
import static zolikon.downloadservice.resources.ResponseStatus.OK;


@Path("/series")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeriesResource {


    private ObjectMapper mapper;
    private SeriesDao seriesDao;
    private SeriesInformationClient seriesInformationClient;

    @Inject
    public SeriesResource(ObjectMapper mapper, SeriesDao seriesDao, SeriesInformationClient seriesInformationClient) {
        this.mapper = mapper;
        this.seriesDao = seriesDao;
        this.seriesInformationClient = seriesInformationClient;
    }


    @POST
    public Response addSeries(Series newSeries) {
        if (newSeries.getName() == null) {
            return createResponse(MISSING_NAME);
        }
        Optional<SeriesApiInformation> seriesApiInformationOptional = seriesInformationClient.getSeriesInfo(newSeries);
        if (!seriesApiInformationOptional.isPresent()) {
            return createResponse(ResponseStatus.UNKNOWN_NAME);
        }
        newSeries.update(seriesApiInformationOptional.get());
        if (newSeries.getLastEpisode() == null) {
            newSeries.setLastEpisode(new Episode(0, 0));
        }
        try {
            seriesDao.save(newSeries);
        } catch (MongoWriteException exc) {
            return createResponse(ResponseStatus.DUPLICATED_NAME);
        }
        return createResponse(OK);
    }

    @GET
    public Response getSeries(@QueryParam("name") @DefaultValue("") String name) {
        String lowercaseName = name.toLowerCase();
        List<Series> seriesList = seriesDao.getSeries().stream().filter(series -> series.getName().toLowerCase().contains(lowercaseName)).collect(Collectors.toList());
        return Response.status(OK.getStatus()).entity(seriesList).build();
    }

    private Response createResponse(ResponseStatus response) {
        ObjectNode node = mapper.createObjectNode();
        node.put("status", response.name());
        return Response.status(response.getStatus()).entity(node).build();
    }


}
