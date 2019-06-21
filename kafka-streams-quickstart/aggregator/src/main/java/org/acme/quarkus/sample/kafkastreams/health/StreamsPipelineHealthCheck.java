package org.acme.quarkus.sample.kafkastreams.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.acme.quarkus.sample.kafkastreams.streams.KafkaStreamsPipeline;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class StreamsPipelineHealthCheck implements HealthCheck {

    private KafkaStreamsPipeline pipeline;

    public StreamsPipelineHealthCheck() {
    }

    @Inject
    public StreamsPipelineHealthCheck(KafkaStreamsPipeline pipeline) {
        this.pipeline = pipeline;
    }
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("Temperature Values Pipeline")
                .state(pipeline.isRunning())
                .build();
    }
}
