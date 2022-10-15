package com.col3name.product.product.controller
//
//import org.apache.kafka.clients.admin.NewTopic
//import org.apache.kafka.clients.producer.ProducerConfig
//import org.apache.kafka.common.serialization.StringSerializer
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.kafka.config.TopicBuilder
//import org.springframework.kafka.core.DefaultKafkaProducerFactory
//import org.springframework.kafka.core.KafkaTemplate
//import org.springframework.kafka.core.ProducerFactory
//
//
//@Configuration
//class KafkaProducerConfig {
//
//    @Bean
//    fun producerFactory(): ProducerFactory<String, String> {
//        val configProps = HashMap<String, Any>()
//        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
//        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
//        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
//        return DefaultKafkaProducerFactory(configProps)
//    }
//
//    @Bean
//    fun kafkaTemplate(): KafkaTemplate<String, String> {
//        return KafkaTemplate(producerFactory())
//    }
//
//    /*
//        Auto configure a topic
//     */
//    @Bean
//    fun createTopic(): NewTopic {
//        return NewTopic("topic-from-bean", 5, 1)
//    }
//
//    @Bean
//    fun topic1(): NewTopic? {
//        return TopicBuilder.name( "product.request.findAll").build()
//    }
//
//    @Bean
//    fun topic2(): NewTopic? {
//        return TopicBuilder.name("product.request.findAll").build()
//    }
//}