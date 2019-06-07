package org.acme.quarkus.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.kafka.KafkaMessage;

/**
 * A bean producing random prices every 5 seconds.
 * The prices are written to a Kafka topic (prices). The Kafka configuration is specified in the application configuration.
 */
@ApplicationScoped
public class PriceGenerator {

    private Random random = new Random();

    @Outgoing("temperature-values")
    public Flowable<KafkaMessage<Integer, Double>> generate() {
        return Flowable.interval(1, TimeUnit.SECONDS)
                .map(tick -> random.nextDouble() * 50)
                .map(value -> {
                	return KafkaMessage.of(random.nextInt(3) + 1, value);
                });
    }

    @Outgoing("weather-stations")
    public Flowable<KafkaMessage<Integer, String>> weatherStations() {
        List<KafkaMessage<Integer, String>> stations = new ArrayList<>();
        stations.add(KafkaMessage.of(1, "{ \"id\" : 1, \"name\" : \"Hamburg\" }"));
        stations.add(KafkaMessage.of(2, "{ \"id\" : 2, \"name\" : \"Snowdonia\" }"));
        stations.add(KafkaMessage.of(3, "{ \"id\" : 3, \"name\" : \"Boston\" }"));

        return Flowable.fromIterable(stations);
	};
}
