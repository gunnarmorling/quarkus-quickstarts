package org.acme.quarkus.sample.kafkastreams.metrics;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.kafka.common.Metric;
import org.apache.kafka.streams.KafkaStreams;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.Gauge;
import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricType;

@ApplicationScoped
public class MetricsExporter {

    @Inject
    MetricRegistry metricRegistry;

    public MetricsExporter() {
    }

    public void exportMetrics(KafkaStreams streams) {
        Set<String> processed = new HashSet<>();

        for (Metric metric : streams.metrics().values()) {
            String name = metric.metricName().group() + ":" + metric.metricName().name();

            if (processed.contains(name)) {
                continue;
            }

            // string-typed metric not supported
            if (name.contentEquals("app-info:commit-id") || name.contentEquals("app-info:version")) {
                continue;
            }
            else if (name.endsWith("count") || name.endsWith("total")) {
                registerCounter(metric, name);
            }
            else {
                registerGauge(metric, name);
            }

            processed.add(name);
        }
    }

    private void registerGauge(Metric metric, String name) {
        Metadata metadata = new Metadata(name, MetricType.GAUGE);
        metadata.setDescription(metric.metricName().description());

        metricRegistry.register(metadata, new Gauge<Double>() {

            @Override
            public Double getValue() {
                return (Double) metric.metricValue();
            }
        } );
    }

    private void registerCounter(Metric metric, String name) {
        Metadata metadata = new Metadata(name, MetricType.COUNTER);
        metadata.setDescription(metric.metricName().description());

        metricRegistry.register(metadata, new Counter() {

            @Override
            public void inc(long n) {
            }

            @Override
            public void inc() {
            }

            @Override
            public long getCount() {
                return ((Double)metric.metricValue()).longValue();
            }

            @Override
            public void dec(long n) {
            }

            @Override
            public void dec() {
            }
        });
    }
}
