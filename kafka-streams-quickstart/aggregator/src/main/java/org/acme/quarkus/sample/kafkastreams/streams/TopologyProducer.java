package org.acme.quarkus.sample.kafkastreams.streams;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.json.Json;

import org.acme.quarkus.sample.kafkastreams.model.WeatherStation;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Produced;

import io.quarkus.kafka.client.serialization.JsonbSerde;

@ApplicationScoped
public class TopologyProducer {

    private static final String WEATHER_STATIONS_TOPIC = "weather-stations";
    private static final String TEMPERATURE_VALUES_TOPIC = "temperature-values";
    private static final String TEMPERATURES_ENRICHED_TOPIC = "temperatures-enriched";

    @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        JsonbSerde<WeatherStation> weatherStationSerde = new JsonbSerde<>(WeatherStation.class);
        JsonObjectSerde jsonObjectSerde = new JsonObjectSerde();

        GlobalKTable<Integer, WeatherStation> stations = builder.globalTable(
                WEATHER_STATIONS_TOPIC,
                Consumed.with(Serdes.Integer(), weatherStationSerde));

        builder.stream(
                        TEMPERATURE_VALUES_TOPIC,
                        Consumed.with(Serdes.Integer(), Serdes.String())
                )
                .map((Integer stationId, String measurement) -> {
                    String[] parts = measurement.split(";");
                    String ts = parts[0];
                    Double value = Double.valueOf(parts[1]);

                    return KeyValue.pair(stationId, Json.createObjectBuilder()
                        .add("stationId", stationId)
                        .add("ts", ts)
                        .add("value", value)
                        .build());
                })
                .join(
                        stations,
                        (stationId, measurement) -> stationId,
                        (measurement, station) ->
                            Json.createObjectBuilder(measurement)
                                .add("stationName", station.name)
                                .build()
                )
                .to(
                        TEMPERATURES_ENRICHED_TOPIC,
                        Produced.with(Serdes.Integer(), jsonObjectSerde)
                );

        return builder.build();
    }





    
    private String getIcon(double temperature) {
        return temperature < 0 ? "⛄⛄⛄" :
            temperature < 10 ? "🌱🌱🌱" :
            temperature < 20 ? "🌷🌷🌷" :
            temperature < 30 ? "🌳🌳🌳" :
                "🌴🌴🌴";
    }















    private Object test() {
        return KeyValue.pair("stationId", Json.createObjectBuilder()
        .add("stationId", "stationId")
        .add("ts", "ts")
        .add("value", "value")
        .build());
    }
}
