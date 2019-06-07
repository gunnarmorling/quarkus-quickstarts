package org.acme.quarkus.sample.kafkastreams;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/weather-stations")
public class WeatherStationEndpoint {

	@Inject
	private KafkaStreamsPipeline pipeline;

	@GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getWeatherStationData(@PathParam("id") int id) {
        JsonObject result = pipeline.getWeatherStationData(id);

        if (result == null) {
        	throw new NotFoundException("No data found for weather station " + id);
        }

        return result;
    }

}
