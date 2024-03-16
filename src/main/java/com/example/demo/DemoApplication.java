package com.example.demo;

import com.example.demo.dto.Sale;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;

@SpringBootApplication
@Slf4j
public class DemoApplication {

	private static final String CHAIN_SALE_SUB = "dev.chain.sale-sub";



	/*@Bean
	JacksonPubSubMessageConverter createConverter(ObjectMapper mapper) {
		return new JacksonPubSubMessageConverter(mapper);
	}

	 */
	@Bean
	MessageChannel saleInputChannel() {
		return new PublishSubscribeChannel();
	}

	@Bean
	PubSubInboundChannelAdapter saleInputChannelAdapter(
			@Qualifier("saleInputChannel") MessageChannel msgChannel,
			PubSubTemplate pubSubTemplate,
			ObjectMapper objectMapper
	) {
		pubSubTemplate.setMessageConverter(new JacksonPubSubMessageConverter(objectMapper));
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, CHAIN_SALE_SUB);
		adapter.setOutputChannel(msgChannel);
		adapter.setAckMode(AckMode.MANUAL);
		adapter.setPayloadType(Sale.class);
		return adapter;
	}


	@Bean
	MessageChannel stringInputChannel() {
		return new PublishSubscribeChannel();
	}


	@Bean
	public PubSubInboundChannelAdapter inboundChannelAdapter(
			@Qualifier("stringInputChannel") MessageChannel messageChannel,
			PubSubTemplate pubSubTemplate)
	{
		PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "dev.string-sub");
		adapter.setOutputChannel(messageChannel);
		adapter.setAckMode(AckMode.MANUAL);
		adapter.setPayloadType(String.class);
		return adapter;
	}

	@ServiceActivator(inputChannel = "stringInputChannel")
	public void messageReceiver(String payload,	@Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {
		log.info("Message arrived via an inbound channel adapter from sub-one! Payload: {} ",  payload);
		message.ack();
	}




	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
