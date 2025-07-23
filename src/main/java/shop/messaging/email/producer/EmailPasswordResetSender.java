package shop.messaging.email.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.messaging.email.dto.EmailPasswordResetPayload;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailPasswordResetSender {
	
	private final RabbitTemplate rabbitTemplate;
	
	@Value("${rabbitmq.exchange.email-exchange}")
	private String exchangeName;
	@Value("${rabbitmq.routing-key.password-reset}")
	private String routingKey;
	
	public void sendPasswordResetToken(EmailPasswordResetPayload passwordResetPayload) {
		try {
			rabbitTemplate.convertAndSend(exchangeName, routingKey, passwordResetPayload);
		}catch(AmqpException e) {
			log.error("Could not send password reset payload for email {}",
					passwordResetPayload.getToEmail(), e);
		}
	}
	
}
