package com.project.Shopapp.components;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomHealthCheck implements HealthIndicator {
    private final KafkaAdmin kafkaAdmin;

    @Override
    public Health health() {
        try {
            Map<String, Object> details = new HashMap<>();
            String clusterId = kafkaAdmin.clusterId();
            if (clusterId.isEmpty()) {
                return Health.down().withDetail("Error", "Cannot get Cluster's id").build();
            } else {
                details.put("kafka", String.format("Cluster's id: %s", clusterId));
            }

            String computerName = InetAddress.getLocalHost().getHostName();
            details.put("computerName", String.format("computerName: %s", computerName));

            return Health.up()
                    .withDetails(details)
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("Error", e.getMessage())
                    .build();
        }
    }
}
