package com.jpmc.midascore.config;

import com.jpmc.midascore.foundation.Transaction;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrap;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset:earliest}")
    private String autoOffsetReset;

    @Bean
    public ConsumerFactory<String, Transaction> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        JsonDeserializer<Transaction> value = new JsonDeserializer<>(Transaction.class);
        value.addTrustedPackages("*");      // accept our Transaction class
        value.setRemoveTypeHeaders(true);   // we don't send type headers

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                value
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Transaction> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Transaction> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
