package com.example.demo.service;

import com.example.demo.dto.Sale;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SaleService {
    @ServiceActivator(inputChannel = "saleInputChannel" )
    public void saleReceiver(Sale payload, @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {
        log.info("Pojo arrived! Payload: " + payload);
        message.ack();
    }
}
