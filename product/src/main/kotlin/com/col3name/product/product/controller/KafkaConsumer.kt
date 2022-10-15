package com.col3name.product.product.controller

import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
//
//@EnableKafka
//class KafkaConsumer {
//
//    /*
//        Read simple topic
//     */
////    @KafkaListener(topics = ["topico-teste-kotlin"])
////    fun receive(@Payload payload: String) {
////        println("Kafka Listener: $payload")
////    }
//
//    /*
//        Listen with Headers
//     */
//    @KafkaListener(topics = [    "product.request.findAll", "product.request.findAll"])
//    fun listenWithHeaders(
//        @Payload message: String,
//        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partition: Int,
//        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String
//    ) {
//        println("Topic $topic, received Message: $message from partiton: $partition")
//    }
//}