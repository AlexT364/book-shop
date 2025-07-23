package shop.messaging.email.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import shop.messaging.email.dto.EmailContactReplyPayload;

@Component
@RequiredArgsConstructor
public class EmailContactReplySender {
	
	private final RabbitTemplate rabbitTemplate;
	@Value("${rabbitmq.exchange.email-exchange}")
	private String exchangeName;
	@Value("${rabbitmq.routing-key.contact-reply}")
	private String routingKey;
	
	public void sendReplyMessage(EmailContactReplyPayload replyPayload) {
		rabbitTemplate.convertAndSend(exchangeName, routingKey, replyPayload);
	}
}
