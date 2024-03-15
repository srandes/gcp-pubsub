package com.example.demo.service;

import com.example.demo.dto.Sale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SaleService {
    @ServiceActivator(inputChannel = "pojoInputChannel")
    public void pojoReceiver(Sale payload) {
        log.info("Pojo arrived! Payload: " + payload);
    }
}
