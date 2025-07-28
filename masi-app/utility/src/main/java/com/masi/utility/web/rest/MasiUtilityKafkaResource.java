package com.masi.utility.web.rest;

import com.masi.utility.broker.KafkaConsumer;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/masi-utility-kafka")
public class MasiUtilityKafkaResource {

    private static final String PRODUCER_BINDING_NAME = "binding-out-0";

    private static final Logger log = LoggerFactory.getLogger(MasiUtilityKafkaResource.class);
    private final KafkaConsumer kafkaConsumer;
    private final StreamBridge streamBridge;

    public MasiUtilityKafkaResource(StreamBridge streamBridge, KafkaConsumer kafkaConsumer) {
        this.streamBridge = streamBridge;
        this.kafkaConsumer = kafkaConsumer;
        
    }
    public static record Payload(String message,String name) implements Serializable {
        private static final long serialVersionUID =4234234L;

    }
    @PostMapping("/publish")
    public Mono<ResponseEntity<Void>> publish(@RequestBody Payload payload) {
        log.debug("REST request the message : {} to send to Kafka topics", payload.message());
        streamBridge.send(PRODUCER_BINDING_NAME, payload,MediaType.APPLICATION_JSON);
        return Mono.just(ResponseEntity.noContent().build());
    }

    @GetMapping("/consume")
    public Flux<String> consume() {
        log.debug("REST request to consume records from Kafka topics");
        return this.kafkaConsumer.getFlux();
    }
}
