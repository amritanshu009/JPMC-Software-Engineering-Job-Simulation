package com.jpmc.midascore.config;

import com.jpmc.midascore.foundation.Transaction;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrap;

    @Bean
    public ProducerFactory<String, Transaction> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);

        JsonSerializer<Transaction> value = new JsonSerializer<>();
        value.setAddTypeInfo(false);  // no type headers

        return new DefaultKafkaProducerFactory<>(
                props,
                new StringSerializer(),
                value
        );
    }

    @Bean
    public KafkaTemplate<String, Transaction> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
