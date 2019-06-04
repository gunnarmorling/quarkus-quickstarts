package org.acme.quarkus.sample.kafkastreams;

import java.util.Optional;
import java.util.OptionalInt;

import javax.json.JsonObject;

public class GetWeatherStationDataResponse {

    private static GetWeatherStationDataResponse NOT_FOUND = new GetWeatherStationDataResponse(null);

    private final JsonObject result;
    private final String host;
    private final Integer port;

    public GetWeatherStationDataResponse(JsonObject result) {
        this.result = result;
        this.host = null;
        this.port = null;
    }

    public GetWeatherStationDataResponse(String host, int port) {
        this.result = null;
        this.host = host;
        this.port = port;
    }

    public static GetWeatherStationDataResponse found(JsonObject jsonObject) {
        return new GetWeatherStationDataResponse(jsonObject);
    }

    public static GetWeatherStationDataResponse foundRemotely(String host, int port) {
        return new GetWeatherStationDataResponse(host, port);
    }

    public static GetWeatherStationDataResponse notFound() {
        return NOT_FOUND;
    }

    public Optional<JsonObject> getResult() {
        return Optional.ofNullable(result);
    }

    public Optional<String> getHost() {
        return Optional.ofNullable(host);
    }

    public OptionalInt getPort() {
        return port != null ? OptionalInt.of(port) : OptionalInt.empty();
    }
}
