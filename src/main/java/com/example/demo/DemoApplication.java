package com.example.demo;

import com.example.demo.dto.Sale;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
public class DemoApplication {

	private static final String CHAIN_SALE_SUB = "chain-sale";

	@Bean
	JacksonPubSubMessageConverter createConverter(ObjectMapper mapper) {
		return new JacksonPubSubMessageConverter(mapper);
	}
	@Bean
	MessageChannel inputChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	PubSubInboundChannelAdapter inputChannelAdapter(
			@Qualifier("inputChannel") MessageChannel messageChannel,
			PubSubTemplate pubSubTemplate,
			ObjectMapper objectMapper)
	{
		pubSubTemplate.setMessageConverter(new JacksonPubSubMessageConverter(objectMapper));
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, CHAIN_SALE_SUB);
		adapter.setOutputChannel(messageChannel);
		adapter.setAckMode(AckMode.AUTO_ACK);
		adapter.setPayloadType(Sale.class);
		return adapter;
	}



	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
