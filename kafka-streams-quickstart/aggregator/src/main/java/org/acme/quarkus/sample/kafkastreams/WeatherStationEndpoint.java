package org.acme.quarkus.sample.kafkastreams;

import java.net.URI;
import java.net.URISyntaxException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@ApplicationScoped
@Path("/weather-stations")
public class WeatherStationEndpoint {

    @Inject
    private KafkaStreamsPipeline pipeline;

    @GET
    @Path("/data/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeatherStationData(@PathParam("id") int id) {
        GetWeatherStationDataResponse result = pipeline.getWeatherStationData(id);

        if (result.getResult().isPresent()) {
            return Response.ok(result.getResult().get()).build();
        }
        else if (result.getHost().isPresent()) {
            URI otherUri = getOtherUri(result.getHost().get(), result.getPort().getAsInt(), id);
            return Response.seeOther(otherUri).build();
        }
        else {
            return Response.status(Status.NOT_FOUND.getStatusCode(), "No data found for weather station " + id).build();
        }
    }

    @GET
    @Path("/meta-data")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMetaData() {
        return pipeline.getMetaData();
    }

    private URI getOtherUri(String host, int port, int id) {
        try {
            return new URI("http://" + host + ":" + port + "/weather-stations/data/" + id);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
