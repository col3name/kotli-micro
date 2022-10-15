package com.col3name.product.product.controller

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
//
//@EnableKafka
//@Configuration
//class KafkaConsumerConfig {
//
//    @Bean
//    fun consumerConfigs(): Map<String, Any> {
//        val props = HashMap<String, Any>()
//        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
//        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
//        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
//        props[ConsumerConfig.GROUP_ID_CONFIG] = "m_groupid"
//        return props
//    }
//
//    @Bean
//    fun consumerFactory(): ConsumerFactory<String, String> {
//        return DefaultKafkaConsumerFactory(consumerConfigs())
//    }
//
//    @Bean
//    fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> {
//        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
//        factory.consumerFactory = consumerFactory()
//        return factory
//    }
//
//    @Bean
//    fun kafkaConsumer(): KafkaConsumer {
//        return KafkaConsumer()
//    }
//}