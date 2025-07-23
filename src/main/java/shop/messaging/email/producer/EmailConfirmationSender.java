package shop.messaging.email.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.messaging.email.dto.EmailConfirmationPayload;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailConfirmationSender {
	
	private final RabbitTemplate rabbitTemplate;
	@Value("${rabbitmq.exchange.email-exchange}")
	private String exchangeName;
	@Value("${rabbitmq.routing-key.registration-confirmation}")
	private String routingKey;
	
	public void sendConfirmationToken(EmailConfirmationPayload confirmationPayload) {
		try {
			rabbitTemplate.convertAndSend(exchangeName, routingKey, confirmationPayload);
		}catch(AmqpException e) {
			log.error("Could not send confiramtion payload for email {}", 
					confirmationPayload.getToEmail(), e);
		}
	}
}
